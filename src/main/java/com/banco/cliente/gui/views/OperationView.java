package com.banco.cliente.gui.views;

import com.banco.cliente.gui.ClienteBancarioGUI;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Vista de Operaciones (Depósito/Retiro) implementada con Swing (diseño mejorado).
 */
public class OperationView extends JPanel {

    private ClienteBancarioGUI mainApp;
    private String idCuenta;
    private JRadioButton rbDeposito;
    private JRadioButton rbRetiro;
    private JTextField montoField;
    private JLabel statusLabel;

    public OperationView(ClienteBancarioGUI mainApp, String idCuenta) {
        this.mainApp = mainApp;
        this.idCuenta = idCuenta;
        
        // Configurar el panel con BorderLayout
        setLayout(new BorderLayout(0, 15));
        setBackground(new Color(240, 248, 255)); // AliceBlue
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Panel superior - Cabecera
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 144, 255)); // DodgerBlue
        headerPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        
        // Botón volver
        JButton volverBtn = new JButton("← Volver");
        volverBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        volverBtn.setForeground(Color.WHITE);
        volverBtn.setBackground(new Color(30, 144, 255));
        volverBtn.setBorderPainted(false);
        volverBtn.setFocusPainted(false);
        volverBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        volverBtn.addActionListener(e -> mainApp.mostrarDashboardView(idCuenta));
        headerPanel.add(volverBtn, BorderLayout.WEST);
        
        JLabel titleLabel = new JLabel("💵 Depósito / Retiro");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        JLabel cuentaLabel = new JLabel("Cuenta: " + idCuenta);
        cuentaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cuentaLabel.setForeground(Color.WHITE);
        headerPanel.add(cuentaLabel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Panel central con formulario
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(30, 144, 255), 2, true),
            new EmptyBorder(30, 40, 30, 40)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Título de sección
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel seccionLabel = new JLabel("Seleccione el tipo de operación:");
        seccionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        seccionLabel.setForeground(new Color(25, 25, 112));
        centerPanel.add(seccionLabel, gbc);
        
        // Radio buttons con estilo
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        radioPanel.setBackground(Color.WHITE);
        
        ButtonGroup group = new ButtonGroup();
        rbDeposito = new JRadioButton("💰 Depositar", true);
        rbDeposito.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rbDeposito.setBackground(Color.WHITE);
        rbDeposito.setFocusPainted(false);
        
        rbRetiro = new JRadioButton("💸 Retirar");
        rbRetiro.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rbRetiro.setBackground(Color.WHITE);
        rbRetiro.setFocusPainted(false);
        
        group.add(rbDeposito);
        group.add(rbRetiro);
        
        radioPanel.add(rbDeposito);
        radioPanel.add(rbRetiro);
        
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        centerPanel.add(radioPanel, gbc);
        
        // Separador
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 12, 12, 12);
        JSeparator separator = new JSeparator();
        centerPanel.add(separator, gbc);
        
        // Label y campo de monto
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(12, 12, 12, 12);
        JLabel montoLabel = new JLabel("Monto:");
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
        
        JButton confirmButton = crearBoton("✓ Confirmar", new Color(34, 139, 34));
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
        confirmButton.addActionListener(e -> realizarOperacion());
        cancelButton.addActionListener(e -> mainApp.mostrarDashboardView(idCuenta));
        
        // Permitir confirmar con Enter
        montoField.addActionListener(e -> realizarOperacion());
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(150, 40));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    private void realizarOperacion() {
        String montoStr = montoField.getText().trim();
        
        if (montoStr.isEmpty()) {
            statusLabel.setText("Por favor ingrese un monto");
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
        
        String tipoOperacion = rbDeposito.isSelected() ? "DEPOSITO" : "RETIRO";
        
        try {
            boolean exitoso = mainApp.getRmiConnector().getBancoStub()
                .registrarOperacion(idCuenta, tipoOperacion, monto);
            
            if (exitoso) {
                statusLabel.setText("¡Operación exitosa!");
                statusLabel.setForeground(new Color(0, 128, 0));
                
                JOptionPane.showMessageDialog(mainApp.getMainFrame(),
                    String.format("%s de $%.2f realizado correctamente", 
                        tipoOperacion, monto),
                    "Operación Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Volver al dashboard después de un momento
                Timer timer = new Timer(1500, ev -> mainApp.mostrarDashboardView(idCuenta));
                timer.setRepeats(false);
                timer.start();
            } else {
                statusLabel.setText("Error: Operación fallida");
                statusLabel.setForeground(Color.RED);
                JOptionPane.showMessageDialog(mainApp.getMainFrame(),
                    "No se pudo realizar la operación.\nVerifique el saldo disponible.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            statusLabel.setText("Error en la operación");
            statusLabel.setForeground(Color.RED);
            JOptionPane.showMessageDialog(mainApp.getMainFrame(),
                "Error al realizar operación: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
