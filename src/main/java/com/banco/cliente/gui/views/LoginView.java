package com.banco.cliente.gui.views;

import com.banco.cliente.gui.ClienteBancarioGUI;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Vista de Login implementada con Swing (diseño mejorado).
 */
public class LoginView extends JPanel {

    private ClienteBancarioGUI mainApp;
    private JTextField cuentaField;
    private JPasswordField passField;
    private JLabel errorLabel;

    public LoginView(ClienteBancarioGUI mainApp) {
        this.mainApp = mainApp;
        
        // Configurar el panel principal con color de fondo
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 248, 255)); // AliceBlue
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Panel central con borde y sombra
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(70, 130, 180), 2, true),
            new EmptyBorder(30, 40, 30, 40)
        ));
        
        GridBagConstraints gbcCenter = new GridBagConstraints();
        gbcCenter.insets = new Insets(8, 8, 8, 8);
        gbcCenter.fill = GridBagConstraints.HORIZONTAL;
        
        // Icono y título
        JLabel iconLabel = new JLabel("🏦");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbcCenter.gridx = 0;
        gbcCenter.gridy = 0;
        gbcCenter.gridwidth = 2;
        centerPanel.add(iconLabel, gbcCenter);
        
        JLabel titleLabel = new JLabel("Banco Distribuido");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(25, 25, 112)); // MidnightBlue
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbcCenter.gridy = 1;
        centerPanel.add(titleLabel, gbcCenter);
        
        JLabel subtitleLabel = new JLabel("Inicio de Sesión");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbcCenter.gridy = 2;
        gbcCenter.insets = new Insets(0, 8, 15, 8);
        centerPanel.add(subtitleLabel, gbcCenter);
        
        gbcCenter.insets = new Insets(8, 8, 8, 8);
        gbcCenter.insets = new Insets(8, 8, 8, 8);
        
        // Label y campo de ID de Cuenta con estilo
        gbcCenter.gridwidth = 1;
        gbcCenter.gridy = 3;
        gbcCenter.gridx = 0;
        gbcCenter.anchor = GridBagConstraints.EAST;
        JLabel cuentaLabel = new JLabel("ID de Cuenta:");
        cuentaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        centerPanel.add(cuentaLabel, gbcCenter);
        
        gbcCenter.gridx = 1;
        gbcCenter.anchor = GridBagConstraints.WEST;
        cuentaField = new JTextField(18);
        cuentaField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cuentaField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
        centerPanel.add(cuentaField, gbcCenter);
        
        // Label y campo de Contraseña con estilo
        gbcCenter.gridy = 4;
        gbcCenter.gridx = 0;
        gbcCenter.anchor = GridBagConstraints.EAST;
        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        centerPanel.add(passLabel, gbcCenter);
        
        gbcCenter.gridx = 1;
        gbcCenter.anchor = GridBagConstraints.WEST;
        passField = new JPasswordField(18);
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
        centerPanel.add(passField, gbcCenter);
        
        // Botón de Login con estilo moderno
        gbcCenter.gridy = 5;
        gbcCenter.gridx = 0;
        gbcCenter.gridwidth = 2;
        gbcCenter.anchor = GridBagConstraints.CENTER;
        gbcCenter.insets = new Insets(20, 8, 8, 8);
        JButton loginButton = new JButton("Ingresar");
        loginButton.setPreferredSize(new Dimension(200, 40));
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loginButton.setBackground(new Color(70, 130, 180)); // SteelBlue
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setOpaque(true);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        centerPanel.add(loginButton, gbcCenter);
        
        // Label de error
        gbcCenter.gridy = 6;
        gbcCenter.insets = new Insets(8, 8, 8, 8);
        errorLabel = new JLabel("");
        errorLabel.setForeground(new Color(220, 20, 60)); // Crimson
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(errorLabel, gbcCenter);
        
        // Agregar panel central al panel principal
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(centerPanel, gbc);
        
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
