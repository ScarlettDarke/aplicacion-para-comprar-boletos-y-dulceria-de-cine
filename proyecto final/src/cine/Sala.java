package cine;

/**
 * Representa una sala física del cine y su configuración de asientos.
 *
 * <p>Esta clase solo define la estructura (layout) de la sala:
 * cuántas filas tiene, cuántos asientos por fila y el código de cada asiento
 * (A1, A2, ..., B1, etc). </p>
 *
 * <p>La ocupación de los asientos NO se maneja aquí, sino en la clase
 * {@link FuncionDeCine}, porque la disponibilidad cambia por función.</p>
 *
 * <p>Tipos de sala admitidos:</p>
 * <ul>
 *   <li><b>A:</b> 10 filas (A–J), 15 asientos cada una.</li>
 *   <li><b>B:</b> Filas A–D tienen 7 asientos. Filas E–J tienen 15.</li>
 *   <li><b>VIP:</b> 8 filas (A–H), 6 asientos cada una. Contiene pasillos lógicos.</li>
 * </ul>
 */
public class Sala {

    /** Identificador único de la sala, ej. "Sala A", "Sala B", "VIP 1". */
    private final String idSala;

    /** Tipo de sala: "A", "B", "VIP". */
    private final String tipo;

    /** Matriz (posiblemente irregular) con etiquetas de asientos. */
    private final String[][] asientos;

    /** Capacidad total de la sala. */
    private final int capacidadTotal;

    /**
     * Construye una sala con un layout predefinido.
     *
     * @param idSala nombre identificador de la sala.
     * @param tipo tipo de layout (A, B o VIP).
     */
    public Sala(String idSala, String tipo) {
        this.idSala = idSala;
        this.tipo = tipo.toUpperCase();

        switch (this.tipo) {
            case "A":
                // 10 filas, 15 columnas
                asientos = new String[10][15];
                break;

            case "B":
                // 10 filas: A–D = 7 asientos, E–J = 15 asientos
                asientos = new String[10][];
                for (int i = 0; i < 4; i++) {
                    asientos[i] = new String[7];
                }
                for (int i = 4; i < 10; i++) {
                    asientos[i] = new String[15];
                }
                break;

            case "VIP":
                // 8 filas, 6 asientos cada una
                asientos = new String[8][6];
                break;

            default:
                throw new IllegalArgumentException("Tipo de sala desconocido: " + tipo);
        }

        // Llenar etiquetas y contar capacidad
        int capacidad = 0;
        char letra = 'A';

        for (int i = 0; i < asientos.length; i++) {
            for (int j = 0; j < asientos[i].length; j++) {
                asientos[i][j] = letra + String.valueOf(j + 1);
                capacidad++;
            }
            letra++;
        }

        this.capacidadTotal = capacidad;
    }

    /* ==========================================================
     * GETTERS
     * ========================================================== */

    /** @return identificador de la sala. */
    public String getIdSala() {
        return idSala;
    }

    /** @return tipo A, B o VIP. */
    public String getTipo() {
        return tipo;
    }

    /** @return capacidad total de asientos. */
    public int getCapacidad() {
        return capacidadTotal;
    }

    /**
     * Devuelve la matriz de etiquetas de asientos.
     * No modifica disponibilidad, solo devuelve estructura.
     */
    public String[][] getAsientos() {
        return asientos;
    }

    /* ==========================================================
     * UTILIDADES PARA MANEJO DE ASIENTOS
     * ========================================================== */

    /**
     * Convierte un código como "C7" en índices de matriz.
     *
     * @param codigo asiento (C7, A1, H3, etc.)
     * @return arreglo {fila, columna}
     */
    public int[] indicesDesdeCodigo(String codigo) {
        if (codigo == null || codigo.length() < 2)
            throw new IllegalArgumentException("Código de asiento inválido: " + codigo);

        codigo = codigo.trim().toUpperCase();

        char letraFila = codigo.charAt(0);
        int fila = letraFila - 'A';

        String numeroStr = codigo.substring(1);
        if (!numeroStr.matches("\\d+"))
            throw new IllegalArgumentException("Número de asiento inválido: " + codigo);

        int col = Integer.parseInt(numeroStr) - 1;

        return new int[]{fila, col};
    }

    /**
     * Verifica si un asiento existe dentro del layout.
     */
    public boolean asientoExiste(String codigo) {
        try {
            int[] pos = indicesDesdeCodigo(codigo);
            int f = pos[0], c = pos[1];
            return f >= 0 && f < asientos.length && c >= 0 && c < asientos[f].length;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Representación del layout de la sala (solo etiquetas).
     * La ocupación se agrega en FuncionDeCine.visualizarEstadoAsientos().
     */
    public String representarAsientosEtiquetas() {
        StringBuilder sb = new StringBuilder();

        for (String[] fila : asientos) {
            for (String asiento : fila) {
                sb.append(String.format("%-4s", asiento));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return idSala + " (Tipo " + tipo + ", Capacidad: " + capacidadTotal + ")";
    }
}
