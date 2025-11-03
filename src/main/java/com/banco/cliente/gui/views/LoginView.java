/**
 * Esqueleto de la Vista de Login.
 */



public class LoginView extends VBox {

    private ClienteBancarioGUI mainApp;

    public LoginView(ClienteBancarioGUI mainApp) {
        this.mainApp = mainApp;

        // --- INICIO ESQUELETO (Mi compañero lo diseña) ---
        Label title = new Label("Inicio de Sesión");
        TextField cuentaField = new TextField();
        cuentaField.setPromptText("ID de Cuenta");
        
        PasswordField passField = new PasswordField();
        passField.setPromptText("Contraseña");
        
        Button loginButton = new Button("Ingresar");
        Label errorLabel = new Label(); // Para mensajes de error
        // --- FIN ESQUELETO ---

        // Lógica de botón (esqueleto)
        loginButton.setOnAction(e -> {
            // TODO: Lógica de autenticación va aquí.
            // 1. Conectar RMI: mainApp.getRmiConnector().conectar()
            // 2. Autenticar: mainApp.getRmiConnector().getBancoStub().autenticar(...)
            // 3. Si es exitoso:
            //    mainApp.mostrarDashboardView(cuentaField.getText());
            // 4. Si falla:
            //    errorLabel.setText("Error: Usuario o contraseña incorrectos.");
            
            System.out.println("Placeholder: Intentando ingresar...");
            // Simulación de éxito para pruebas de navegación:
             mainApp.mostrarDashboardView(cuentaField.getText());
        });

        // --- INICIO LAYOUT BÁSICO (Mi compañero lo diseña) ---
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(title, cuentaField, passField, loginButton, errorLabel);
        // --- FIN LAYOUT BÁSICO ---
    }
}
