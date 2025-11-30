import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Clase utilitaria encargada de la capa de persistencia de datos (Entrada/Salida).
 * Gestiona el almacenamiento físico de la información en archivos de texto con formato CSV.
 * <p>
 * Esta clase no debe ser instanciada, sus métodos son estáticos para acceso global.
 * </p>
 */
public class GestorArchivos {
    /** Nombre del directorio raíz donde se guardarán los datos de la aplicación. */
    private static final String RUTA_CARPETA = "ArchivosAplicacion";
    /** Ruta completa al archivo que contiene la base de datos de películas. */
    private static final String ARCHIVO_PELICULAS = RUTA_CARPETA + "/peliculas.csv";
    /**
     * Constructor privado para evitar la instanciación de la clase.
     * Al ser una clase de utilidad (solo métodos estáticos), no tiene sentido crear objetos de ella.
     */
    private GestorArchivos() {}
    /**
     * Verifica la existencia del directorio de almacenamiento.
     * Si la carpeta definida en {@code RUTA_CARPETA} no existe, intenta crearla.
     */
    public static void verificarCarpeta() {
        File carpeta = new File(RUTA_CARPETA);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }
    /**
     * Serializa la lista completa de películas y la escribe en el disco.
     * <p>
     * <b>Advertencia:</b> Este método sobrescribe el archivo completo cada vez que se llama.
     * El formato de guardado es: {@code Nombre,Genero,Sinopsis,Duracion}
     * </p>
     *
     * @param peliculas La lista de objetos {@link Pelicula} que se desea persistir.
     */
    public static void guardarPeliculas(List<Pelicula> peliculas) {
        verificarCarpeta();
        // Usamos try-with-resources para asegurar que el PrintWriter se cierre automáticamente
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_PELICULAS))) {
            for (Pelicula p : peliculas) {
                // Escribimos los datos separados por comas (CSV estándar)
                writer.println(
                    p.getNombre() + "," +
                    p.getGenero() + "," +
                    p.getSinopsis() + "," +
                    p.getDuracion() // LocalTime se guarda como HH:mm automáticamente
                );
            }
            System.out.println("-> Datos de películas guardados en " + ARCHIVO_PELICULAS);
        } catch (IOException e) {
            System.err.println("Error al guardar películas: " + e.getMessage());
        }
    }
    /**
     * Lee el archivo CSV y reconstruye los objetos {@link Pelicula} en memoria.
     * <p>
     * Se realiza un análisis línea por línea separando los campos por comas.
     * Se valida que cada línea tenga exactamente 4 columnas para evitar errores de índice.
     * </p>
     *
     * @return Una {@code List<Pelicula>} con los datos recuperados. Si el archivo no existe, retorna una lista vacía.
     */
    public static List<Pelicula> cargarPeliculas() {
        List<Pelicula> lista = new ArrayList<>();
        File archivo = new File(ARCHIVO_PELICULAS);
        if (!archivo.exists()) {
            return lista; // Si no existe, retornamos lista vacía para evitar NullPointerException fuera
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                
                // Validamos integridad de datos: Deben ser 4 columnas exactas
                if (datos.length == 4) { 
                    lista.add(new Pelicula(
                        datos[0], // Nombre
                        datos[1], // Genero
                        datos[2], // Sinopsis
                        datos[3]  // Duracion (El constructor de Pelicula la convierte a LocalTime)
                    ));
                }
            }
            System.out.println("-> Se cargaron " + lista.size() + " películas del archivo.");
        } catch (IOException e) {
            System.err.println("Error al cargar películas: " + e.getMessage());
        }
        return lista;
    }
}