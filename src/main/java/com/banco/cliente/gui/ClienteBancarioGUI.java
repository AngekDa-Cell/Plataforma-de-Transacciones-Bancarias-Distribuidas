package com.banco.cliente.gui;

import com.banco.cliente.gui.views.*;
import javax.swing.*;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
 * Clase principal de la aplicación Swing.
 * Gestiona el JFrame principal y la navegación entre vistas (JPanels).
 */
public class ClienteBancarioGUI {

    private JFrame mainFrame;
    private RMIConnector rmiConnector;
    private PrivateKey privateKey;
    private X509Certificate certificate;
    private String alias;
    private String idCuenta;

    public ClienteBancarioGUI() {
        // Configurar el Look and Feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.mainFrame = new JFrame("🏦 Banco Distribuido");
        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mainFrame.setSize(550, 450);
        this.mainFrame.setLocationRelativeTo(null); // Centrar en pantalla

        this.rmiConnector = new RMIConnector();
        
        // Iniciar con la vista de Login
        mostrarLoginView();
        
        this.mainFrame.setVisible(true);
    }

    /**
     * Muestra la vista de Login en el Frame principal.
     */
    public void mostrarLoginView() {
        LoginView loginView = new LoginView(this);
        cambiarVista(loginView, 720, 520);
    }

    /**
     * Muestra el Dashboard una vez que el login es exitoso.
     * @param idCuenta El ID de la cuenta del usuario logueado.
     */
    public void mostrarDashboardView(String idCuenta) {
        DashboardView dashboardView = new DashboardView(this, idCuenta);
        cambiarVista(dashboardView, 600, 500);
    }

    /**
     * Muestra la vista de operaciones (Depósito/Retiro).
     * @param idCuenta El ID de la cuenta del usuario.
     */
    public void mostrarVistaOperacion(String idCuenta) {
        OperationView operationView = new OperationView(this, idCuenta);
        cambiarVista(operationView, 600, 500);
    }

    /**
     * Muestra la vista de transferencias.
     * @param idCuentaOrigen El ID de la cuenta de origen.
     */
    public void mostrarVistaTransferencia(String idCuentaOrigen) {
        TransferView transferView = new TransferView(this, idCuentaOrigen);
        cambiarVista(transferView, 600, 500);
    }
    
    /**
     * Método helper para cambiar la vista actual en la ventana.
     */
    private void cambiarVista(JPanel nuevaVista, int width, int height) {
        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(nuevaVista);
        mainFrame.setSize(width, height);
        mainFrame.setLocationRelativeTo(null); // Re-centrar
        mainFrame.revalidate();
        mainFrame.repaint();
    }
    
    /**
     * Devuelve el conector RMI para que las vistas lo usen.
     */
    public RMIConnector getRmiConnector() {
        return rmiConnector;
    }

    /**
     * Devuelve el JFrame principal para operaciones de diálogo.
     */
    public JFrame getMainFrame() {
        return mainFrame;
    }

    public void setSecurityContext(PrivateKey pk, X509Certificate cert, String alias, String idCuenta) {
        this.privateKey = pk;
        this.certificate = cert;
        this.alias = alias;
        this.idCuenta = idCuenta;
    }

    public PrivateKey getPrivateKey() { return privateKey; }
    public X509Certificate getCertificate() { return certificate; }
    public String getAlias() { return alias; }
    public String getIdCuenta() { return idCuenta; }

    public static void main(String[] args) {
        // Ejecutar en el Event Dispatch Thread de Swing
        SwingUtilities.invokeLater(() -> new ClienteBancarioGUI());
    }
}
