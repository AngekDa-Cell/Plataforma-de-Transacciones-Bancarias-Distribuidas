package banco;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClienteBancario {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            BancoServidor banco = (BancoServidor) registry.lookup("BancoServidor");

            System.out.println("Consultando saldo de CTA-1001...");
            double saldo1 = banco.consultarSaldo("CTA-1001");
            System.out.println("Saldo CTA-1001: $" + saldo1);

            System.out.println("Transfiriendo $500 de CTA-1001 a CTA-1002...");
            boolean transfOk = banco.transferirFondos("CTA-1001", "CTA-1002", 500);
            System.out.println("Transferencia exitosa: " + transfOk);

            System.out.println("Consultando nuevo saldo de CTA-1001...");
            double nuevoSaldo1 = banco.consultarSaldo("CTA-1001");
            System.out.println("Nuevo saldo CTA-1001: $" + nuevoSaldo1);

            System.out.println("Consultando nuevo saldo de CTA-1002...");
            double nuevoSaldo2 = banco.consultarSaldo("CTA-1002");
            System.out.println("Nuevo saldo CTA-1002: $" + nuevoSaldo2);

            System.out.println("Intentando retirar $6000 de CTA-1001...");
            boolean retiroOk = banco.registrarOperacion("CTA-1001", "RETIRO", 6000);
            System.out.println("Retiro exitoso: " + retiroOk);

        } catch (RemoteException e) {
            System.err.println("Error de RMI: " + e.getMessage());
            e.printStackTrace();
        } catch (NotBoundException e) {
            System.err.println("Objeto remoto no encontrado: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
