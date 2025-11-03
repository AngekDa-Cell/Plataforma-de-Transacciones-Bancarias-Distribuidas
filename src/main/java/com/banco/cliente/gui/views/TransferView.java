/**
 * Esqueleto de la Vista de Transferencia.
 */


 
public class TransferView extends VBox {

    private ClienteBancarioGUI mainApp;
    private String idCuentaOrigen;

    public TransferView(ClienteBancarioGUI mainApp, String idCuentaOrigen) {
        this.mainApp = mainApp;
        this.idCuentaOrigen = idCuentaOrigen;

        // --- INICIO ESQUELETO (Mi compañero lo diseña) ---
        Label title = new Label("Realizar Transferencia");
        TextField destinoField = new TextField();
        destinoField.setPromptText("Cuenta Destino");
        TextField montoField = new TextField();
        montoField.setPromptText("Monto");
        Button confirmButton = new Button("Confirmar Transferencia");
        Button cancelButton = new Button("Cancelar");
        Label statusLabel = new Label(); // Para "Éxito" o "Error"
        // --- FIN ESQUELETO ---

        // Lógica de botones (esqueleto)
        confirmButton.setOnAction(e -> {
            // TODO: Lógica RMI para transferir fondos
            // mainApp.getRmiConnector().getBancoStub().transferirFondos(...)
            // statusLabel.setText("¡Transferencia exitosa!");
            System.out.println("Placeholder: Transfiriendo...");
        });

        cancelButton.setOnAction(e -> {
            mainApp.mostrarDashboardView(idCuentaOrigen);
        });

        // --- INICIO LAYOUT BÁSICO (Mi compañero lo diseña) ---
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(title, destinoField, montoField, confirmButton, cancelButton, statusLabel);
        // --- FIN LAYOUT BÁSICO ---
    }
}

