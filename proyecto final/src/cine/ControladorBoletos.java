package cine;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador responsable del flujo de compra de boletos.
 * <p>
 * - Lista películas y funciones (usando Cartelera)
 * - Muestra disposición física (libres/ocupados)
 * - Solicita al usuario los asientos en formato "H7 H8 H9"
 * - Valida disponibilidad; en caso de error vuelve a pedir
 * - Si están disponibles, inicia dos hilos concurrentes:
 *     * Hilo de transacción bancaria (mensajes con pausas de 2-5s)
 *     * Hilo de barra de progreso (imprime cada 0.5s)
 *   Espera a que termine la transacción bancaria, mantiene la barra 3s más y luego muestra resumen.
 * - Genera boletos (uno por asiento) usando {@link FuncionDeCine#venderAsientosPorCodigos(List, String)}
 * </p>
 */
public class ControladorBoletos {

    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter FECHA_MOSTRAR = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter HORA_MOSTRAR  = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Flujo principal para que un cliente compre boletos.
     *
     * @param cliente cliente que compra (se usará su tipo si hace falta)
     */
    public static void comprarBoletos(Cliente cliente) {

        if (Cartelera.peliculas.isEmpty()) {
            System.out.println("No hay películas en cartelera.");
            return;
        }

        System.out.println("\n=== COMPRA DE BOLETOS ===");

        // 1) Seleccionar película
        Pelicula peli = seleccionarPelicula();
        if (peli == null) return;

        // 2) Seleccionar función de esa película
        FuncionDeCine funcion = seleccionarFuncion(peli);
        if (funcion == null) return;

        // 3) Mostrar disposición
        System.out.println("\nDisposición de sala (XX = ocupado):");
        System.out.println(funcion.visualizarEstadoAsientos());
        System.out.println("Disponibles: " + funcion.getDisponibles());

        // 4) Pedir asientos (hasta que validen)
        List<String> codigos = null;
        while (true) {
            System.out.println("\nEscribe los asientos que deseas separados por espacios (ej. H7 H8 H9):");
            String linea = sc.nextLine().trim();
            if (linea.isEmpty()) {
                System.out.println("Entrada vacía. Cancelando compra.");
                return;
            }
            String[] parts = linea.split("\\s+");
            // Normalizar (mayúsculas, sin comas)
            List<String> candidatos = Arrays.stream(parts)
                    .map(s -> s.replaceAll(",", "").toUpperCase())
                    .collect(Collectors.toList());

            // Validaciones: formato, existencia y disponibilidad
            boolean todoBien = true;
            for (String c : candidatos) {
                if (!funcion.asientoDisponible(c)) {
                    System.out.println("Asiento inválido o no disponible: " + c);
                    todoBien = false;
                }
            }
            if (todoBien) {
                codigos = candidatos;
                break;
            } else {
                System.out.println("Alguno(s) de los asientos no están disponibles. Intenta de nuevo.");
            }
        }

        // 5) Iniciar hilos concurrentes: pago y barra
        final List<String> finalCodigos = codigos;
        final String tipoCliente = "Adulto"; // si quieres, puedes obtenerlo de cliente.getTipo() si existe

        // Preparar tarea de venta (se realizará después de confirmar pago)
        List<Boleto> boletosGenerados = new ArrayList<>();

        // Hilo que simula la transacción bancaria
        Thread hiloPago = new Thread(() -> {
            try {
                Random rnd = new Random();
                System.out.println("\n[Pago] estableciendo conexión con el banco...");
                Thread.sleep((2000 + rnd.nextInt(3000))); // 2-5s
                System.out.println("[Pago] haciendo el cargo correspondiente...");
                Thread.sleep((2000 + rnd.nextInt(3000))); // 2-5s
                System.out.println("[Pago] transacción finalizada.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("[Pago] Interrumpido.");
            }
        });

        // Hilo que imprime barra de progreso cada 0.5s hasta que hiloPago termine
        Thread hiloBarra = new Thread(() -> {
            try {
                final char[] anim = {'|','/','-','\\'};
                int idx = 0;
                while (hiloPago.isAlive()) {
                    System.out.print("\rProcesando " + anim[idx % anim.length]);
                    idx++;
                    Thread.sleep(500);
                }
                // Mantener 3 segundos adicionales la pantalla de progreso
                long end = System.currentTimeMillis() + 3000;
                while (System.currentTimeMillis() < end) {
                    System.out.print("\rListo.      ");
                    Thread.sleep(500);
                }
                System.out.println(); // nueva línea al finalizar
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Iniciar ambos
        hiloPago.start();
        hiloBarra.start();

        // Esperar a que termine la transacción bancaria
        try {
            hiloPago.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Compra interrumpida.");
            return;
        }

        // 6) Con el pago finalizado, vendemos los asientos (llamamos a funcion)
        try {
            boletosGenerados.addAll(funcion.venderAsientosPorCodigos(finalCodigos, tipoCliente));
        } catch (IllegalArgumentException ex) {
            System.out.println("No fue posible reservar los asientos: " + ex.getMessage());
            return;
        }

        // Asegurar que hiloBarra termine antes de continuar
        try {
            hiloBarra.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 7) Mostrar resumen con claves
        System.out.println("\n=== RESUMEN DE COMPRA ===");
        System.out.println("Título: " + peli.getNombre());
        System.out.println("Fecha: " + funcion.getFecha().format(FECHA_MOSTRAR));
        System.out.println("Hora:  " + funcion.getHora().format(HORA_MOSTRAR));
        System.out.println("Asientos: " + String.join(", ", finalCodigos));

        double total = 0;
        for (Boleto b : boletosGenerados) {
            total += b.getPrecio();
        }
        System.out.printf("Precio total: $%.2f%n", total);

        // Generar y mostrar claves: ya generadas en la función (si quieres mostrarlas, recreamos formato)
        for (Boleto b : boletosGenerados) {
            // clave se construye igual que en FuncionDeCine.generarClavePorAsiento
            String clave = funcion.getIdFuncion() + ":" + b.getAsientoAsignado();
            System.out.println("Clave boleto: " + clave);
        }

        System.out.println("\n¡Compra finalizada! Gracias por su compra.");
    }

    /* ------------------ Métodos auxiliares ------------------ */

    private static Pelicula seleccionarPelicula() {
        System.out.println("\nPELÍCULAS DISPONIBLES:");
        for (int i = 0; i < Cartelera.peliculas.size(); i++) {
            System.out.println((i + 1) + ". " + Cartelera.peliculas.get(i).getNombre());
        }
        System.out.print("Seleccione una película: ");
        int op;
        try {
            op = Integer.parseInt(sc.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Selección inválida.");
            return null;
        }
        if (op < 1 || op > Cartelera.peliculas.size()) {
            System.out.println("Selección inválida.");
            return null;
        }
        return Cartelera.peliculas.get(op - 1);
    }

    private static FuncionDeCine seleccionarFuncion(Pelicula peli) {
        List<FuncionDeCine> funciones = Cartelera.funciones;
        List<FuncionDeCine> funcionesPeli = new ArrayList<>();
        for (FuncionDeCine f : funciones) {
            if (f.getPelicula().getNombre().equalsIgnoreCase(peli.getNombre())) {
                funcionesPeli.add(f);
            }
        }
        if (funcionesPeli.isEmpty()) {
            System.out.println("No hay funciones programadas para esa película.");
            return null;
        }
        System.out.println("\nFUNCIONES DISPONIBLES:");
        for (int i = 0; i < funcionesPeli.size(); i++) {
            FuncionDeCine f = funcionesPeli.get(i);
            System.out.println((i + 1) + ". Sala " + f.getSala().getIdSala() + " | Fecha: " + f.getFecha().format(FECHA_MOSTRAR)
                    + " | Hora: " + f.getHora().format(HORA_MOSTRAR));
        }
        System.out.print("Seleccione función: ");
        int op;
        try {
            op = Integer.parseInt(sc.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Selección inválida.");
            return null;
        }
        if (op < 1 || op > funcionesPeli.size()) {
            System.out.println("Selección inválida.");
            return null;
        }
        return funcionesPeli.get(op - 1);
    }
}
