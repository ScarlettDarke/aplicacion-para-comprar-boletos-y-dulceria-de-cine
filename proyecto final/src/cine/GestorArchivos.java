package cine;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestor de archivos centralizado para la aplicación.
 * <p>
 * Provee utilidades para:
 * <ul>
 *   <li>Crear/verificar carpetas necesarias</li>
 *   <li>Persistir y cargar clientes y empleados (.dat via serialización)</li>
 *   <li>Persistir y cargar películas (CSV)</li>
 *   <li>Guardar/leer notificaciones y historial de vendedores (archivos de texto)</li>
 * </ul>
 * </p>
 *
 * Nota: las clases Cliente y Empleado deben implementar Serializable para la serialización binaria.
 */
public class GestorArchivos {

    public static final String RUTA_BASE = "ArchivosAplicacion";
    public static final String RUTA_NOTIFICACIONES = RUTA_BASE + File.separator + "notificaciones";
    public static final String RUTA_HISTORIAL = RUTA_BASE + File.separator + "historial_vendedores";
    public static final String ARCHIVO_PELICULAS_CSV = RUTA_BASE + File.separator + "peliculas.csv";

    private static final DateTimeFormatter TF = DateTimeFormatter.ofPattern("yyyyMMdd:HHmm");

    private GestorArchivos() {
        // utilitaria - no instanciar
    }

    /**
     * Verifica que la carpeta principal y subcarpetas existan; si no, las crea.
     */
    public static void verificarCarpeta() {
        try {
            Path base = Paths.get(RUTA_BASE);
            if (Files.notExists(base)) Files.createDirectories(base);

            Path notif = Paths.get(RUTA_NOTIFICACIONES);
            if (Files.notExists(notif)) Files.createDirectories(notif);

            Path hist = Paths.get(RUTA_HISTORIAL);
            if (Files.notExists(hist)) Files.createDirectories(hist);

            // Asegurar existencia del archivo de películas
            Path pel = Paths.get(ARCHIVO_PELICULAS_CSV);
            if (Files.notExists(pel)) Files.createFile(pel);

        } catch (IOException e) {
            System.err.println("Error creando carpetas de aplicación: " + e.getMessage());
        }
    }

    /**
     * Inicializa la estructura de archivos del sistema. Llamar al inicio de la app.
     */
    public static void inicializarSistema() {
        verificarCarpeta();
    }

    // ------------------ Serialización (clientes y empleados) ------------------

    /**
     * Guarda la lista de clientes en ruta especificada (.dat) usando serialización.
     *
     * @param clientes lista de clientes (puede ser vacía)
     * @param ruta ruta al archivo de salida (ej. "ArchivosAplicacion/clientes.dat")
     */
    public static void guardarClientes(List<Cliente> clientes, String ruta) {
        if (clientes == null) clientes = new ArrayList<>();
        try {
            Path parent = Paths.get(ruta).getParent();
            if (parent != null && Files.notExists(parent)) Files.createDirectories(parent);

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {
                oos.writeObject(clientes);
            }
        } catch (IOException e) {
            System.err.println("Error guardando clientes: " + e.getMessage());
        }
    }

    /**
     * Carga la lista de clientes desde un archivo .dat.
     *
     * @param ruta ruta al archivo .dat
     * @return lista de clientes, o lista vacía si no existe o en caso de error.
     */
    @SuppressWarnings("unchecked")
    public static List<Cliente> cargarClientes(String ruta) {
        File f = new File(ruta);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            Object o = ois.readObject();
            return (List<Cliente>) o;
        } catch (Exception e) {
            System.err.println("Error cargando clientes desde " + ruta + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Guarda la lista de empleados en ruta especificada (.dat) usando serialización.
     *
     * @param empleados lista de empleados
     * @param ruta ruta al archivo .dat
     */
    public static void guardarEmpleados(List<Empleado> empleados, String ruta) {
        if (empleados == null) empleados = new ArrayList<>();
        try {
            Path parent = Paths.get(ruta).getParent();
            if (parent != null && Files.notExists(parent)) Files.createDirectories(parent);

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {
                oos.writeObject(empleados);
            }
        } catch (IOException e) {
            System.err.println("Error guardando empleados: " + e.getMessage());
        }
    }

    /**
     * Carga la lista de empleados desde archivo .dat.
     *
     * @param ruta ruta al archivo .dat
     * @return lista de empleados o lista vacía en caso de error o no existir archivo.
     */
    @SuppressWarnings("unchecked")
    public static List<Empleado> cargarEmpleados(String ruta) {
        File f = new File(ruta);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            Object o = ois.readObject();
            return (List<Empleado>) o;
        } catch (Exception e) {
            System.err.println("Error cargando empleados desde " + ruta + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ------------------ Películas (CSV) ------------------

    /**
     * Guarda la lista de películas en formato CSV (sobrescribe).
     * Formato por línea: Nombre,Genero,Sinopsis,HH:mm
     *
     * @param peliculas lista de Pelicula
     */
    public static void guardarPeliculas(List<Pelicula> peliculas) {
        verificarCarpeta();
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_PELICULAS_CSV))) {
            for (Pelicula p : peliculas) {
                // reemplazar comas para no romper CSV
                String nombre = sanitizeCsv(p.getNombre());
                String genero = sanitizeCsv(p.getGenero());
                String sinopsis = sanitizeCsv(p.getSinopsis());
                String dur = p.getDuracion().toString(); // LocalTime as HH:mm
                pw.println(String.join(",", nombre, genero, sinopsis, dur));
            }
        } catch (IOException e) {
            System.err.println("Error guardando peliculas: " + e.getMessage());
        }
    }

    /**
     * Carga películas desde CSV y las devuelve en una lista.
     *
     * @return lista de Pelicula (vacía si archivo no existe o error).
     */
    public static List<Pelicula> cargarPeliculas() {
        verificarCarpeta();
        List<Pelicula> salida = new ArrayList<>();
        File f = new File(ARCHIVO_PELICULAS_CSV);
        if (!f.exists()) return salida;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] cols = linea.split(",", 4);
                if (cols.length < 4) continue;
                salida.add(new Pelicula(cols[0], cols[1], cols[2], cols[3]));
            }
        } catch (IOException e) {
            System.err.println("Error cargando peliculas: " + e.getMessage());
        }
        return salida;
    }

    private static String sanitizeCsv(String s) {
        if (s == null) return "";
        return s.replace(",", " ").replaceAll("[\\r\\n]+", " ");
    }

    // ------------------ Notificaciones e historial ------------------

    /**
     * Guarda una notificación (sobrescribe) para una clave de orden.
     *
     * @param clave clave identificadora (ej. ORD-20250101...).
     * @param texto texto a guardar.
     */
    public static void guardarNotificacion(String clave, String texto) {
        verificarCarpeta();
        Path p = Paths.get(RUTA_NOTIFICACIONES, clave + ".txt");
        try (BufferedWriter bw = Files.newBufferedWriter(p)) {
            bw.write(texto);
        } catch (IOException e) {
            System.err.println("Error guardando notificación: " + e.getMessage());
        }
    }

    /**
     * Lee la notificación asociada a una clave y devuelve su contenido.
     *
     * @param clave clave de la orden
     * @return contenido del archivo o mensaje por defecto si no existe.
     */
    public static String leerNotificacion(String clave) {
        verificarCarpeta();
        Path p = Paths.get(RUTA_NOTIFICACIONES, clave + ".txt");
        if (!Files.exists(p)) return "No hay notificaciones para la clave " + clave;
        try {
            return new String(Files.readAllBytes(p));
        } catch (IOException e) {
            return "Error leyendo notificación: " + e.getMessage();
        }
    }

    /**
     * Agrega una línea al historial del vendedor (archivo por nickname).
     *
     * @param nickname nickname del vendedor
     * @param texto línea a agregar
     */
    public static void guardarHistorialVendedor(String nickname, String texto) {
        verificarCarpeta();
        Path p = Paths.get(RUTA_HISTORIAL, nickname + ".txt");
        try (BufferedWriter bw = Files.newBufferedWriter(p, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            String linea = (LocalDateTime.now().format(TF)) + " - " + texto;
            bw.write(linea);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error guardando historial vendedor: " + e.getMessage());
        }
    }
}
