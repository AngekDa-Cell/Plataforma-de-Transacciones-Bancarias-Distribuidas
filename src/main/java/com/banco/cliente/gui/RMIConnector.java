package com.banco.cliente.gui;

import com.banco.servidor.BancoServidor;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import com.banco.security.SecurityUtil;

/**
 * Clase de utilidad (helper) para gestionar la conexión RMI.
 */
public class RMIConnector {

    private BancoServidor bancoStub;
    private KeyStore keyStore;

    /**
     * Intenta conectar con el servidor RMI.
     * @return true si la conexión es exitosa, false en caso contrario.
     */
    public boolean conectar() {
        try {
            // Conecta al registro RMI en localhost, puerto 1099
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            // Busca el objeto remoto "BancoServidor"
            bancoStub = (BancoServidor) registry.lookup("BancoServidor");
            return true;
        } catch (Exception e) {
            System.err.println("Error al conectar RMI: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Devuelve el objeto stub RMI para hacer las llamadas remotas.
     * @return El stub de BancoServidor, o null si no está conectado.
     */
    public BancoServidor getBancoStub() {
        return bancoStub;
    }

    public boolean cargarKeyStoreCliente(String path, String password) {
        try {
            keyStore = SecurityUtil.loadKeyStore(path, password);
            if (keyStore == null) return false;
            return true;
        } catch (Exception e) {
            System.err.println("Error cargando keystore cliente: " + e.getMessage());
            return false;
        }
    }

    public PrivateKey obtenerPrivateKey(String alias, String keyPassword) {
        try {
            return SecurityUtil.getPrivateKey(keyStore, alias, keyPassword);
        } catch (Exception e) {
            System.err.println("No se pudo obtener la clave privada: " + e.getMessage());
            return null;
        }
    }

    public X509Certificate obtenerCertificado(String alias) {
        try {
            return SecurityUtil.getCertificate(keyStore, alias);
        } catch (Exception e) {
            System.err.println("No se pudo obtener el certificado: " + e.getMessage());
            return null;
        }
    }
}
