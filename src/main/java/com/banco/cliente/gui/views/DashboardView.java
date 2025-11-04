package com.banco.cliente.gui.views;

import com.banco.cliente.gui.ClienteBancarioGUI;
import javax.swing.*;
import java.awt.*;

/**
 * Vista de Dashboard implementada con Swing (estilo Tkinter).
 */
public class DashboardView extends JPanel {

    private ClienteBancarioGUI mainApp;
    private String idCuenta;
    private JLabel saldoLabel;

    public DashboardView(ClienteBancarioGUI mainApp, String idCuenta) {
        this.mainApp = mainApp;
        this.idCuenta = idCuenta;
        
        // Configurar el panel con layout GridBagLayout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Título de bienvenida
        JLabel welcomeLabel = new JLabel("Bienvenido, " + idCuenta);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(welcomeLabel, gbc);
        
        // Label de saldo
        saldoLabel = new JLabel("Saldo Actual: Cargando...");
        saldoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 1;
        add(saldoLabel, gbc);
        
        // Cargar saldo inicial
        cargarSaldo();
        
        // Panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));
        
        JButton transferButton = new JButton("Realizar Transferencia");
        JButton opButton = new JButton("Depósito / Retiro");
        JButton refreshButton = new JButton("Actualizar Saldo");
        JButton logoutButton = new JButton("Cerrar Sesión");
        
        // Establecer tamaño preferido para los botones
        Dimension buttonSize = new Dimension(200, 40);
        transferButton.setPreferredSize(buttonSize);
        opButton.setPreferredSize(buttonSize);
        refreshButton.setPreferredSize(buttonSize);
        logoutButton.setPreferredSize(buttonSize);
        
        buttonPanel.add(transferButton);
        buttonPanel.add(opButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(logoutButton);
        
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);
        
        // Acciones de botones
        transferButton.addActionListener(e -> mainApp.mostrarVistaTransferencia(idCuenta));
        
        opButton.addActionListener(e -> mainApp.mostrarVistaOperacion(idCuenta));
        
        refreshButton.addActionListener(e -> cargarSaldo());
        
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                mainApp.getMainFrame(),
                "¿Está seguro que desea cerrar sesión?",
                "Confirmar Cierre de Sesión",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                mainApp.mostrarLoginView();
            }
        });
    }
    
    /**
     * Carga y actualiza el saldo de la cuenta.
     */
    private void cargarSaldo() {
        try {
            double saldo = mainApp.getRmiConnector().getBancoStub()
                .consultarSaldo(idCuenta);
            saldoLabel.setText(String.format("Saldo Actual: $%.2f", saldo));
        } catch (Exception ex) {
            saldoLabel.setText("Saldo Actual: Error al cargar");
            JOptionPane.showMessageDialog(mainApp.getMainFrame(),
                "Error al consultar saldo: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
