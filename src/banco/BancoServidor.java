package banco;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BancoServidor extends Remote {
    boolean autenticar(String idCuenta, String password) throws RemoteException;
    
    double consultarSaldo(String idCuenta) throws RemoteException;

    boolean transferirFondos(String idCuentaOrigen, String idCuentaDestino, double monto) throws RemoteException;

    boolean registrarOperacion(String idCuenta, String tipoOperacion, double monto) throws RemoteException;
}
