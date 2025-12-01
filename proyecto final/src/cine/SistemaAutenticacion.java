package cine;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Gestor central de autenticación y registro de usuarios.
 *
 * <p>
 * Provee:
 * <ul>
 *   <li>Pantalla inicial (registro cliente / inicio sesión)</li>
 *   <li>Carga y guardado de clientes y empleados usando {@link GestorArchivos}</li>
 *   <li>Singleton accesible mediante {@link #getInstancia()}</li>
 *   <li>Redirección a menús según tipo de usuario</li>
 * </ul>
 * </p>
 *
 * NOTA: Mantengo el constructor público para compatibilidad (algunos puntos del
 * proyecto instancian directamente la clase). Sin embargo, recomiendo usar
 * {@link #getInstancia()} desde otras clases para compartir el mismo estado.
 */
public class SistemaAutenticacion {

    private static final Scanner sc = new Scanner(System.in);

    // ----- Datos en memoria -----
    private List<Cliente> clientes;
    private List<Empleado> empleados;

    // Rutas (usadas por GestorArchivos)
    private static final String ARCHIVO_CLIENTES_DAT = "ArchivosAplicacion/clientes.dat";
    private static final String ARCHIVO_EMPLEADOS_DAT = "ArchivosAplicacion/empleados.dat";

    // Singleton (accesible desde Reportes y otras clases)
    private static SistemaAutenticacion instancia = null;

    /**
     * Constructor.
     * Inicializa la estructura de carpetas, carga datos y garantiza la existencia
     * de un administrador por defecto.
     *
     * Aunque existe {@link #getInstancia()}, el constructor se deja público para
     * compatibilidad con llamadas existentes en el proyecto.
     */
    public SistemaAutenticacion() {
        // inicializa estructura de archivos y carga listas
        GestorArchivos.inicializarSistema();
        cargarDatos();
        inicializarAdministradorPorDefecto();

        // Si no hay instancia singleton registrada, la registramos para mantener
        // consistencia en el resto del sistema.
        if (instancia == null) {
            instancia = this;
        }
    }

    /**
     * Devuelve la instancia compartida del sistema de autenticación.
     * Si no existe la crea (lazy init).
     *
     * @return instancia única de SistemaAutenticacion
     */
    public static synchronized SistemaAutenticacion getInstancia() {
        if (instancia == null) {
            instancia = new SistemaAutenticacion();
        }
        return instancia;
    }

    /* -------------------- Carga / Guardado -------------------- */

    /**
     * Carga listas de clientes y empleados desde disco usando GestorArchivos.
     */
    @SuppressWarnings("unchecked")
    private void cargarDatos() {
        // GestorArchivos devuelve listas (vacías) si el archivo no existe o hay error,
        // por lo que podemos asignar directamente.
        clientes = GestorArchivos.cargarClientes(ARCHIVO_CLIENTES_DAT);
        empleados = GestorArchivos.cargarEmpleados(ARCHIVO_EMPLEADOS_DAT);

        if (clientes == null) clientes = new ArrayList<>();
        if (empleados == null) empleados = new ArrayList<>();
    }

    /**
     * Persiste las listas actuales de clientes y empleados en disco.
     */
    public void guardarDatos() {
        GestorArchivos.guardarClientes(clientes, ARCHIVO_CLIENTES_DAT);
        GestorArchivos.guardarEmpleados(empleados, ARCHIVO_EMPLEADOS_DAT);
    }

    /**
     * Asegura que exista un administrador por defecto en el sistema.
     * Si no existe lo crea, lo agrega a la lista y guarda los datos.
     */
    private void inicializarAdministradorPorDefecto() {
        boolean adminExiste = empleados.stream()
                .anyMatch(emp -> emp.getNickname() != null && "elAdministrador".equalsIgnoreCase(emp.getNickname()));

        if (!adminExiste) {
            Administrador adminDefault = new Administrador(
                    "Administrador", "Sistema", "PorDefecto",
                    "elAdministrador", "3l4dm1n", "admin@cine.com",
                    "5512345678", "matutino", "entre semana"
            );
            empleados.add(adminDefault);
            guardarDatos();
            System.out.println("Administrador por defecto creado: elAdministrador / 3l4dm1n");
        }
    }

    /* -------------------- Pantalla inicial y flujo de registro / login -------------------- */

    /**
     * Muestra la pantalla principal de la aplicación (registro cliente / ingreso).
     * Bucle interactivo por consola.
     */
    public void mostrarPantallaInicial() {
        while (true) {
            System.out.println("\n=== CINE APP ===");
            System.out.println("1. Nuevo registro de cliente");
            System.out.println("2. Ingreso al sistema");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");

            String opcion = sc.nextLine().trim();

            switch (opcion) {
                case "1":
                    registrarNuevoCliente();
                    break;
                case "2":
                    iniciarSesion();
                    break;
                case "3":
                    System.out.println("¡Hasta pronto!");
                    return;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }

    /**
     * Flujo guiado para registrar un nuevo cliente desde consola.
     * Valida contraseñas, unicidad de nickname y formato de tarjeta.
     */
    private void registrarNuevoCliente() {
        System.out.println("\n=== NUEVO REGISTRO DE CLIENTE ===");

        boolean datosCorrectos = false;
        Cliente nuevoCliente = null;

        while (!datosCorrectos) {

            System.out.print("Nombre(s): ");
            String nombre = sc.nextLine().trim();

            System.out.print("Apellido Paterno: ");
            String apellidoPaterno = sc.nextLine().trim();

            System.out.print("Apellido Materno: ");
            String apellidoMaterno = sc.nextLine().trim();

            int edad;
            while (true) {
                try {
                    System.out.print("Edad: ");
                    edad = Integer.parseInt(sc.nextLine().trim());
                    if (edad < 0) throw new NumberFormatException();
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Edad inválida. Intente con un número entero positivo.");
                }
            }

            String nickname;
            while (true) {
                System.out.print("Nickname: ");
                nickname = sc.nextLine().trim();
                if (nickname.isEmpty()) {
                    System.out.println("Nickname no puede estar vacío.");
                    continue;
                }
                if (nicknameExiste(nickname)) {
                    System.out.println("El nickname ya está en uso. Elija otro.");
                    continue;
                }
                break;
            }

            String contrasena;
            while (true) {
                System.out.print("Contraseña: ");
                contrasena = sc.nextLine();
                System.out.print("Confirmar Contraseña: ");
                String confirmar = sc.nextLine();
                if (!contrasena.equals(confirmar)) {
                    System.out.println("Las contraseñas no coinciden. Intente nuevamente.");
                } else {
                    break;
                }
            }

            System.out.print("Correo electrónico: ");
            String correo = sc.nextLine().trim();

            System.out.print("Número de celular: ");
            String celular = sc.nextLine().trim();

            String tarjeta;
            while (true) {
                System.out.print("Número de tarjeta bancaria (16 dígitos): ");
                tarjeta = sc.nextLine().trim();
                if (tarjeta.matches("\\d{16}")) break;
                System.out.println("Número de tarjeta inválido. Debe tener 16 dígitos numéricos.");
            }

            nuevoCliente = new Cliente(nombre, apellidoPaterno, apellidoMaterno, edad,
                    nickname, contrasena, correo, celular, tarjeta);

            System.out.println("\n=== DATOS INGRESADOS ===");
            System.out.println(nuevoCliente);
            System.out.print("\n¿Los datos son correctos? (s/n): ");
            String respuesta = sc.nextLine().trim();

            if (respuesta.equalsIgnoreCase("s")) {
                datosCorrectos = true;
            } else {
                System.out.println("Reingrese los datos.");
            }
        }

        clientes.add(nuevoCliente);
        guardarDatos();

        System.out.println("\nRegistro exitoso! Mostrando mensaje por 5 segundos...");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Verifica si un nickname ya existe entre clientes o empleados.
     *
     * @param nickname nickname a checar
     * @return true si existe, false en caso contrario
     */
    public boolean nicknameExiste(String nickname) {
        boolean existeEnClientes = clientes.stream().anyMatch(c -> c.getNickname().equalsIgnoreCase(nickname));
        boolean existeEnEmpleados = empleados.stream().anyMatch(e -> e.getNickname().equalsIgnoreCase(nickname));
        return existeEnClientes || existeEnEmpleados;
    }

    /**
     * Flujo de inicio de sesión: solicita nickname y contraseña, valida y redirige.
     * Soporta 'salir' para volver a la pantalla anterior.
     */
    private void iniciarSesion() {
        System.out.println("\n=== INGRESO AL SISTEMA ===");

        while (true) {
            System.out.print("Nickname (o 'salir' para volver): ");
            String nickname = sc.nextLine().trim();
            if ("salir".equalsIgnoreCase(nickname)) return;

            System.out.print("Contraseña: ");
            String contrasena = sc.nextLine();

            // Buscar en empleados
            for (Empleado empleado : empleados) {
                if (empleado.validarCredenciales(nickname, contrasena)) {
                    System.out.println("\n¡Bienvenido " + empleado.getNombre() + "!");
                    redirigirSegunTipoUsuario(empleado);
                    return;
                }
            }

            // Buscar en clientes
            for (Cliente cliente : clientes) {
                if (cliente.validarCredenciales(nickname, contrasena)) {
                    System.out.println("\n¡Bienvenido " + cliente.getNombre() + "!");
                    redirigirSegunTipoUsuario(cliente);
                    return;
                }
            }

            System.out.println("Credenciales incorrectas. Intente nuevamente.");
        }
    }

    /**
     * Redirige a los menús según el tipo de usuario.
     *
     * @param usuario instancia de Persona (Cliente o Empleado)
     */
    private void redirigirSegunTipoUsuario(Persona usuario) {
        if (usuario instanceof Administrador) {
            MenuAdministrador.mostrarMenu((Administrador) usuario);
        } else if (usuario instanceof VendedorDulceria) {
            MenuVendedorDulceria.mostrarMenu((VendedorDulceria) usuario);
        } else if (usuario instanceof Cliente) {
            MenuCliente.mostrarMenu((Cliente) usuario);
        } else {
            System.out.println("Tipo de usuario no reconocido.");
        }
    }

    /* -------------------- Operaciones auxiliares públicas -------------------- */

    /**
     * Permite que el administrador agregue un empleado (vía menú).
     *
     * @param emp instancia de Empleado creada por el administrador
     */
    public void agregarEmpleadoDesdeAdministrador(Empleado emp) {
        if (emp == null) return;
        empleados.add(emp);
        guardarDatos();
    }

    /* -------------------- Getters para otras clases (Reportes, UI, etc.) -------------------- */

    /** @return lista de clientes en memoria (NO null). */
    public List<Cliente> getClientes() {
        return clientes;
    }

    /** @return lista de empleados en memoria (NO null). */
    public List<Empleado> getEmpleados() {
        return empleados;
    }

    /* -------------------- MAIN de arranque rápido (para pruebas) -------------------- */

    /**
     * Punto de entrada simple para ejecutar la pantalla inicial sin usar el singleton.
     */
    public static void main(String[] args) {
        // Si alguien ejecuta con java SistemaAutenticacion, usamos el singleton por consistencia
        SistemaAutenticacion s = SistemaAutenticacion.getInstancia();
        s.mostrarPantallaInicial();
    }
}
