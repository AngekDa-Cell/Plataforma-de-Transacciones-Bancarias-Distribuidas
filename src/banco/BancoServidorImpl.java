package banco;

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
    }

    @Override
    public boolean autenticar(String idCuenta, String password) throws RemoteException {
        String passAlmacenada = credenciales.get(idCuenta);
        return passAlmacenada != null && passAlmacenada.equals(password);
    }

    @Override
    public double consultarSaldo(String idCuenta) throws RemoteException {
        Double saldo = saldos.get(idCuenta);
        return saldo != null ? saldo : 0.0;
    }

    @Override
    public synchronized boolean transferirFondos(String idCuentaOrigen, String idCuentaDestino, double monto) throws RemoteException {
        if (monto <= 0) return false;
        Double saldoOrigen = saldos.get(idCuentaOrigen);
        Double saldoDestino = saldos.get(idCuentaDestino);
        if (saldoOrigen == null || saldoDestino == null) return false;
        if (saldoOrigen < monto) return false;
        saldos.put(idCuentaOrigen, saldoOrigen - monto);
        saldos.put(idCuentaDestino, saldoDestino + monto);
        return true;
    }

    @Override
    public synchronized boolean registrarOperacion(String idCuenta, String tipoOperacion, double monto) throws RemoteException {
        if (monto <= 0) return false;
        Double saldo = saldos.get(idCuenta);
        if (saldo == null) return false;
        String tipo = tipoOperacion == null ? "" : tipoOperacion.trim().toUpperCase();
        switch (tipo) {
            case "DEPOSITO":
                saldos.put(idCuenta, saldo + monto);
                return true;
            case "RETIRO":
                if (saldo < monto) return false;
                saldos.put(idCuenta, saldo - monto);
                return true;
            default:
                return false;
        }
    }
}
