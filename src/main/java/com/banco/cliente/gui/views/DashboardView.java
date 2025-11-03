/**
 * Esqueleto de la Vista de Dashboard.
 */


 
public class DashboardView extends VBox {

    private ClienteBancarioGUI mainApp;
    private String idCuenta;

    public DashboardView(ClienteBancarioGUI mainApp, String idCuenta) {
        this.mainApp = mainApp;
        this.idCuenta = idCuenta;

        // --- INICIO ESQUELETO (Mi compañero lo diseña) ---
        Label welcomeLabel = new Label("Bienvenido, " + idCuenta);
        Label saldoLabel = new Label("Saldo Actual: $...cargando...");
        Button transferButton = new Button("Realizar Transferencia");
        Button opButton = new Button("Depósito / Retiro");
        Button logoutButton = new Button("Salir");
        // --- FIN ESQUELETO ---
        
        // TODO: Lógica para cargar el saldo inicial usando RMI
        // saldoLabel.setText("Saldo Actual: $" + mainApp.getRmiConnector()...consultarSaldo(idCuenta));

        // Lógica de botones (esqueleto)
        transferButton.setOnAction(e -> {
            // TODO: mainApp.mostrarVistaTransferencia(idCuenta);
            System.out.println("Placeholder: Ir a Transferencia");
        });
        
        opButton.setOnAction(e -> {
            // TODO: mainApp.mostrarVistaOperacion(idCuenta);
            System.out.println("Placeholder: Ir a Depósito/Retiro");
        });

        logoutButton.setOnAction(e -> {
            mainApp.mostrarLoginView();
        });

        // --- INICIO LAYOUT BÁSICO (Mi compañero lo diseña) ---
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(welcomeLabel, saldoLabel, transferButton, opButton, logoutButton);
        // --- FIN LAYOUT BÁSICO ---
    }
}
