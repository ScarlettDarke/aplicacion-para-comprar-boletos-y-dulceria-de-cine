import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class MenuModulo2 {
    
    // Scanner estático para usarlo en todos los métodos sin pasarlo como parámetro
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // 1. Levantamos la "Base de Datos" (Listas + Archivos)
        Cartelera.inicializar();
        
        int opcion = 0;
        do {
            System.out.println("\n=== GESTIÓN DE CINE (MÓDULO 2) ===");
            System.out.println("1. Dar de Alta Película");
            System.out.println("2. Programar Función (Cartelera)");
            System.out.println("3. Ver Cartelera");
            System.out.println("4. Salir");
            System.out.print("Selecciona una opción: ");
            
            try {
                String input = sc.nextLine(); // Leemos como String para evitar bugs del buffer
                opcion = Integer.parseInt(input);
                
                switch (opcion) {
                    case 1: uiAgregarPelicula(); break;
                    case 2: uiProgramarFuncion(); break;
                    case 3: uiVerCartelera(); break;
                    case 4: System.out.println("Cerrando sistema... ¡Bye!"); break;
                    default: System.out.println("Opción no válida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingresa un número válido, por favor.");
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
            }

        } while (opcion != 4);
    }

    // --- INTERFAZ PARA AGREGAR PELÍCULA ---
    private static void uiAgregarPelicula() {
        System.out.println("\n--- NUEVA PELÍCULA ---");
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        
        System.out.print("Género: ");
        String genero = sc.nextLine();
        
        System.out.print("Sinopsis: ");
        String sinopsis = sc.nextLine();
        
        // Validación básica de formato de hora
        String duracion = "";
        while (true) {
            System.out.print("Duración (HH:mm): ");
            duracion = sc.nextLine();
            if (duracion.matches("\\d{2}:\\d{2}")) break;
            System.out.println("Formato incorrecto. Usa HH:mm (ej. 02:30)");
        }

        Cartelera.agregarPelicula(nombre, genero, sinopsis, duracion);
    }

    // --- INTERFAZ PARA PROGRAMAR FUNCIÓN ---
    private static void uiProgramarFuncion() {
        if (Cartelera.peliculas.isEmpty()) {
            System.out.println("¡No hay películas registradas! Agrega una primero.");
            return;
        }

        System.out.println("\n--- PROGRAMAR FUNCIÓN ---");
        
        // 1. Seleccionar Película
        System.out.println("Selecciona la película:");
        for (int i = 0; i < Cartelera.peliculas.size(); i++) {
            System.out.println((i + 1) + ". " + Cartelera.peliculas.get(i).toString());
        }
        
        int indicePeli = -1;
        try {
            System.out.print("Número de película: ");
            indicePeli = Integer.parseInt(sc.nextLine()) - 1;
            if (indicePeli < 0 || indicePeli >= Cartelera.peliculas.size()) {
                System.out.println("Índice inválido.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
            return;
        }
        Pelicula peliSeleccionada = Cartelera.peliculas.get(indicePeli);

        // 2. Seleccionar Sala (Simulamos las salas físicas del cine)
        System.out.println("\nSelecciona la Sala:");
        System.out.println("1. Sala 1 (Tipo A - 150 pax)");
        System.out.println("2. Sala 2 (Tipo B - 118 pax)");
        System.out.println("3. Sala VIP (Tipo VIP - 48 pax)");
        System.out.print("Opción: ");
        
        Sala salaSeleccionada = null;
        String opSala = sc.nextLine();
        
        // Aquí instanciamos la sala según la elección. 
        // En un sistema real, estas salas ya existirían en una lista, pero esto funciona para el Módulo 2.
        switch (opSala) {
            case "1": salaSeleccionada = new Sala("Sala 1", "A"); break;
            case "2": salaSeleccionada = new Sala("Sala 2", "B"); break;
            case "3": salaSeleccionada = new Sala("Sala VIP", "VIP"); break;
            default: System.out.println("Sala no válida."); return;
        }

        // 3. Fecha y Hora
        try {
            System.out.print("\nFecha (YYYY-MM-DD): "); // Formato ISO estándar
            LocalDate fecha = LocalDate.parse(sc.nextLine());

            System.out.print("Hora de inicio (HH:mm): ");
            LocalTime hora = LocalTime.parse(sc.nextLine());

            // 4. Intentar Agendar (Aquí salta la magia de tu validación)
            Cartelera.agregarFuncion(peliSeleccionada, salaSeleccionada, fecha, hora);

        } catch (DateTimeParseException e) {
            System.out.println("Error en formato de fecha/hora. Usa el formato exacto.");
        } catch (HorarioOcupadoException e) {
            System.out.println("ERROR DE HORARIO");
            System.out.println(e.getMessage());
            System.out.println("Intenta con otro horario u otra sala.");
        }
    }

    // --- VER RESUMEN ---
    private static void uiVerCartelera() {
        System.out.println("\n--- CARTELERA ACTUAL ---");
        if (Cartelera.funciones.isEmpty()) {
            System.out.println("No hay funciones programadas.");
        } else {
            for (FuncionDeCine f : Cartelera.funciones) {
                System.out.println(f.toString() + " | " + f.getFechaHora());
            }
        }
    }
}