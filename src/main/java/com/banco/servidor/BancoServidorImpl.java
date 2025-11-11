package com.banco.servidor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.security.cert.X509Certificate;
import java.security.KeyStore;
import java.util.Locale;
import com.banco.security.SecurityUtil;
import com.banco.security.AuditLog;

public class BancoServidorImpl extends UnicastRemoteObject implements BancoServidor {

    private final Map<String, Double> saldos;
    private final Map<String, String> credenciales;
    private final KeyStore trustStore;
    private final long TIME_SKEW_MS = 10 * 60 * 1000; // 10 minutos

    public BancoServidorImpl() throws RemoteException {
        super();
        this.saldos = new HashMap<>();
        this.credenciales = new HashMap<>();
        this.trustStore = cargarTrustStore();
        
        // Cuentas de ejemplo con credenciales
        this.saldos.put("CTA-1001", 5000.0);
        this.credenciales.put("CTA-1001", "1234");
        
        this.saldos.put("CTA-1002", 2000.0);
        this.credenciales.put("CTA-1002", "1234");
        
        this.saldos.put("CTA-1003", 1000.0);
        this.credenciales.put("CTA-1003", "1234");
        
        System.out.println("Cuentas de prueba:");
        System.out.println("- CTA-1001 (password: 1234) - Saldo: $5000");
        System.out.println("- CTA-1002 (password: 1234) - Saldo: $2000");
        System.out.println("- CTA-1003 (password: 1234) - Saldo: $1000");
    }

    private KeyStore cargarTrustStore() throws RemoteException {
        try {
            String tsPath = System.getProperty("server.truststore.path");
            String tsPass = System.getProperty("server.truststore.password");
            if (tsPath == null || tsPass == null) {
                System.err.println("[WARN] Truststore del servidor no configurado (props server.truststore.path/password). Se requerirá de todas formas para verificar certificados.");
            }
            return SecurityUtil.loadKeyStore(tsPath, tsPass);
        } catch (Exception e) {
            throw new RemoteException("No se pudo cargar el truststore del servidor", e);
        }
    }

    @Override
    public boolean autenticar(String idCuenta, String password) throws RemoteException {
        String passAlmacenada = credenciales.get(idCuenta);
        boolean autenticado = passAlmacenada != null && passAlmacenada.equals(password);
        System.out.println("Intento de autenticación: " + idCuenta + " - " + (autenticado ? "EXITOSO" : "FALLIDO"));
        return autenticado;
    }

    @Override
    public double consultarSaldo(String idCuenta) throws RemoteException {
        Double saldo = saldos.get(idCuenta);
        double resultado = saldo != null ? saldo : 0.0;
        System.out.println("Consulta de saldo: " + idCuenta + " - $" + resultado);
        return resultado;
    }

    @Override
    public synchronized boolean transferirFondos(String idCuentaOrigen, String idCuentaDestino, double monto) throws RemoteException {
        System.out.println("Transferencia solicitada: " + idCuentaOrigen + " -> " + idCuentaDestino + " Monto: $" + monto);
        
        if (monto <= 0) {
            System.out.println("Transferencia rechazada: monto inválido");
            return false;
        }
        
        Double saldoOrigen = saldos.get(idCuentaOrigen);
        Double saldoDestino = saldos.get(idCuentaDestino);
        
        if (saldoOrigen == null || saldoDestino == null) {
            System.out.println("Transferencia rechazada: cuenta no existe");
            return false;
        }
        
        if (idCuentaOrigen.equals(idCuentaDestino)) {
            System.out.println("Transferencia rechazada: cuenta origen y destino son iguales");
            return false;
        }

        if (saldoOrigen < monto) {
            System.out.println("Transferencia rechazada: saldo insuficiente");
            return false;
        }
        
        saldos.put(idCuentaOrigen, saldoOrigen - monto);
        saldos.put(idCuentaDestino, saldoDestino + monto);
        System.out.println("Transferencia exitosa");
        return true;
    }

    @Override
    public synchronized boolean registrarOperacion(String idCuenta, String tipoOperacion, double monto) throws RemoteException {
        System.out.println("Operación solicitada: " + tipoOperacion + " - Cuenta: " + idCuenta + " - Monto: $" + monto);
        
        if (monto <= 0) {
            System.out.println("Operación rechazada: monto inválido");
            return false;
        }
        
        Double saldo = saldos.get(idCuenta);
        if (saldo == null) {
            System.out.println("Operación rechazada: cuenta no existe");
            return false;
        }
        
        String tipo = tipoOperacion == null ? "" : tipoOperacion.trim().toUpperCase();
        switch (tipo) {
            case "DEPOSITO":
                saldos.put(idCuenta, saldo + monto);
                System.out.println("Depósito exitoso - Nuevo saldo: $" + saldos.get(idCuenta));
                return true;
            case "RETIRO":
                if (saldo < monto) {
                    System.out.println("Retiro rechazado: saldo insuficiente");
                    return false;
                }
                saldos.put(idCuenta, saldo - monto);
                System.out.println("Retiro exitoso - Nuevo saldo: $" + saldos.get(idCuenta));
                return true;
            default:
                System.out.println("Operación rechazada: tipo inválido");
                return false;
        }
    }

    // ================= Implementación firmada =================
    public boolean autenticarCert(String idCuenta, long timestamp, byte[] firma, byte[] certificadoX509Der) throws RemoteException {
        String canonical = String.format(Locale.ROOT, "LOGIN|%s|%d", idCuenta, timestamp);
        SeguridadResultado ver = verificar("LOGIN", idCuenta, canonical, timestamp, firma, certificadoX509Der);
        if (!ver.ok) System.out.println("[SEC] LOGIN fallo: razon=" + ver.failureReason);
        AuditLog.append("LOGIN", idCuenta, canonical, ver.ok, firma, ver.certSubject, ver.failureReason);
        return ver.ok;
    }

    @Override
    public double consultarSaldoFirmado(String idCuenta, long timestamp, byte[] firma, byte[] certificadoX509Der) throws RemoteException {
        String canonical = String.format(Locale.ROOT, "CONSULTAR|%s|%d", idCuenta, timestamp);
        SeguridadResultado ver = verificar("CONSULTAR", idCuenta, canonical, timestamp, firma, certificadoX509Der);
        if (!ver.ok) {
            System.out.println("[SEC] CONSULTAR fallo: razon=" + ver.failureReason);
            throw new RemoteException("Firma/certificado inválido o expirado: " + ver.failureReason);
        }
        double resultado = consultarSaldo(idCuenta);
        AuditLog.append("CONSULTAR", idCuenta, canonical + "|resultado=" + resultado, true, firma, ver.certSubject, null);
        return resultado;
    }

    @Override
    public synchronized boolean transferirFondosFirmado(String idCuentaOrigen, String idCuentaDestino, double monto, long timestamp, byte[] firma, byte[] certificadoX509Der) throws RemoteException {
        String canonical = String.format(Locale.ROOT, "TRANSFERIR|%s|%s|%.8f|%d", idCuentaOrigen, idCuentaDestino, monto, timestamp);
        SeguridadResultado ver = verificar("TRANSFERIR", idCuentaOrigen, canonical, timestamp, firma, certificadoX509Der);
        if (!ver.ok) {
            System.out.println("[SEC] TRANSFERIR fallo: razon=" + ver.failureReason);
            throw new RemoteException("Firma/certificado inválido o expirado: " + ver.failureReason);
        }
        boolean ok = transferirFondos(idCuentaOrigen, idCuentaDestino, monto);
        AuditLog.append("TRANSFERIR", idCuentaOrigen, canonical + "|ok=" + ok, ok, firma, ver.certSubject, null);
        return ok;
    }

    @Override
    public synchronized boolean registrarOperacionFirmado(String idCuenta, String tipoOperacion, double monto, long timestamp, byte[] firma, byte[] certificadoX509Der) throws RemoteException {
        String canonical = String.format(Locale.ROOT, "OPERACION|%s|%s|%.8f|%d", idCuenta, safe(tipoOperacion), monto, timestamp);
        SeguridadResultado ver = verificar("OPERACION", idCuenta, canonical, timestamp, firma, certificadoX509Der);
        if (!ver.ok) {
            System.out.println("[SEC] OPERACION fallo: razon=" + ver.failureReason);
            throw new RemoteException("Firma/certificado inválido o expirado: " + ver.failureReason);
        }
        boolean ok = registrarOperacion(idCuenta, tipoOperacion, monto);
        AuditLog.append("OPERACION", idCuenta, canonical + "|ok=" + ok, ok, firma, ver.certSubject, null);
        return ok;
    }

    private String safe(String v) {
        return v == null ? "" : v.trim().toUpperCase(Locale.ROOT);
    }

    private SeguridadResultado verificar(String oper, String idCuenta, String canonical, long timestamp, byte[] firma, byte[] certDer) throws RemoteException {
        try {
            if (Math.abs(System.currentTimeMillis() - timestamp) > TIME_SKEW_MS) {
                return SeguridadResultado.fail("timestamp_fuera_de_ventana");
            }
            X509Certificate cert = SecurityUtil.readCertificateFromDer(certDer);
            // Validar que el certificado esté en el truststore
            boolean trusted = SecurityUtil.isCertificateInTrustStore(cert, trustStore);
            if (!trusted) return SeguridadResultado.fail("certificado_no_confiable");
            boolean signatureOk = SecurityUtil.verify(canonical.getBytes("UTF-8"), firma, cert);
            if (!signatureOk) return SeguridadResultado.fail("firma_invalida");
            // Opcional: validar que el Subject CN contenga el idCuenta
            String subject = cert.getSubjectX500Principal().getName();
            if (!subject.contains(idCuenta)) {
                System.out.println("[WARN] Subject DN no contiene el idCuenta. Subject=" + subject + " idCuenta=" + idCuenta);
            }
            return SeguridadResultado.ok(subject);
        } catch (Exception e) {
            throw new RemoteException("Error de verificación de firma/certificado: " + e.getMessage(), e);
        }
    }

    private static class SeguridadResultado {
        final boolean ok;
        final String certSubject;
        final String failureReason;
        private SeguridadResultado(boolean ok, String certSubject, String failureReason) {
            this.ok = ok; this.certSubject = certSubject; this.failureReason = failureReason;
        }
        static SeguridadResultado ok(String subject) { return new SeguridadResultado(true, subject, null); }
        static SeguridadResultado fail(String reason) { return new SeguridadResultado(false, null, reason); }
    }
}
