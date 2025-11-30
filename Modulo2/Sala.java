/**
 * Representa una sala física del cine y su configuración de asientos.
 * <p>
 * Esta clase define la capacidad y la distribución espacial de las butacas.
 * Soporta configuraciones de asientos regulares (matrices rectangulares) e 
 * irregulares (jagged arrays) para salas con diseños asimétricos.
 * </p>
 */
public class Sala {
    /** Identificador único de la sala (ej. "Sala 1", "Sala Norte"). */
    private String idSala;
    /** * Categoría de la sala que define su layout.
     * Valores esperados: "A" (Estándar), "B" (Irregular), "VIP" (Exclusiva).
     */
    private String tipo;
    /** * Matriz de cadenas que representa visualmente los asientos.
     * <p>
     * Se almacena el nombre del asiento (ej. "A1", "B5"). 
     * Es una matriz irregular (jagged array) para soportar filas de diferente longitud.
     * </p>
     */
    private String[][] asientos; 
    /** Cantidad total de butacas disponibles en la sala. */
    private int capacidadTotal;
    /**
     * Constructor que inicializa la sala y construye su distribución de asientos.
     *
     * @param idSala Identificador de la sala.
     * @param tipo   Tipo de configuración ("A", "B", "VIP").
     * @throws IllegalArgumentException Si el tipo de sala proporcionado no está definido.
     */
    public Sala(String idSala, String tipo) {
        this.idSala = idSala;
        this.tipo = tipo;
        inicializarAsientos();
    }
    /**
     * Construye la estructura de datos (matriz) según el tipo de sala.
     * <p>
     * Lógica de dimensiones:
     * <ul>
     * <li><b>Tipo A:</b> Rectangular 10x15.</li>
     * <li><b>Tipo B:</b> Irregular. Filas 0-3 (7 asientos), Filas 4-9 (15 asientos).</li>
     * <li><b>Tipo VIP:</b> Rectangular pequeña 8x6.</li>
     * </ul>
     * </p>
     */
    private void inicializarAsientos() {
        switch (tipo) {
            case "A":
                // Filas A(0) a J(9) -> 10 filas.
                // 15 lugares cada una.
                asientos = new String[10][15];
                break;
            case "B":
                // Filas A a J -> 10 filas en total.
                asientos = new String[10][]; // Declaramos solo las filas primero
                // Filas A(0) a D(3) tienen 7 lugares (Jagged Array)
                for (int i = 0; i < 4; i++) {
                    asientos[i] = new String[7];
                }
                // Filas E(4) a J(9) tienen 15 lugares
                for (int i = 4; i < 10; i++) {
                    asientos[i] = new String[15];
                }
                break;
            case "VIP":
                // Filas A(0) a H(7) -> 8 filas.
                // 6 lugares cada una.
                asientos = new String[8][6];
                break;   
            default:
                throw new IllegalArgumentException("Tipo de sala no válido: " + tipo);
        }
        // Rellenamos con nombres de asientos (Ej: A1, A2...) y calculamos capacidad
        llenarMatrizNombres();
    }
    /**
     * Rellena la matriz con las etiquetas de los asientos y calcula la capacidad final.
     * <p>
     * Utiliza aritmética de caracteres ({@code char++}) para generar las letras de las filas
     * (A, B, C...) automáticamente.
     * </p>
     */
    private void llenarMatrizNombres() {
        capacidadTotal = 0;
        char filaChar = 'A';
        
        for (int i = 0; i < asientos.length; i++) {
            for (int j = 0; j < asientos[i].length; j++) {
                // Generamos "A1", "B5", etc.
                asientos[i][j] = String.valueOf(filaChar) + (j + 1);
                capacidadTotal++;
            }
            filaChar++;
        }
    }
    // Getters
    /** * Obtiene la matriz con los nombres de los asientos.
     * @return Un array bidimensional de Strings.
     */
    public String[][] getAsientos() { return asientos; }
    /** @return El tipo de configuración de la sala. */
    public String getTipo() { return tipo; }
    /** @return El identificador único de la sala. */
    public String getIdSala() { return idSala; }
    /** @return El número total de asientos disponibles. */
    public int getCapacidad() { return capacidadTotal; }
}