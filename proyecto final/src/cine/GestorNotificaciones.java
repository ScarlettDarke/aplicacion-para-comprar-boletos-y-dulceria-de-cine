package cine;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Gestor de notificaciones para órdenes de dulcería.
 * Archivos en: ArchivosAplicacion/notificaciones/<clave>.txt
 */
public class GestorNotificaciones {

    private static final Path DIR_NOTIF = Paths.get("ArchivosAplicacion", "notificaciones");
    private static final DateTimeFormatter TF = DateTimeFormatter.ofPattern("yyyyMMdd:HHmm");

    static {
        try {
            if (Files.notExists(DIR_NOTIF)) Files.createDirectories(DIR_NOTIF);
        } catch (IOException e) {
            System.err.println("No se pudo crear carpeta de notificaciones: " + e.getMessage());
        }
    }

    /**
     * Crea la notificación inicial (estado en espera) para una orden.
     * Si ya existe, la sobreescribe.
     */
    public static void crearNotificacionInicial(String claveCompra, String textoInicial) {
        Path file = DIR_NOTIF.resolve(claveCompra + ".txt");
        try (BufferedWriter bw = Files.newBufferedWriter(file)) {
            bw.write(textoInicial);
        } catch (IOException e) {
            System.err.println("Error creando notificación: " + e.getMessage());
        }
    }

    /**
     * Actualiza (sobrescribe) la notificación final indicando vendedor y hora.
     */
    public static void actualizarNotificacionFinal(String claveCompra, String textoFinal) {
        crearNotificacionInicial(claveCompra, textoFinal);
    }

    /**
     * Lee la notificación y devuelve su contenido. Si no existe retorna mensaje por defecto.
     */
    public static String leerNotificacion(String claveCompra) {
        Path file = DIR_NOTIF.resolve(claveCompra + ".txt");
        if (Files.exists(file)) {
            try {
                return new String(Files.readAllBytes(file));
            } catch (IOException e) {
                return "Error leyendo notificación: " + e.getMessage();
            }
        }
        return "No hay notificaciones para la clave " + claveCompra;
    }
}
