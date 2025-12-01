package cine;

import java.io.*;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 * GestorNotificaciones
 * Crea, lee y actualiza archivos de notificación para órdenes de dulcería.
 *
 * Archivos:
 * ArchivosAplicacion/notificaciones/<claveOrden>.txt
 *
 * Contenido inicial: "Estamos trabajando arduamente ... (leer espera)"
 * Contenido final (sobrescribe): "Hola, soy <NombreDelVendedor>. Ya está lista tu orden ... AAAAMMDD:hhmm"
 */
public class GestorNotificaciones {

    private static final Path DIR_NOTIF = Paths.get("ArchivosAplicacion", "notificaciones");
    private static final DateTimeFormatter TF = DateTimeFormatter.ofPattern("yyyyMMdd:HHmm");

    static {
        try { if (Files.notExists(DIR_NOTIF)) Files.createDirectories(DIR_NOTIF); }
        catch (IOException e) { System.err.println("No se pudo crear carpeta de notificaciones: " + e.getMessage()); }
    }

    /**
     * Crea el archivo inicial de notificación con la leyenda de espera.
     */
    public static void crearArchivoInicial(OrdenDulceria orden) {
        String clave = orden.getClaveCompra();
        Path file = DIR_NOTIF.resolve(clave + ".txt");
        String contenido = "Estamos trabajando arduamente para que tus alimentos sean deliciosos. Por favor, espera un poco más =D";
        try (BufferedWriter bw = Files.newBufferedWriter(file, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            bw.write(contenido);
        } catch (IOException e) {
            System.err.println("Error creando notificación inicial: " + e.getMessage());
        }
    }

    /**
     * Sobrescribe el archivo de notificación indicando entrega lista, con nombre del vendedor y hora.
     */
    public static void actualizarNotificacionFinal(OrdenDulceria orden, VendedorDulceria vendedor, LocalDateTime whenFinished) {
        String clave = orden.getClaveCompra();
        Path file = DIR_NOTIF.resolve(clave + ".txt");
        String timestamp = whenFinished.format(TF);
        String contenido = String.format("Hola, soy %s. Ya está lista tu orden de dulcería. Puedes pasar a recogerla en la fila de dulcería para ventas de la app. %s",
                                         vendedor.getNombre(), timestamp);
        try (BufferedWriter bw = Files.newBufferedWriter(file, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            bw.write(contenido);
        } catch (IOException e) {
            System.err.println("Error actualizando notificación final: " + e.getMessage());
        }
    }

    /**
     * Lee el archivo de notificación y devuelve su contenido. Si no existe devuelve null.
     */
    public static String leerNotificacion(String claveCompra) {
        Path file = DIR_NOTIF.resolve(claveCompra + ".txt");
        if (Files.exists(file)) {
            try {
                return new String(Files.readAllBytes(file));
            } catch (IOException e) {
                System.err.println("Error leyendo notificación: " + e.getMessage());
            }
        }
        return null;
    }
}
