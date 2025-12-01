package cine;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Representa una proyección (función) de una película en una sala.
 * <p>
 * Controla la ocupación de asientos **a nivel de función** (cada función tiene su propio mapa
 * de asientos vendidos). Proporciona utilidades para comprobar disponibilidad, vender asientos
 * por código (ej. "H7") y visualizar el mapa de asientos mostrando cuáles están libres y cuáles ocupados.
 * </p>
 */
public class FuncionDeCine {

    private final Pelicula pelicula;
    private final Sala sala;
    private final LocalDateTime fechaHora;
    private final String idFuncion;

    /**
     * Matriz espejo a la disposición física de {@link Sala#getAsientos()}.
     * Si asientosVendidos[i][j] == null -> libre, si no -> contiene el {@link Boleto}.
     */
    private final Boleto[][] asientosVendidos;

    private static final DateTimeFormatter FECHA_FMT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter HORA_FMT  = DateTimeFormatter.ofPattern("HHmm");
    private static final DateTimeFormatter FECHA_MOSTRAR = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter HORA_MOSTRAR  = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Crea una nueva función.
     *
     * @param pelicula Película proyectada (no nulo).
     * @param sala     Sala donde se proyecta (no nulo).
     * @param fecha    Fecha de la proyección (no nulo).
     * @param hora     Hora de inicio (no nulo).
     * @throws IllegalArgumentException si algún parámetro es nulo.
     */
    public FuncionDeCine(Pelicula pelicula, Sala sala, LocalDate fecha, LocalTime hora) {
        if (pelicula == null || sala == null || fecha == null || hora == null) {
            throw new IllegalArgumentException("Parámetros nulos en constructor de FuncionDeCine");
        }
        this.pelicula = pelicula;
        this.sala = sala;
        this.fechaHora = LocalDateTime.of(fecha, hora);

        // Construimos la matriz espejo (soporta jagged arrays)
        String[][] molde = sala.getAsientos();
        this.asientosVendidos = new Boleto[molde.length][];
        for (int i = 0; i < molde.length; i++) {
            this.asientosVendidos[i] = new Boleto[molde[i].length];
        }

        this.idFuncion = generarId();
    }

    /**
     * Genera ID legible para la función.
     * Formato: INI_TITULO:AAAAMMDD:hhmm:SALA
     */
    private String generarId() {
        String nombre = pelicula.getNombre() == null ? "PEL" : pelicula.getNombre().trim();
        String ini = extraerIniciales(nombre);
        String fecha = fechaHora.format(FECHA_FMT);
        String hora = fechaHora.format(HORA_FMT);
        String salaClean = sala.getIdSala().replaceAll("\\s+", "");
        return ini + ":" + fecha + ":" + hora + ":" + salaClean;
    }

    /**
     * Extrae hasta 3 letras iniciales del título (omitimos artículos y espacios).
     */
    private String extraerIniciales(String titulo) {
        if (titulo == null || titulo.isBlank()) return "PEL";
        // Tomar las primeras letras de las primeras 2 palabras si es posible
        String[] parts = titulo.replaceAll("[^A-Za-z0-9 ]", "").trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length && sb.length() < 3; i++) {
            sb.append(parts[i].charAt(0));
        }
        String res = sb.toString().toUpperCase();
        if (res.length() < 3) res = (res + "XXX").substring(0,3);
        return res;
    }

    /* -------------------- Disponibilidad y venta -------------------- */

    /**
     * Indica si un asiento (por etiqueta) está libre en esta función.
     *
     * @param codigoEtiqueta ej. "A1"
     * @return true si existe y está libre; false si está ocupado o inválido.
     */
    public synchronized boolean asientoDisponible(String codigoEtiqueta) {
        try {
            int[] idx = sala.indicesDesdeCodigo(codigoEtiqueta);
            int f = idx[0], c = idx[1];
            if (f < 0 || f >= asientosVendidos.length) return false;
            if (c < 0 || c >= asientosVendidos[f].length) return false;
            return asientosVendidos[f][c] == null;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Vende (reserva) los asientos indicados por códigos y crea los boletos correspondientes.
     * Este método es <b>synchronized</b> para evitar ventas simultáneas inconsistentes.
     *
     * @param codigos Lista de códigos (ej. ["H7","H8"])
     * @param tipoCliente categoría para el boleto (ej. "Adulto")
     * @return Lista de {@link Boleto} generados (en el mismo orden que codigos).
     * @throws IllegalArgumentException si algún asiento no existe o ya está ocupado.
     */
    public synchronized List<Boleto> venderAsientosPorCodigos(List<String> codigos, String tipoCliente) {
        // Validar primero que todos existan y estén libres
        for (String cod : codigos) {
            if (!asientoExisteEnSala(cod))
                throw new IllegalArgumentException("Asiento inválido: " + cod);
            if (!asientoDisponible(cod))
                throw new IllegalArgumentException("Asiento no disponible: " + cod);
        }

        List<Boleto> vendidos = new ArrayList<>();
        for (String cod : codigos) {
            int[] idx = sala.indicesDesdeCodigo(cod);
            int f = idx[0], c = idx[1];

            // Generar precio (por ahora fijo por boleto; se puede modificar para tarifas)
            double precio = calcularPrecioBase();

            // Generar clave por boleto: InicialesTitulo:AAAAMMDD:hhmm:Sala:Asiento
            String clave = generarClavePorAsiento(cod);

            Boleto b = new Boleto(cod, precio, tipoCliente);
            // si Boleto tuviera id, podríamos setear b.setIdBoleto(clave) -> no existe setter, ignoramos
            // Guardar en matriz (marca ocupado)
            asientosVendidos[f][c] = b;
            vendidos.add(b);
        }
        return vendidos;
    }

    /**
     * Calcula un precio base por boleto. (Actualmente constante, modificar si quieres tarifas).
     * @return precio double
     */
    private double calcularPrecioBase() {
        // Puedes derivarlo de la película/género/hora; por simplicidad devolvemos fijo.
        return 70.0;
    }

    /**
     * Genera la clave para un asiento específico según la especificación:
     * InicialesDelTítuloDeLaPelícula:AAAAMMDD:hhmm:Sala:Asiento
     */
    private String generarClavePorAsiento(String asiento) {
        String ini = extraerIniciales(pelicula.getNombre());
        String fecha = fechaHora.format(FECHA_FMT);
        String hora = fechaHora.format(HORA_FMT);
        String salaStr = sala.getIdSala().replaceAll("\\s+","");
        return ini + ":" + fecha + ":" + hora + ":" + salaStr + ":" + asiento;
    }

    /**
     * Indica si el código de asiento es válido en el molde físico de la sala.
     */
    private boolean asientoExisteEnSala(String codigo) {
        try {
            int[] idx = sala.indicesDesdeCodigo(codigo);
            int f = idx[0], c = idx[1];
            return f >= 0 && f < sala.getAsientos().length && c >= 0 && c < sala.getAsientos()[f].length;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Devuelve la cantidad de asientos libres actualmente.
     */
    public synchronized int getDisponibles() {
        int libres = 0;
        for (int i = 0; i < asientosVendidos.length; i++) {
            for (int j = 0; j < asientosVendidos[i].length; j++) {
                if (asientosVendidos[i][j] == null) libres++;
            }
        }
        return libres;
    }

    /**
     * Visualiza el estado de la sala para esta función, marcando "XX" los ocupados.
     * Para sala VIP dibuja espacios extra (pasillos) entre grupos 1-2 | 3-4 | 5-6.
     *
     * @return String con representación para mostrar al cliente.
     */
    public synchronized String visualizarEstadoAsientos() {
        StringBuilder sb = new StringBuilder();
        String[][] molde = sala.getAsientos();
        boolean esVIP = "VIP".equalsIgnoreCase(sala.getTipo());

        for (int i = 0; i < molde.length; i++) {
            for (int j = 0; j < molde[i].length; j++) {
                String etiqueta = molde[i][j];
                boolean ocupada = asientosVendidos[i][j] != null;

                if (esVIP) {
                    // grupos 0-1, 2-3, 4-5 con espacios entre ellos
                    if (j == 2 || j == 4) sb.append("   "); // espacio extra para pasillo visual
                }

                if (ocupada) sb.append(String.format("%-5s", "XX"));
                else sb.append(String.format("%-5s", etiqueta));
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    /* -------------------- Getters y utilidades -------------------- */

    public String getIdFuncion() { return idFuncion; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public LocalDate getFecha() { return fechaHora.toLocalDate(); }
    public LocalTime getHora() { return fechaHora.toLocalTime(); }
    public Pelicula getPelicula() { return pelicula; }
    public Sala getSala() { return sala; }

    @Override
    public String toString() {
        return "Función " + idFuncion + " - " + pelicula.getNombre() + " en " + sala.getIdSala()
                + " (" + fechaHora.format(FECHA_MOSTRAR) + " " + fechaHora.format(HORA_MOSTRAR) + ")";
    }
    /**
 * Verifica si existe conflicto (solapamiento) entre esta función y otra.
 * <p>
 * Regla: Debe haber al menos 30 minutos entre el fin de una función y el inicio de otra
 * en la misma sala. Se toma en cuenta la duración de la película y el colchón.
 * </p>
 *
 * @param otraFuncion La función a comparar.
 * @return true si hay conflicto, false si no.
 */
public boolean hayConflicto(FuncionDeCine otraFuncion) {
    if (otraFuncion == null) return false;

    // Si las salas son diferentes, no hay conflicto posible
    if (!this.sala.getIdSala().equalsIgnoreCase(otraFuncion.getSala().getIdSala())) {
        return false;
    }

    LocalDateTime inicioA = this.fechaHora;
    LocalDateTime finA = inicioA
            .plusHours(this.pelicula.getDuracion().getHour())
            .plusMinutes(this.pelicula.getDuracion().getMinute())
            .plusMinutes(30); // colchón obligatorio

    LocalDateTime inicioB = otraFuncion.getFechaHora();
    LocalDateTime finB = inicioB
            .plusHours(otraFuncion.getPelicula().getDuracion().getHour())
            .plusMinutes(otraFuncion.getPelicula().getDuracion().getMinute())
            .plusMinutes(30);

    // Hay solapamiento si InicioA < FinB y InicioB < FinA
    return inicioA.isBefore(finB) && inicioB.isBefore(finA);
}

}
