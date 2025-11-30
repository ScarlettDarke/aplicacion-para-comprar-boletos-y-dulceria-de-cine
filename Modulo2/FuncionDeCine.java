import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
/**
 * Representa una proyección específica de una película en el cine.
 * <p>
 * Esta clase vincula una {@link Pelicula}, una {@link Sala} y un horario ({@link LocalDateTime}).
 * Además, gestiona el estado de ocupación de los asientos para esta función en particular
 * mediante una matriz de boletos vendidos.
 * </p>
 */
public class FuncionDeCine {
    /** La película que se proyectará. */
    private Pelicula pelicula;
    /** La sala física donde ocurrirá la proyección. */
    private Sala sala;
    /** Fecha y hora exacta del inicio de la función. */
    private LocalDateTime fechaHora; // Combina fecha y hora
    /** Identificador único generado automáticamente para la función. */
    private String idFuncion;
    /** * Matriz espejo a la distribución de asientos de la Sala.
     * Almacena los objetos {@link Boleto} vendidos. Si una posición es null, el asiento está libre.
     */
    private Boleto[][] asientosVendidos; 
    /**
     * Constructor principal. Inicializa la función y prepara la matriz de asientos vacía.
     *
     * @param pelicula La película a proyectar.
     * @param sala La sala asignada.
     * @param fecha La fecha de la función.
     * @param hora La hora de inicio.
     */
    public FuncionDeCine(Pelicula pelicula, Sala sala, LocalDate fecha, LocalTime hora) {
        this.pelicula = pelicula;
        this.sala = sala;
        this.fechaHora = LocalDateTime.of(fecha, hora);
        // Inicializamos la matriz de boletos con las mismas dimensiones que la sala
        // Si la sala es [10][15], esto también. Inicialmente todo es null (asiento libre).
        String[][] moldeSala = sala.getAsientos();
        this.asientosVendidos = new Boleto[moldeSala.length][];
        
        // Copia la estructura (filas y columnas) de la sala original para soportar salas irregulares
        for (int i = 0; i < moldeSala.length; i++) {
            this.asientosVendidos[i] = new Boleto[moldeSala[i].length];
        }
        
        this.idFuncion = generarId();
    }
    /**
     * Genera un identificador único (SKU) para la función.
     * Formato: INI_PELICULA:AAAAMMDD:hhmm:SALA
     * * @return Una cadena con el ID generado.
     */
    private String generarId() {
        // 1. Iniciales de la película (Tomamos las primeras 3 letras en mayúsculas)
        String iniciales = pelicula.getNombre().length() >= 3 
                ? pelicula.getNombre().substring(0, 3).toUpperCase() 
                : pelicula.getNombre().toUpperCase();
        // 2. Fecha formato AAAAMMDD
        String fechaStr = fechaHora.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // 3. Hora formato hhmm
        String horaStr = fechaHora.format(DateTimeFormatter.ofPattern("HHmm"));
        // 4. Nombre de Sala
        String salaStr = sala.getIdSala().replace(" ", ""); // Quitamos espacios "Sala A" -> "SalaA"
        
        return iniciales + ":" + fechaStr + ":" + horaStr + ":" + salaStr;
    }
    /**
     * Lógica CRÍTICA: Valida si esta función se empalma temporalmente con otra.
     * <p>
     * Regla de Negocio: Debe haber un intervalo mínimo de 30 minutos (limpieza/desalojo) 
     * entre el fin de una función y el inicio de la siguiente en la misma sala.
     * </p>
     * * @param otraFuncion La función candidata con la cual comparar horarios.
     * @return {@code true} si existe un conflicto (solapamiento), {@code false} si el horario está libre.
     */
    public boolean hayConflicto(FuncionDeCine otraFuncion) {
        // 1. Si no son en la misma sala, no hay conflicto físico.
        if (!this.sala.getIdSala().equals(otraFuncion.getSala().getIdSala())) {
            return false; 
        }
        // Calculamos inicio y fin de ESTA funcion
        LocalDateTime inicioA = this.fechaHora;
        // Fin = inicio + duracion de peli + 30 min de limpieza
        LocalDateTime finA = inicioA
                .plusHours(this.pelicula.getDuracion().getHour())
                .plusMinutes(this.pelicula.getDuracion().getMinute())
                .plusMinutes(30); // COLCHON DE 30 MIN
        // Calculamos inicio y fin de la OTRA función
        LocalDateTime inicioB = otraFuncion.getFechaHora();
        LocalDateTime finB = inicioB
                .plusHours(otraFuncion.getPelicula().getDuracion().getHour())
                .plusMinutes(otraFuncion.getPelicula().getDuracion().getMinute())
                .plusMinutes(30); 
        // Logica de intervalos: ¿Se solapan?
        // Un evento A solapa con B si: InicioA < FinB Y InicioB < FinA
        boolean seSolapan = inicioA.isBefore(finB) && inicioB.isBefore(finA);
        
        return seSolapan;
    }
    /**
     * Registra la venta de un boleto ocupando la posición correspondiente en la matriz.
     *
     * @param boleto El objeto boleto generado.
     * @param fila Índice de la fila (basado en 0).
     * @param columna Índice de la columna (basado en 0).
     */
    public void venderBoleto(Boleto boleto, int fila, int columna) {
        asientosVendidos[fila][columna] = boleto;
    }
    // Getters
    /** @return El ID único de la función. */
    public String getIdFuncion() { return idFuncion; }
    /** @return La fecha y hora de inicio. */
    public LocalDateTime getFechaHora() { return fechaHora; }
    /** @return El objeto Película asociado. */
    public Pelicula getPelicula() { return pelicula; }
    /** @return El objeto Sala asociado. */
    public Sala getSala() { return sala; }
    /** @return La matriz de boletos vendidos (estado actual de la sala). */
    public Boleto[][] getAsientosVendidos() { return asientosVendidos; }
    @Override
    public String toString() {
        return "Función [" + idFuncion + "] - " + pelicula.getNombre() + " en " + sala.getIdSala();
    }
}