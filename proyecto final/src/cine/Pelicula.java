package cine;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
/**
 * Representa una película disponible en la cartelera del cine.
 * Actúa como un objeto de modelo (DTO) que contiene la información descriptiva y técnica
 * de la cinta, incluyendo su duración para cálculos de agenda.
 */
public class Pelicula {
    /** Título de la película. */
    private String nombre;
    /** Género cinematográfico (ej. Acción, Terror, Comedia). */
    private String genero;
    /** Breve resumen de la trama. */
    private String sinopsis;
    /**
     * Duración de la película.
     * <p>
     * <b>Nota de implementación:</b> Se utiliza {@link LocalTime} para representar la duración
     * (horas y minutos), lo cual facilita las operaciones de suma de tiempo en la programación
     * de funciones.
     * </p>
     */
    private LocalTime duracion;
    /**
     * Constructor para crear una nueva película.
     * Realiza la conversión (parsing) de la duración de texto a objeto de tiempo.
     *
     * @param nombre      El título de la película.
     * @param genero      El género al que pertenece.
     * @param sinopsis    La descripción de la trama.
     * @param duracionStr La duración en formato de texto estricto "HH:mm" (ej. "02:30").
     * @throws java.time.format.DateTimeParseException Si el formato de la hora no es válido.
     */
    public Pelicula(String nombre, String genero, String sinopsis, String duracionStr) {
        this.nombre = nombre;
        this.genero = genero;
        this.sinopsis = sinopsis;
        // Parseamos el string "HH:mm" a un objeto de tiempo real
        // Esto permite que luego podamos sumar .plusHours() o .plusMinutes() fácilmente
        this.duracion = LocalTime.parse(duracionStr, DateTimeFormatter.ofPattern("HH:mm"));
    }
    // Getters
    /**
     * Obtiene el título de la película.
     * @return El nombre como cadena de texto.
     */
    public String getNombre() { return nombre; }
    /**
     * Obtiene el género de la película.
     * @return El género como cadena de texto.
     */
    public String getGenero() { return genero; }
    /**
     * Obtiene la sinopsis de la película.
     * @return La sinopsis como cadena de texto.
     */
    public String getSinopsis() { return sinopsis; }
    /**
     * Obtiene la duración de la película.
     * @return Un objeto {@link LocalTime} representando horas y minutos de duración.
     */
    public LocalTime getDuracion() { return duracion; }
    /**
     * Representación en texto de la película.
     * Útil para depuración o listados simples en consola.
     *
     * @return Cadena con formato "Nombre (HH:mm)".
     */
    @Override
    public String toString() {
        return nombre + " (" + duracion + ")";
    }
}