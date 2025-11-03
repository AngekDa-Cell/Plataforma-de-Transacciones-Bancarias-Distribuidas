package banco;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorRMI {
    public static void main(String[] args) {
        try {
            BancoServidorImpl servidor = new BancoServidorImpl();
            Registry registro = LocateRegistry.createRegistry(1099);
            registro.rebind("BancoServidor", servidor);
            System.out.println("Servidor RMI de Banco listo y escuchando en el puerto 1099.");
        } catch (RemoteException e) {
            System.err.println("Error de RMI: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
