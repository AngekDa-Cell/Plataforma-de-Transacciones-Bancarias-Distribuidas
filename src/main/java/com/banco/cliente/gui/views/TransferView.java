package com.banco.cliente.gui.views;

import com.banco.cliente.gui.ClienteBancarioGUI;
import javax.swing.*;
import java.awt.*;

/**
 * Vista de Transferencia implementada con Swing (estilo Tkinter).
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
        
        // Configurar el panel con layout GridBagLayout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Título
        JLabel titleLabel = new JLabel("Realizar Transferencia");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);
        
        // Información de cuenta origen
        gbc.gridy = 1;
        JLabel origenLabel = new JLabel("Desde cuenta: " + idCuentaOrigen);
        origenLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        add(origenLabel, gbc);
        
        // Label y campo de cuenta destino
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Cuenta Destino:"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        destinoField = new JTextField(15);
        add(destinoField, gbc);
        
        // Label y campo de monto
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Monto:"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        montoField = new JTextField(15);
        add(montoField, gbc);
        
        // Panel de botones
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton confirmButton = new JButton("Confirmar Transferencia");
        JButton cancelButton = new JButton("Cancelar");
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        add(buttonPanel, gbc);
        
        // Label de estado
        gbc.gridy = 5;
        statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        add(statusLabel, gbc);
        
        // Acciones de botones
        confirmButton.addActionListener(e -> realizarTransferencia());
        
        cancelButton.addActionListener(e -> mainApp.mostrarDashboardView(idCuentaOrigen));
        
        // Permitir confirmar con Enter
        montoField.addActionListener(e -> realizarTransferencia());
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

