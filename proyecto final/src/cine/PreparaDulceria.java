package cine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Hilo que simula la preparación de una orden de dulcería.
 * Flujo:
 *  1) Espera 20-40s (en cola)
 *  2) Selecciona vendedor aleatorio
 *  3) Espera 20-30s (antes de empezar)
 *  4) Espera 10-15s (preparación)
 *  5) Actualiza historial del vendedor y notificación del cliente
 */
public class PreparaDulceria extends Thread {

    private final OrdenDulceria orden;
    private final Random rnd = new Random();
    private static final DateTimeFormatter TF = DateTimeFormatter.ofPattern("yyyyMMdd:HHmm");

    public PreparaDulceria(OrdenDulceria orden) {
        this.orden = orden;
    }

    @Override
    public void run() {
        try {
            // 1) En cola 20-40s
            int pausa1 = 20 + rnd.nextInt(21); // 20..40
            Thread.sleep(pausa1 * 1000L);

            // 2) Seleccionar vendedor
            VendedorDulceria vendedor = seleccionarVendedor();

            LocalDateTime asignacionTime = LocalDateTime.now();
            String tipoOrden = orden.getTipoOrden();
            String lineaAsignacion = String.format("Generada: %s | Tipo: %s | Asignada: %s",
                    orden.getFechaCompra().format(TF), tipoOrden, asignacionTime.format(TF));

            if (vendedor != null) {
                // Guardar registro en archivo historial del vendedor
                GestorArchivos.guardarHistorialVendedor(vendedor.getNickname(), lineaAsignacion);
            }

            // 3) Espera 20-30s antes de empezar a preparar
            int pausa2 = 20 + rnd.nextInt(11); // 20..30
            Thread.sleep(pausa2 * 1000L);

            LocalDateTime inicioPreparacion = LocalDateTime.now();
            if (vendedor != null) {
                GestorArchivos.guardarHistorialVendedor(vendedor.getNickname(),
                        "InicioPreparacion: " + inicioPreparacion.format(TF));
            }

            // 4) Preparación 10-15s
            int pausa3 = 10 + rnd.nextInt(6); // 10..15
            Thread.sleep(pausa3 * 1000L);

            LocalDateTime fin = LocalDateTime.now();
            if (vendedor != null) {
                GestorArchivos.guardarHistorialVendedor(vendedor.getNickname(),
                        "Terminado: " + fin.format(TF));
            }

            // 5) Actualizar notificación final (sobrescribe)
            String mensaje = "Tu orden " + (orden.getClaveCompra().isEmpty() ? orden.generarClaveAutomatica() : orden.getClaveCompra())
                    + " está lista para recoger.\nAtendida por: " + (vendedor != null ? vendedor.getNombre() : "sin asignar")
                    + "\nHora: " + fin.format(TF);

            GestorNotificaciones.actualizarNotificacionFinal((orden.getClaveCompra().isEmpty() ? orden.generarClaveAutomatica() : orden.getClaveCompra()),mensaje);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("PreparaDulceria interrumpido.");
        } catch (Exception ex) {
            System.err.println("Error en PreparaDulceria: " + ex.getMessage());
        }
    }

    /**
     * Selecciona un vendedor aleatoriamente leyendo empleados serializados.
     * Usa GestorArchivos para cargar empleados.
     */
    private VendedorDulceria seleccionarVendedor() {
        try {
            List<Empleado> empleados = GestorArchivos.cargarEmpleados("ArchivosAplicacion/empleados.dat");
            if (empleados == null || empleados.isEmpty()) return null;
            List<VendedorDulceria> vendedores = new ArrayList<>();
            for (Empleado e : empleados) {
                if (e instanceof VendedorDulceria) vendedores.add((VendedorDulceria) e);
            }
            if (vendedores.isEmpty()) return null;
            return vendedores.get(rnd.nextInt(vendedores.size()));
        } catch (Exception e) {
            System.err.println("No se pudieron cargar empleados para asignación: " + e.getMessage());
            return null;
        }
    }
}

