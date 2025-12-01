package cine;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Módulo completo para:
 * ➤ Dar de alta películas
 * ➤ Programar funciones (cumpliendo EXACTAMENTE el PDF)
 * ➤ Consultar cartelera
 */
public class MenuModulo2 {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        Cartelera.inicializar();

        int opcion;

        do {
            System.out.println("\n=== GESTIÓN DE CINE (MÓDULO 2) ===");
            System.out.println("1. Dar de Alta Película");
            System.out.println("2. Programar Función");
            System.out.println("3. Ver Cartelera");
            System.out.println("4. Salir");
            System.out.print("Seleccione opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());

                switch (opcion) {
                    case 1:
                        uiAgregarPelicula();
                        break;
                    case 2:
                        uiProgramarFuncion();
                        break;
                    case 3:
                        uiVerCartelera();
                        break;
                    case 4:
                        System.out.println("Cerrando sistema…");
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Ingresa solo números.");
                opcion = 0;
            }

        } while (opcion != 4);
    }

    /* ────────────────────────────────────────────────
     * ALTA DE PELÍCULA
     * ────────────────────────────────────────────────
     */
    private static void uiAgregarPelicula() {

        System.out.println("\n--- NUEVA PELÍCULA ---");

        System.out.print("Título: ");
        String nombre = sc.nextLine();

        System.out.print("Género: ");
        String genero = sc.nextLine();

        System.out.print("Sinopsis: ");
        String sinopsis = sc.nextLine();

        String duracion;
        while (true) {
            System.out.print("Duración (HH:mm): ");
            duracion = sc.nextLine();
            if (duracion.matches("\\d{2}:\\d{2}")) break;
            System.out.println("Formato incorrecto.");
        }

        Cartelera.agregarPelicula(nombre, genero, sinopsis, duracion);
    }

    /* ────────────────────────────────────────────────
     * PROGRAMAR FUNCIÓN (CUMPLE 100% EL PDF)
     * ────────────────────────────────────────────────
     */
    private static void uiProgramarFuncion() {

        if (Cartelera.peliculas.isEmpty()) {
            System.out.println("No hay películas registradas.");
            return;
        }

        System.out.println("\n--- PROGRAMAR FUNCIÓN ---");

        // 1. Elegir película
        System.out.println("Selecciona la película:");
        for (int i = 0; i < Cartelera.peliculas.size(); i++) {
            System.out.println((i + 1) + ". " + Cartelera.peliculas.get(i).getNombre());
        }

        int idx = -1;
        try {
            System.out.print("Opción: ");
            idx = Integer.parseInt(sc.nextLine()) - 1;
        } catch (Exception e) {
            System.out.println("Entrada inválida.");
            return;
        }

        if (idx < 0 || idx >= Cartelera.peliculas.size()) {
            System.out.println("Película inválida.");
            return;
        }

        Pelicula pelicula = Cartelera.peliculas.get(idx);

        // 2. Fecha
        LocalDate fecha;
        try {
            System.out.print("Fecha (AAAA-MM-DD): ");
            fecha = LocalDate.parse(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Fecha inválida.");
            return;
        }

        // 3. Elegir sala REAL (A, B, VIP)
        System.out.println("\nSeleccione sala:");
        System.out.println("1. Sala A");
        System.out.println("2. Sala B");
        System.out.println("3. Sala VIP");
        System.out.print("Opción: ");

        Sala sala;
        String opSala = sc.nextLine();

        switch (opSala) {
            case "1": sala = new Sala("Sala A", "A"); break;
            case "2": sala = new Sala("Sala B", "B"); break;
            case "3": sala = new Sala("Sala VIP", "VIP"); break;
            default:
                System.out.println("Sala inválida.");
                return;
        }

        // 4. Mostrar funciones existentes para esa sala y fecha
        System.out.println("\nFunciones ya programadas en " + sala.getIdSala() +
                           " para " + fecha + ":");

        boolean hayFunciones = false;

        for (FuncionDeCine f : Cartelera.funciones) {
            if (f.getSala().getIdSala().equalsIgnoreCase(sala.getIdSala())
                && f.getFechaHora().toLocalDate().equals(fecha)) {

                hayFunciones = true;
                System.out.println(" • " + f.getFechaHora().toLocalTime() +
                                   "  |  " + f.getPelicula().getNombre());
            }
        }

        if (!hayFunciones) {
            System.out.println(" -- No hay funciones registradas --");
        }

        // 5. Ciclo Alta / Cancelar (requisito del PDF)
        while (true) {

            System.out.print("\nEscriba 'Alta' para registrar o 'Cancelar' para abortar: ");
            String op = sc.nextLine().trim().toUpperCase();

            if (op.equals("CANCELAR")) {
                System.out.println("Operación cancelada.");
                return;
            }

            if (!op.equals("ALTA")) {
                System.out.println("Opción inválida.");
                continue;
            }

            // 6. Pedir hora y validar
            LocalTime hora;
            try {
                System.out.print("Hora inicio (HH:mm): ");
                hora = LocalTime.parse(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Formato incorrecto.");
                continue;
            }

            try {
                Cartelera.agregarFuncion(pelicula, sala, fecha, hora);
                System.out.println("Función registrada correctamente.");
                return;
            } catch (HorarioOcupadoException e) {
                System.out.println("\nNO SE PUEDE REGISTRAR LA FUNCIÓN");
                System.out.println(e.getMessage());
                System.out.println("La sala requiere al menos 30 minutos de separación.");
                // se repite el ciclo Alta/Cancelar
            }
        }
    }

    /* ────────────────────────────────────────────────
     * VER CARTELERA COMPLETA
     * ────────────────────────────────────────────────
     */
    private static void uiVerCartelera() {

        System.out.println("\n--- CARTELERA ACTUAL ---");

        if (Cartelera.funciones.isEmpty()) {
            System.out.println("No hay funciones registradas.");
            return;
        }

        for (FuncionDeCine f : Cartelera.funciones) {
            System.out.println(
                f.getPelicula().getNombre() + " | " +
                f.getSala().getIdSala() + " | " +
                f.getFechaHora().toLocalDate() + " " +
                f.getFechaHora().toLocalTime() + " | " +
                "ID: " + f.getIdFuncion()
            );
        }
    }
}
