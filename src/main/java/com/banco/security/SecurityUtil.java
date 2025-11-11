package com.banco.security;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Objects;

public class SecurityUtil {

    public static KeyStore loadKeyStore(String path, String password) throws Exception {
        if (path == null || password == null) {
            return null; // Permitir nulo si no está configurado explícitamente
        }
        KeyStore ks = KeyStore.getInstance(getTypeFromExtension(path));
        try (FileInputStream fis = new FileInputStream(path)) {
            ks.load(fis, password.toCharArray());
        }
        return ks;
    }

    private static String getTypeFromExtension(String path) {
        String lower = path.toLowerCase();
        if (lower.endsWith(".p12") || lower.endsWith(".pfx")) return "PKCS12";
        return "JKS";
    }

    public static PrivateKey getPrivateKey(KeyStore ks, String alias, String keyPassword) throws Exception {
        Objects.requireNonNull(ks, "KeyStore nulo");
        Key key = ks.getKey(alias, keyPassword.toCharArray());
        if (!(key instanceof PrivateKey)) {
            throw new KeyStoreException("Alias no corresponde a una clave privada: " + alias);
        }
        return (PrivateKey) key;
    }

    public static X509Certificate getCertificate(KeyStore ks, String alias) throws Exception {
        Objects.requireNonNull(ks, "KeyStore nulo");
        Certificate cert = ks.getCertificate(alias);
        if (cert == null) throw new KeyStoreException("Certificado no encontrado para alias: " + alias);
        return (X509Certificate) cert;
    }

    public static byte[] sign(byte[] data, PrivateKey privateKey) throws Exception {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(privateKey);
        sig.update(data);
        return sig.sign();
    }

    public static boolean verify(byte[] data, byte[] signature, X509Certificate cert) throws Exception {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(cert.getPublicKey());
        sig.update(data);
        return sig.verify(signature);
    }

    public static X509Certificate readCertificateFromDer(byte[] der) throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        try (ByteArrayInputStream bais = new ByteArrayInputStream(der)) {
            return (X509Certificate) cf.generateCertificate(bais);
        }
    }

    public static X509Certificate readCertificateFromFile(String path) throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        try (FileInputStream fis = new FileInputStream(path)) {
            return (X509Certificate) cf.generateCertificate(fis);
        }
    }

    public static boolean isCertificateInTrustStore(X509Certificate cert, KeyStore trustStore) throws Exception {
        if (trustStore == null) return false;
        String alias = trustStore.getCertificateAlias(cert);
        if (alias != null) return true;
        // Fallback: comparar contra todas las entradas
        var en = trustStore.aliases();
        while (en.hasMoreElements()) {
            String a = en.nextElement();
            Certificate c = trustStore.getCertificate(a);
            if (c instanceof X509Certificate) {
                if (MessageDigest.isEqual(c.getEncoded(), cert.getEncoded())) return true;
            }
        }
        return false;
    }

    public static byte[] sha256(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(data);
    }

    public static String hex(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            sb.append(Character.forDigit((b >> 4) & 0xF, 16));
            sb.append(Character.forDigit((b & 0xF), 16));
        }
        return sb.toString();
    }

    public static String b64(byte[] data) {
        return java.util.Base64.getEncoder().encodeToString(data);
    }

    public static byte[] utf8(String s) {
        return s.getBytes(StandardCharsets.UTF_8);
    }
}
