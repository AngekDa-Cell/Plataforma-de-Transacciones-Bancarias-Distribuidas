package com.banco.cliente.gui.views;

import com.banco.cliente.gui.ClienteBancarioGUI;
import javax.swing.*;
import java.awt.*;

/**
 * Vista de Operaciones (Depósito/Retiro) implementada con Swing (estilo Tkinter).
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
        
        // Configurar el panel con layout GridBagLayout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Título
        JLabel titleLabel = new JLabel("Depósito o Retiro");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);
        
        // Radio buttons para tipo de operación
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        ButtonGroup group = new ButtonGroup();
        rbDeposito = new JRadioButton("Depositar", true);
        rbRetiro = new JRadioButton("Retirar");
        group.add(rbDeposito);
        group.add(rbRetiro);
        
        radioPanel.add(rbDeposito);
        radioPanel.add(rbRetiro);
        
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(radioPanel, gbc);
        
        // Label y campo de monto
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Monto:"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        montoField = new JTextField(15);
        add(montoField, gbc);
        
        // Panel de botones
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton confirmButton = new JButton("Confirmar Operación");
        JButton cancelButton = new JButton("Cancelar");
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        add(buttonPanel, gbc);
        
        // Label de estado
        gbc.gridy = 4;
        statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        add(statusLabel, gbc);
        
        // Acciones de botones
        confirmButton.addActionListener(e -> realizarOperacion());
        
        cancelButton.addActionListener(e -> mainApp.mostrarDashboardView(idCuenta));
        
        // Permitir confirmar con Enter
        montoField.addActionListener(e -> realizarOperacion());
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
