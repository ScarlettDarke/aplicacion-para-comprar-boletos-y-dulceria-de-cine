package cine;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
/**
 * Clase estática encargada de la gestión centralizada de la cartelera del cine.
 * Mantiene en memoria (RAM) las listas de películas y funciones programadas,
 * actuando como intermediario entre la persistencia de datos y la lógica de negocio.
 */
public class Cartelera {
    /** * Lista en memoria que almacena todas las películas disponibles.
     * Se carga desde el almacenamiento persistente al iniciar.
     */
    public static List<Pelicula> peliculas = new ArrayList<>();
    /** * Lista en memoria que almacena todas las funciones (proyecciones) programadas.
     * Se valida contra esta lista para evitar conflictos de horarios.
     */
    public static List<FuncionDeCine> funciones = new ArrayList<>();
    /**
     * Inicializa el sistema verificando la integridad de los archivos y cargando datos.
     * <p>
     * Este método debe ser llamado al arranque de la aplicación para asegurar
     * que las listas en memoria contengan la información persistida.
     * </p>
     */
    public static void inicializar() {
        // Verifica si existe la carpeta, si no, la crea
        GestorArchivos.verificarCarpeta();
        // Carga las películas del archivo a la RAM
        peliculas = GestorArchivos.cargarPeliculas();
        System.out.println("--- Sistema Inicializado ---");
        System.out.println("Películas en cartelera: " + peliculas.size());
    }
    /**
     * Crea una nueva película, la añade a la lista en memoria y actualiza el archivo de persistencia.
     *
     * @param nombre      El título de la película.
     * @param genero      El género cinematográfico.
     * @param sinopsis    Breve descripción de la trama.
     * @param duracionStr La duración de la película en formato texto (se parseará internamente).
     */
    public static void agregarPelicula(String nombre, String genero, String sinopsis, String duracionStr) {
        Pelicula nuevaPeli = new Pelicula(nombre, genero, sinopsis, duracionStr);
        peliculas.add(nuevaPeli);
        // Cada vez que agregamos, re-escribimos el archivo CSV para asegurar los datos.
        GestorArchivos.guardarPeliculas(peliculas);
        System.out.println("-> Película agregada: " + nuevaPeli.getNombre());
    }
    /**
     * Intenta programar una nueva función en una sala y horario específicos.
     * Realiza una validación crítica para asegurar que la sala no esté ocupada.
     *
     * @param pelicula La película que se va a proyectar.
     * @param sala     La sala donde se proyectará.
     * @param fecha    La fecha de la proyección.
     * @param hora     La hora de inicio de la proyección.
     * @throws HorarioOcupadoException Si existe un conflicto de horario con otra función en la misma sala.
     */
    public static void agregarFuncion(Pelicula pelicula, Sala sala, LocalDate fecha, LocalTime hora) 
            throws HorarioOcupadoException {
        // 1. Crear la candidata
        FuncionDeCine nuevaFuncion = new FuncionDeCine(pelicula, sala, fecha, hora);
        // 2. Barrer todas las funciones existentes buscando conflictos
        for (FuncionDeCine funcionExistente : funciones) {
            if (nuevaFuncion.hayConflicto(funcionExistente)) {
                throw new HorarioOcupadoException(
                    "CRÍTICO: La sala " + sala.getIdSala() + 
                    " está ocupada en ese horario (o en tiempo de limpieza)."
                );
            }
        }
        // 3. Si sobrevivió al for, es válida. La guardamos.
        funciones.add(nuevaFuncion);
        System.out.println("-> Función programada exitosamente: ID " + nuevaFuncion.getIdFuncion());
    }
    /**
     * Busca una película dentro de la lista cargada en memoria por su nombre.
     * La búsqueda no distingue entre mayúsculas y minúsculas.
     *
     * @param nombre El nombre (o parte del nombre) de la película a buscar.
     * @return El objeto {@code Pelicula} si se encuentra, o {@code null} si no existe.
     */
    public static Pelicula buscarPelicula(String nombre) {
        for (Pelicula p : peliculas) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }
}
/**
 * Excepción personalizada para manejar conflictos de agenda en las salas de cine.
 * Se lanza cuando se intenta programar una función en un horario que se solapa con otra.
 */
class HorarioOcupadoException extends Exception {
    
    /**
     * Constructor de la excepción.
     *
     * @param mensaje Detalle del conflicto de horario encontrado.
     */
    public HorarioOcupadoException(String mensaje) {
        super(mensaje);
    }
}