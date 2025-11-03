/**
 * Clase principal de la aplicación JavaFX.
 * Gestiona el 'Stage' principal y la navegación entre vistas (Scenes).
 */


public class ClienteBancarioGUI extends Application {

    private Stage primaryStage;
    private RMIConnector rmiConnector;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Banco Distribuido");

        this.rmiConnector = new RMIConnector();
        
        // Iniciar con la vista de Login
        mostrarLoginView();
        
        this.primaryStage.show();
    }

    /**
     * Muestra la vista de Login en el Stage principal.
     */
    public void mostrarLoginView() {
        LoginView loginView = new LoginView(this);
        cambiarEscena(loginView, 350, 250);
    }

    /**
     * Muestra el Dashboard una vez que el login es exitoso.
     * @param idCuenta El ID de la cuenta del usuario logueado.
     */
    public void mostrarDashboardView(String idCuenta) {
        DashboardView dashboardView = new DashboardView(this, idCuenta);
        cambiarEscena(dashboardView, 500, 400);
    }
    
    /**
     * Método helper para cambiar la escena (vista) actual en la ventana.
     */
    private void cambiarEscena(Pane nuevaVista, double width, double height) {
        Scene scene = new Scene(nuevaVista, width, height);
        primaryStage.setScene(scene);
    }
    
    /**
     * Devuelve el conector RMI para que las vistas lo usen.
     */
    public RMIConnector getRmiConnector() {
        return rmiConnector;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
