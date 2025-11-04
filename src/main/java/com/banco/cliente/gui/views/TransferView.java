package com.banco.cliente.gui.views;

import com.banco.cliente.gui.ClienteBancarioGUI;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Vista de Transferencia implementada con Swing (diseño mejorado).
 */
public class TransferView extends JPanel {

    private ClienteBancarioGUI mainApp;
    private String idCuentaOrigen;
    private JTextField destinoField;
    private JTextField montoField;
    private JLabel statusLabel;

    public TransferView(ClienteBancarioGUI mainApp, String idCuentaOrigen) {
        this.mainApp = mainApp;
        this.idCuentaOrigen = idCuentaOrigen;
        
        // Configurar el panel con BorderLayout
        setLayout(new BorderLayout(0, 15));
        setBackground(new Color(240, 248, 255)); // AliceBlue
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Panel superior - Cabecera
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180)); // SteelBlue
        headerPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        
        // Botón volver
        JButton volverBtn = new JButton("← Volver");
        volverBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        volverBtn.setForeground(Color.WHITE);
        volverBtn.setBackground(new Color(70, 130, 180));
        volverBtn.setBorderPainted(false);
        volverBtn.setFocusPainted(false);
        volverBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        volverBtn.addActionListener(e -> mainApp.mostrarDashboardView(idCuentaOrigen));
        headerPanel.add(volverBtn, BorderLayout.WEST);
        
        JLabel titleLabel = new JLabel("💸 Realizar Transferencia");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Panel central con formulario
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(70, 130, 180), 2, true),
            new EmptyBorder(30, 40, 30, 40)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Información de cuenta origen
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        
        JPanel origenPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        origenPanel.setBackground(new Color(240, 248, 255));
        origenPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel origenLabel = new JLabel("📤 Desde cuenta: " + idCuentaOrigen);
        origenLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        origenLabel.setForeground(new Color(25, 25, 112));
        origenPanel.add(origenLabel);
        
        centerPanel.add(origenPanel, gbc);
        
        // Separador
        gbc.gridy = 1;
        gbc.insets = new Insets(15, 12, 15, 12);
        JSeparator separator = new JSeparator();
        centerPanel.add(separator, gbc);
        
        // Label y campo de cuenta destino
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(12, 12, 12, 12);
        JLabel destinoLabel = new JLabel("📥 Cuenta Destino:");
        destinoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        centerPanel.add(destinoLabel, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        destinoField = new JTextField(18);
        destinoField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        destinoField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        centerPanel.add(destinoField, gbc);
        
        // Label y campo de monto
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel montoLabel = new JLabel("💵 Monto:");
        montoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        centerPanel.add(montoLabel, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        montoField = new JTextField(18);
        montoField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        montoField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        centerPanel.add(montoField, gbc);
        
        // Panel de botones
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(25, 12, 12, 12);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton confirmButton = crearBoton("✓ Confirmar Transferencia", new Color(34, 139, 34));
        JButton cancelButton = crearBoton("✗ Cancelar", new Color(220, 20, 60));
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        centerPanel.add(buttonPanel, gbc);
        
        // Label de estado
        gbc.gridy = 5;
        gbc.insets = new Insets(15, 12, 0, 12);
        statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(statusLabel, gbc);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Acciones de botones
        confirmButton.addActionListener(e -> realizarTransferencia());
        cancelButton.addActionListener(e -> mainApp.mostrarDashboardView(idCuentaOrigen));
        
        // Permitir confirmar con Enter
        montoField.addActionListener(e -> realizarTransferencia());
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(200, 40));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    private void realizarTransferencia() {
        String cuentaDestino = destinoField.getText().trim();
        String montoStr = montoField.getText().trim();
        
        if (cuentaDestino.isEmpty() || montoStr.isEmpty()) {
            statusLabel.setText("Complete todos los campos");
            statusLabel.setForeground(Color.RED);
            return;
        }
        
        if (cuentaDestino.equals(idCuentaOrigen)) {
            statusLabel.setText("No puede transferir a la misma cuenta");
            statusLabel.setForeground(Color.RED);
            return;
        }
        
        double monto;
        try {
            monto = Double.parseDouble(montoStr);
            if (monto <= 0) {
                statusLabel.setText("El monto debe ser mayor a 0");
                statusLabel.setForeground(Color.RED);
                return;
            }
        } catch (NumberFormatException ex) {
            statusLabel.setText("Monto inválido");
            statusLabel.setForeground(Color.RED);
            return;
        }
        
        // Confirmar la transferencia
        int confirm = JOptionPane.showConfirmDialog(
            mainApp.getMainFrame(),
            String.format("¿Confirma transferir $%.2f a la cuenta %s?", monto, cuentaDestino),
            "Confirmar Transferencia",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        try {
            boolean exitoso = mainApp.getRmiConnector().getBancoStub()
                .transferirFondos(idCuentaOrigen, cuentaDestino, monto);
            
            if (exitoso) {
                statusLabel.setText("¡Transferencia exitosa!");
                statusLabel.setForeground(new Color(0, 128, 0));
                
                JOptionPane.showMessageDialog(mainApp.getMainFrame(),
                    String.format("Transferencia de $%.2f a cuenta %s realizada correctamente", 
                        monto, cuentaDestino),
                    "Transferencia Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Volver al dashboard después de un momento
                Timer timer = new Timer(1500, ev -> mainApp.mostrarDashboardView(idCuentaOrigen));
                timer.setRepeats(false);
                timer.start();
            } else {
                statusLabel.setText("Error: Transferencia fallida");
                statusLabel.setForeground(Color.RED);
                JOptionPane.showMessageDialog(mainApp.getMainFrame(),
                    "No se pudo realizar la transferencia.\nVerifique el saldo disponible y que la cuenta destino exista.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            statusLabel.setText("Error en la transferencia");
            statusLabel.setForeground(Color.RED);
            JOptionPane.showMessageDialog(mainApp.getMainFrame(),
                "Error al realizar transferencia: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}

