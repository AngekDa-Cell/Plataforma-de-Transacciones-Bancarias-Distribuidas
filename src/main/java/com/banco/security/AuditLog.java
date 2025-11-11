package com.banco.security;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Log de auditoría encadenado con SHA-256. Cada línea incluye el hash del
 * contenido anterior para evitar manipulaciones.
 */
public class AuditLog {
    private static final File LOG_FILE = new File("audit.log");
    private static String lastHash = loadLastHash();

    private static synchronized String loadLastHash() {
        if (!LOG_FILE.exists()) return "";
        try (RandomAccessFile raf = new RandomAccessFile(LOG_FILE, "r")) {
            long len = raf.length();
            if (len == 0) return "";
            // Leer última línea
            long pos = len - 1;
            while (pos > 0) {
                raf.seek(pos--);
                if (raf.read() == '\n') break;
            }
            String line = raf.readLine();
            if (line == null) return "";
            int idx = line.lastIndexOf(" hash=");
            if (idx >= 0) {
                return line.substring(idx + 6).trim();
            }
        } catch (Exception ignored) {}
        return "";
    }

    public static synchronized void append(String oper, String idCuenta, String canonical, boolean ok, byte[] firma, String certSubject) {
        append(oper, idCuenta, canonical, ok, firma, certSubject, null);
    }

    public static synchronized void append(String oper, String idCuenta, String canonical, boolean ok, byte[] firma, String certSubject, String reason) {
        try {
            String ts = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date());
            String base = String.format("ts=%s oper=%s id=%s ok=%s msg=%s sig=%s subj=%s prev=%s%s",
                    ts, oper, idCuenta, ok, canonical.replace('\n', ' ').replace('\r', ' '),
                    SecurityUtil.b64(firma),
                    certSubject == null ? "" : certSubject.replace(' ', '_'),
                    lastHash,
                    reason == null ? "" : (" reason=" + reason.replace(' ', '_')));
            String newHash = SecurityUtil.hex(SecurityUtil.sha256(base.getBytes(StandardCharsets.UTF_8)));
            String line = base + " hash=" + newHash + System.lineSeparator();
            try (FileOutputStream fos = new FileOutputStream(LOG_FILE, true)) {
                fos.write(line.getBytes(StandardCharsets.UTF_8));
            }
            lastHash = newHash;
        } catch (Exception e) {
            System.err.println("[AUDIT] Error escribiendo log: " + e.getMessage());
        }
    }
}
