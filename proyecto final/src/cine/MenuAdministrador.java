package cine;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

/**
 * MENÚ COMPLETO DEL ADMINISTRADOR
 * Implementa todas las funcionalidades del documento oficial.
 */
public class MenuAdministrador {

    private static final Scanner sc = new Scanner(System.in);

    public static void mostrarMenu(Administrador admin) {

        while (true) {
            System.out.println("\n====== MENÚ ADMINISTRADOR ======");
            System.out.println("1. Dar de alta una película");
            System.out.println("2. Dar de alta una función");
            System.out.println("3. Registrar nuevo empleado");
            System.out.println("4. Ver películas compradas por un cliente");
            System.out.println("5. Cerrar sesión");
            System.out.print("Seleccione opción: ");

            String op = sc.nextLine();

            switch (op) {

                case "1":
                    altaPelicula();
                    break;

                case "2":
                    altaFuncion();
                    break;

                case "3":
                    registrarEmpleado();
                    break;

                case "4":
                    reporteComprasCliente();
                    break;

                case "5":
                    System.out.println("Sesión cerrada.");
                    return;

                default:
                    System.out.println("Opción inválida.\n");
            }
        }
    }

    /* =====================================================
                      1. ALTA DE PELÍCULA
       ===================================================== */
    private static void altaPelicula() {

        System.out.println("\n--- ALTA DE PELÍCULA ---");

        System.out.print("Nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Géneros: ");
        String genero = sc.nextLine();

        System.out.print("Sinopsis: ");
        String sinopsis = sc.nextLine();

        String duracionStr;
        while (true) {
            System.out.print("Duración (HH:mm): ");
            duracionStr = sc.nextLine();
            if (duracionStr.matches("\\d{2}:\\d{2}")) break;
            System.out.println("Formato inválido. Usa HH:mm");
        }

        Cartelera.agregarPelicula(nombre, genero, sinopsis, duracionStr);
    }


    /* =====================================================
                        2. ALTA DE FUNCIÓN
       ===================================================== */
    private static void altaFuncion() {

        if (Cartelera.peliculas.isEmpty()) {
            System.out.println("No hay películas registradas.");
            return;
        }

        System.out.println("\n--- ALTA DE FUNCIÓN ---");

        // 1. Listar películas
        for (int i = 0; i < Cartelera.peliculas.size(); i++) {
            System.out.println((i + 1) + ". " + Cartelera.peliculas.get(i).getNombre());
        }

        System.out.print("Seleccione película: ");
        int idx;

        try {
            idx = Integer.parseInt(sc.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
            return;
        }

        if (idx < 0 || idx >= Cartelera.peliculas.size()) {
            System.out.println("Índice fuera de rango.");
            return;
        }

        Pelicula peli = Cartelera.peliculas.get(idx);

        // 2. Fecha
        System.out.print("Fecha (YYYY-MM-DD): ");
        LocalDate fecha = LocalDate.parse(sc.nextLine());

        // 3. Seleccionar sala
        Sala sala = seleccionarSala();
        if (sala == null) return;

        // 4. Mostrar programación existente
        System.out.println("\n--- FUNCIONES EXISTENTES EN ESA SALA Y FECHA ---");
        mostrarFuncionesExistentes(sala, fecha);

        // 5. Alta o cancelar
        while (true) {
            System.out.print("\nEscriba 'Alta' para registrar o 'Cancelar' para salir: ");
            String opcion = sc.nextLine().trim().toUpperCase();

            if (opcion.equals("CANCELAR")) return;

            if (opcion.equals("ALTA")) {

                System.out.print("Hora (00-23): ");
                int h = Integer.parseInt(sc.nextLine());
                System.out.print("Minutos (00-59): ");
                int m = Integer.parseInt(sc.nextLine());

                LocalTime hora = LocalTime.of(h, m);

                try {
                    Cartelera.agregarFuncion(peli, sala, fecha, hora);
                    System.out.println("FUNCIÓN REGISTRADA EXITOSAMENTE.");
                    return;
                } catch (HorarioOcupadoException e) {
                    System.out.println("NO SE PUEDE AGENDAR:");
                    System.out.println(e.getMessage());
                    System.out.println("Intenta otra hora o cancela.");
                }
            }
        }
    }


    private static Sala seleccionarSala() {

        System.out.println("\nSeleccione sala:");
        System.out.println("1. Sala A");
        System.out.println("2. Sala B");
        System.out.println("3. Sala VIP");
        System.out.print("Opción: ");

        String op = sc.nextLine();

        switch (op) {
            case "1": return new Sala("Sala A", "A");
            case "2": return new Sala("Sala B", "B");
            case "3": return new Sala("Sala VIP", "VIP");
            default:
                System.out.println("Opción inválida.");
                return null;
        }
    }

    private static void mostrarFuncionesExistentes(Sala sala, LocalDate fecha) {

        boolean hay = false;

        for (FuncionDeCine f : Cartelera.funciones) {
            if (f.getSala().getIdSala().equalsIgnoreCase(sala.getIdSala()) &&
                f.getFechaHora().toLocalDate().equals(fecha)) {
                System.out.println("* " + f.getFechaHora().toLocalTime() + " | ID: " + f.getIdFuncion());
                hay = true;
            }
        }

        if (!hay) System.out.println("No hay funciones registradas.");
    }


    /* =====================================================
                     3. REGISTRAR EMPLEADO
       ===================================================== */
    private static void registrarEmpleado() {

        System.out.println("\n--- REGISTRAR EMPLEADO ---");

        System.out.print("Nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Apellido paterno: ");
        String ap = sc.nextLine();

        System.out.print("Apellido materno: ");
        String am = sc.nextLine();

        System.out.print("Nickname: ");
        String nick = sc.nextLine();

        System.out.print("Contraseña: ");
        String pass = sc.nextLine();

        System.out.print("Correo: ");
        String correo = sc.nextLine();

        System.out.print("Celular: ");
        String cel = sc.nextLine();

        System.out.print("Turno (matutino/vespertino/nocturno): ");
        String turno = sc.nextLine();

        System.out.println("\nTipo de empleado:");
        System.out.println("1. Administrador");
        System.out.println("2. Vendedor de dulcería");
        System.out.print("Opción: ");
        String tipo = sc.nextLine();

        Empleado empleado;

        if (tipo.equals("1")) {

            System.out.print("¿Fin de semana o entre semana?: ");
            String categoria = sc.nextLine();

            empleado = new Administrador(
                    nombre, ap, am, nick, pass, correo, cel, turno, categoria
            );

        } else if (tipo.equals("2")) {

            System.out.print("Día de descanso: ");
            String descanso = sc.nextLine();

            empleado = new VendedorDulceria(
                    nombre, ap, am, nick, pass, correo, cel, turno, descanso
            );

        } else {
            System.out.println("Opción inválida.");
            return;
        }

        // Guardar empleado en sistema
        SistemaAutenticacion sistema = new SistemaAutenticacion();
        sistema.agregarEmpleadoDesdeAdministrador(empleado);

        System.out.println("Empleado registrado exitosamente.");
    }


    /* =====================================================
             4. VER PELÍCULAS COMPRADAS DE UN CLIENTE
       ===================================================== */
    private static void reporteComprasCliente() {

        System.out.print("Escriba nickname o parte de él: ");
        String criterio = sc.nextLine();

        Reportes.buscarClientePorNickname(criterio);
    }
}
