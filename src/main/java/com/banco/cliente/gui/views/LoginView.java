package com.banco.cliente.gui.views;

import com.banco.cliente.gui.ClienteBancarioGUI;
import com.banco.security.SecurityUtil;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
 * Vista de Login implementada con Swing (diseño mejorado).
 */
public class LoginView extends JPanel {

    private ClienteBancarioGUI mainApp;
    private JTextField cuentaField;
    private JPasswordField passField;
    private JLabel errorLabel;
    private JTextField keystorePathField;
    private JPasswordField keystorePassField;
    private JTextField aliasField;
    private JPasswordField keyPassField;
    private JCheckBox usarCertCheck;

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
        
    // Label y campo de Contraseña con estilo (solo si login clásico)
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
        
    // Checkbox para usar certificado
    gbcCenter.gridy = 5;
    gbcCenter.gridx = 0;
    gbcCenter.gridwidth = 2;
    usarCertCheck = new JCheckBox("Usar autenticación con certificado");
    usarCertCheck.setBackground(Color.WHITE);
    usarCertCheck.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    centerPanel.add(usarCertCheck, gbcCenter);

    // Campos de keystore (ocultos hasta marcar el check)
    gbcCenter.gridwidth = 1;
    gbcCenter.gridy = 6; gbcCenter.gridx = 0; gbcCenter.anchor = GridBagConstraints.EAST;
    JLabel ksPathLabel = new JLabel("Keystore (.p12):");
    ksPathLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    centerPanel.add(ksPathLabel, gbcCenter);
    gbcCenter.gridx = 1; gbcCenter.anchor = GridBagConstraints.WEST;
    keystorePathField = new JTextField(18);
    keystorePathField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    centerPanel.add(keystorePathField, gbcCenter);

    gbcCenter.gridy = 7; gbcCenter.gridx = 0; gbcCenter.anchor = GridBagConstraints.EAST;
    JLabel ksPassLabel = new JLabel("Password KS:");
    ksPassLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    centerPanel.add(ksPassLabel, gbcCenter);
    gbcCenter.gridx = 1; gbcCenter.anchor = GridBagConstraints.WEST;
    keystorePassField = new JPasswordField(18);
    keystorePassField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    centerPanel.add(keystorePassField, gbcCenter);

    gbcCenter.gridy = 8; gbcCenter.gridx = 0; gbcCenter.anchor = GridBagConstraints.EAST;
    JLabel aliasLabel = new JLabel("Alias:");
    aliasLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    centerPanel.add(aliasLabel, gbcCenter);
    gbcCenter.gridx = 1; gbcCenter.anchor = GridBagConstraints.WEST;
    aliasField = new JTextField(18);
    aliasField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    centerPanel.add(aliasField, gbcCenter);

    gbcCenter.gridy = 9; gbcCenter.gridx = 0; gbcCenter.anchor = GridBagConstraints.EAST;
    JLabel keyPassLabel = new JLabel("Password Clave:");
    keyPassLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    centerPanel.add(keyPassLabel, gbcCenter);
    gbcCenter.gridx = 1; gbcCenter.anchor = GridBagConstraints.WEST;
    keyPassField = new JPasswordField(18);
    keyPassField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    centerPanel.add(keyPassField, gbcCenter);

    // Inicialmente ocultar campos de certificado
    toggleCertFields(false);
    usarCertCheck.addActionListener(e -> toggleCertFields(usarCertCheck.isSelected()));

    // Botón de Login con estilo moderno
        gbcCenter.gridy = 5;
    // Reposicionar: si se usan campos de cert, botón al final
    gbcCenter.gridy = 10;
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
    gbcCenter.gridy = 11;
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
    
    private void toggleCertFields(boolean enable) {
        keystorePathField.setEnabled(enable);
        keystorePassField.setEnabled(enable);
        aliasField.setEnabled(enable);
        keyPassField.setEnabled(enable);
        passField.setEnabled(!enable); // deshabilitar password tradicional si se usa certificado
    }

    private void realizarLogin() {
        String idCuenta = cuentaField.getText().trim();
        String password = new String(passField.getPassword());
        
        if (idCuenta.isEmpty()) {
            errorLabel.setText("Ingrese ID de cuenta");
            return;
        }

        // Conectar RMI si no está
        if (!mainApp.getRmiConnector().conectar()) {
            errorLabel.setText("Error de conexión RMI");
            return;
        }

        boolean usarCert = usarCertCheck.isSelected();
        if (usarCert) {
            String ksPath = keystorePathField.getText().trim();
            String ksPass = new String(keystorePassField.getPassword());
            String alias = aliasField.getText().trim();
            String keyPass = new String(keyPassField.getPassword());
            if (ksPath.isEmpty() || ksPass.isEmpty() || alias.isEmpty() || keyPass.isEmpty()) {
                errorLabel.setText("Complete campos de certificado");
                return;
            }
            if (!mainApp.getRmiConnector().cargarKeyStoreCliente(ksPath, ksPass)) {
                errorLabel.setText("Keystore inválido");
                return;
            }
            PrivateKey pk = mainApp.getRmiConnector().obtenerPrivateKey(alias, keyPass);
            X509Certificate cert = mainApp.getRmiConnector().obtenerCertificado(alias);
            if (pk == null || cert == null) {
                errorLabel.setText("Clave o certificado no encontrados");
                return;
            }
            try {
                long ts = System.currentTimeMillis();
                String canonical = String.format("LOGIN|%s|%d", idCuenta, ts);
                byte[] firma = SecurityUtil.sign(canonical.getBytes("UTF-8"), pk);
                boolean ok = mainApp.getRmiConnector().getBancoStub().autenticarCert(
                        idCuenta, ts, firma, cert.getEncoded());
                if (ok) {
                    mainApp.setSecurityContext(pk, cert, alias, idCuenta);
                    errorLabel.setText("");
                    mainApp.mostrarDashboardView(idCuenta);
                } else {
                    errorLabel.setText("Autenticación con certificado fallida");
                }
            } catch (Exception ex) {
                errorLabel.setText("Error cert: " + ex.getMessage());
            }
        } else {
            if (password.isEmpty()) {
                errorLabel.setText("Ingrese contraseña");
                return;
            }
            try {
                boolean autenticado = mainApp.getRmiConnector().getBancoStub().autenticar(idCuenta, password);
                if (autenticado) {
                    mainApp.setSecurityContext(null, null, null, idCuenta);
                    errorLabel.setText("");
                    mainApp.mostrarDashboardView(idCuenta);
                } else {
                    errorLabel.setText("Credenciales inválidas");
                }
            } catch (Exception ex) {
                errorLabel.setText("Error auth: " + ex.getMessage());
            }
        }
    }
}
