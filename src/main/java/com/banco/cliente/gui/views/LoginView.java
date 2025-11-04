package com.banco.cliente.gui.views;

import com.banco.cliente.gui.ClienteBancarioGUI;
import javax.swing.*;
import java.awt.*;

/**
 * Vista de Login implementada con Swing (estilo Tkinter).
 */
public class LoginView extends JPanel {

    private ClienteBancarioGUI mainApp;
    private JTextField cuentaField;
    private JPasswordField passField;
    private JLabel errorLabel;

    public LoginView(ClienteBancarioGUI mainApp) {
        this.mainApp = mainApp;
        
        // Configurar el panel con layout GridBagLayout para más control
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Título
        JLabel titleLabel = new JLabel("Inicio de Sesión");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);
        
        // Label y campo de ID de Cuenta
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("ID de Cuenta:"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        cuentaField = new JTextField(15);
        add(cuentaField, gbc);
        
        // Label y campo de Contraseña
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Contraseña:"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        passField = new JPasswordField(15);
        add(passField, gbc);
        
        // Botón de Login
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Ingresar");
        loginButton.setPreferredSize(new Dimension(120, 30));
        add(loginButton, gbc);
        
        // Label de error
        gbc.gridy = 4;
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        add(errorLabel, gbc);
        
        // Acción del botón de login
        loginButton.addActionListener(e -> realizarLogin());
        
        // Permitir login con Enter
        passField.addActionListener(e -> realizarLogin());
    }
    
    private void realizarLogin() {
        String idCuenta = cuentaField.getText().trim();
        String password = new String(passField.getPassword());
        
        if (idCuenta.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Por favor complete todos los campos");
            return;
        }
        
        // Intentar conectar al servidor RMI
        if (!mainApp.getRmiConnector().conectar()) {
            errorLabel.setText("Error: No se puede conectar al servidor");
            JOptionPane.showMessageDialog(mainApp.getMainFrame(),
                "No se pudo conectar al servidor RMI.\nVerifique que el servidor esté ejecutándose.",
                "Error de Conexión",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Autenticar con el servidor
            boolean autenticado = mainApp.getRmiConnector().getBancoStub()
                .autenticar(idCuenta, password);
            
            if (autenticado) {
                errorLabel.setText("");
                mainApp.mostrarDashboardView(idCuenta);
            } else {
                errorLabel.setText("Usuario o contraseña incorrectos");
                passField.setText("");
            }
        } catch (Exception ex) {
            errorLabel.setText("Error en la autenticación");
            JOptionPane.showMessageDialog(mainApp.getMainFrame(),
                "Error al autenticar: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
