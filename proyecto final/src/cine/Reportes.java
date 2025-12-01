package cine;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase de Reportes administrativos.
 */
public class Reportes {

    /** Instancia central del sistema */
    private static final SistemaAutenticacion sistema = SistemaAutenticacion.getInstancia();

    /* ============================================================
     *  1. BUSCAR CLIENTE POR NICKNAME
     * ============================================================ */
    public static List<Cliente> buscarClientePorNickname(String criterio) {

        List<Cliente> encontrados = new ArrayList<>();

        for (Cliente c : sistema.getClientes()) {
            if (c.getNickname().toLowerCase().contains(criterio.toLowerCase())) {
                encontrados.add(c);
            }
        }

        if (encontrados.isEmpty()) {
            System.out.println("\nNo se encontraron clientes.");
        } else {
            System.out.println("\n=== COINCIDENCIAS ===");
            for (int i = 0; i < encontrados.size(); i++) {
                Cliente c = encontrados.get(i);
                System.out.println((i + 1) + ". " + c.getNickname() +
                        " | " + c.getNombreCompleto());
            }
        }

        return encontrados;
    }

    /* ============================================================
     *  2. MOSTRAR HISTORIAL DE COMPRAS
     * ============================================================ */
    public static void mostrarPeliculasCompradas(Cliente cliente) {

        if (cliente == null) {
            System.out.println("Cliente inválido.");
            return;
        }

        if (cliente.getHistorialCompras().isEmpty()) {
            System.out.println("\nEl cliente no ha realizado compras.");
            return;
        }

        System.out.println("\n=== HISTORIAL DE COMPRAS DE " +
                cliente.getNickname().toUpperCase() + " ===");
        System.out.println("Nombre: " + cliente.getNombreCompleto());
        System.out.println("----------------------------------");

        for (CompraBoletos compra : cliente.getHistorialCompras()) {

            FuncionDeCine funcion = compra.getFuncion();

            System.out.println("Película: " + funcion.getPelicula().getNombre());
            System.out.println("Sala: " + funcion.getSala().getIdSala());
            System.out.println("Fecha y hora: " + funcion.getFechaHora());
            System.out.println("Boletos: " + compra.getBoletos().size());

            System.out.print("Asientos: ");
            for (Boleto b : compra.getBoletos()) {
                System.out.print(b.getAsientoAsignado() + " ");
            }

            System.out.println("\n----------------------------------");
        }
    }

    /* ============================================================
     *  3. HISTORIAL DE VENDEDOR
     * ============================================================ */
    public static void mostrarHistorialVendedor(String nickname) {
        System.out.println("\n=== HISTORIAL DEL VENDEDOR " +
                nickname.toUpperCase() + " ===");

        String contenido = GestorArchivos.leerNotificacion(nickname);
        System.out.println(contenido);
    }
}
