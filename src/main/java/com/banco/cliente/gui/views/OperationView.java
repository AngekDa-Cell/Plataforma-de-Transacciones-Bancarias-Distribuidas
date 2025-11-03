/**
 * Esqueleto de la Vista de Operaciones (Depósito/Retiro).
 */



public class OperationView extends VBox {

    private ClienteBancarioGUI mainApp;
    private String idCuenta;

    public OperationView(ClienteBancarioGUI mainApp, String idCuenta) {
        this.mainApp = mainApp;
        this.idCuenta = idCuenta;

        // --- INICIO ESQUELETO (Mi compañero lo diseña) ---
        Label title = new Label("Depósito o Retiro");
        
        ToggleGroup group = new ToggleGroup();
        RadioButton rbDeposito = new RadioButton("Depositar");
        rbDeposito.setToggleGroup(group);
        rbDeposito.setSelected(true);
        RadioButton rbRetiro = new RadioButton("Retirar");
        rbRetiro.setToggleGroup(group);

        TextField montoField = new TextField();
        montoField.setPromptText("Monto");
        Button confirmButton = new Button("Confirmar Operación");
        Button cancelButton = new Button("Cancelar");
        Label statusLabel = new Label(); // Para "Éxito" o "Error"
        // --- FIN ESQUELETO ---

        // Lógica de botones (esqueleto)
        confirmButton.setOnAction(e -> {
            // TODO: Lógica RMI para registrar operación
            // String tipo = rbDeposito.isSelected() ? "DEPOSITO" : "RETIRO";
            // mainApp.getRmiConnector().getBancoStub().registrarOperacion(idCuenta, tipo, ...);
            // statusLabel.setText("¡Operación exitosa!");
            System.out.println("Placeholder: Realizando operación...");
        });

        cancelButton.setOnAction(e -> {
            mainApp.mostrarDashboardView(idCuenta);
        });

        // --- INICIO LAYOUT BÁSICO (Mi compañero lo diseña) ---
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(title, rbDeposito, rbRetiro, montoField, confirmButton, cancelButton, statusLabel);
        // --- FIN LAYOUT BÁSICO ---
    }
}
