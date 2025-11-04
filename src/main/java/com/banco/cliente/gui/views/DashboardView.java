package com.banco.cliente.gui.views;

import com.banco.cliente.gui.ClienteBancarioGUI;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Vista de Dashboard implementada con Swing (diseño mejorado).
 */
public class DashboardView extends JPanel {

    private ClienteBancarioGUI mainApp;
    private String idCuenta;
    private JLabel saldoLabel;
    private JLabel saldoValorLabel;

    public DashboardView(ClienteBancarioGUI mainApp, String idCuenta) {
        this.mainApp = mainApp;
        this.idCuenta = idCuenta;
        
        // Configurar el panel con layout BorderLayout
        setLayout(new BorderLayout(0, 15));
        setBackground(new Color(240, 248, 255)); // AliceBlue
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Panel superior - Cabecera
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180)); // SteelBlue
        headerPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        
        // Título de bienvenida
        JLabel welcomeLabel = new JLabel("👤 Bienvenido, " + idCuenta);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Panel central con GridBagLayout
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Panel de saldo (tarjeta)
        JPanel saldoPanel = new JPanel(new GridBagLayout());
        saldoPanel.setBackground(Color.WHITE);
        saldoPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(70, 130, 180), 2, true),
            new EmptyBorder(20, 30, 20, 30)
        ));
        
        GridBagConstraints gbcSaldo = new GridBagConstraints();
        gbcSaldo.gridx = 0;
        gbcSaldo.gridy = 0;
        gbcSaldo.insets = new Insets(5, 0, 5, 0);
        
        saldoLabel = new JLabel("💰 Saldo Disponible");
        saldoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        saldoLabel.setForeground(Color.GRAY);
        saldoPanel.add(saldoLabel, gbcSaldo);
        
        gbcSaldo.gridy = 1;
        saldoValorLabel = new JLabel("Cargando...");
        saldoValorLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        saldoValorLabel.setForeground(new Color(34, 139, 34)); // ForestGreen
        saldoPanel.add(saldoValorLabel, gbcSaldo);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        centerPanel.add(saldoPanel, gbc);
        
        // Cargar saldo inicial
        cargarSaldo();
        
        // Panel de botones principales
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        JButton transferBtn = crearBotonPrincipal("💸 Transferencia", 
            new Color(70, 130, 180), 
            e -> mainApp.mostrarVistaTransferencia(idCuenta));
        centerPanel.add(transferBtn, gbc);
        
        gbc.gridx = 1;
        JButton opBtn = crearBotonPrincipal("💵 Operaciones", 
            new Color(30, 144, 255), 
            e -> mainApp.mostrarVistaOperacion(idCuenta));
        centerPanel.add(opBtn, gbc);
        
        // Botones secundarios
        gbc.gridy = 2;
        gbc.gridx = 0;
        JButton refreshBtn = crearBotonSecundario("🔄 Actualizar Saldo", 
            e -> cargarSaldo());
        centerPanel.add(refreshBtn, gbc);
        
        gbc.gridx = 1;
        JButton logoutBtn = crearBotonSecundario("🚪 Cerrar Sesión", 
            e -> cerrarSesion());
        centerPanel.add(logoutBtn, gbc);
        
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private JButton crearBotonPrincipal(String texto, Color color, java.awt.event.ActionListener action) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(220, 60));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);
        return btn;
    }
    
    private JButton crearBotonSecundario(String texto, java.awt.event.ActionListener action) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(220, 45));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(70, 130, 180));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(70, 130, 180), 2, true),
            new EmptyBorder(10, 15, 10, 15)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);
        return btn;
    }
    
    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(
            mainApp.getMainFrame(),
            "¿Está seguro que desea cerrar sesión?",
            "Confirmar Cierre de Sesión",
            JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            mainApp.mostrarLoginView();
        }
    }
    
    /**
     * Carga y actualiza el saldo de la cuenta.
     */
    private void cargarSaldo() {
        try {
            double saldo = mainApp.getRmiConnector().getBancoStub()
                .consultarSaldo(idCuenta);
            saldoValorLabel.setText(String.format("$%.2f", saldo));
        } catch (Exception ex) {
            saldoValorLabel.setText("Error");
            saldoValorLabel.setForeground(new Color(220, 20, 60)); // Crimson
            JOptionPane.showMessageDialog(mainApp.getMainFrame(),
                "Error al consultar saldo: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
