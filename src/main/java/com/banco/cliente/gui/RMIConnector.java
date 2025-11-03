/**
 * Clase de utilidad (helper) para gestionar la conexión RMI.
 */


 
public class RMIConnector {

    private BancoServidor bancoStub;

    /**
     * Intenta conectar con el servidor RMI.
     * @return true si la conexión es exitosa, false en caso contrario.
     */
    public boolean conectar() {
        try {
            // Conecta al registro RMI en localhost, puerto 1099
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            // Busca el objeto remoto "BancoServidor"
            bancoStub = (BancoServidor) registry.lookup("BancoServidor");
            return true;
        } catch (Exception e) {
            System.err.println("Error al conectar RMI: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Devuelve el objeto stub RMI para hacer las llamadas remotas.
     * @return El stub de BancoServidor, o null si no está conectado.
     */
    public BancoServidor getBancoStub() {
        return bancoStub;
    }
}
