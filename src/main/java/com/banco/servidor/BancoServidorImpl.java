package com.banco.servidor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class BancoServidorImpl extends UnicastRemoteObject implements BancoServidor {

    private final Map<String, Double> saldos;
    private final Map<String, String> credenciales;

    public BancoServidorImpl() throws RemoteException {
        super();
        this.saldos = new HashMap<>();
        this.credenciales = new HashMap<>();
        
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
}
