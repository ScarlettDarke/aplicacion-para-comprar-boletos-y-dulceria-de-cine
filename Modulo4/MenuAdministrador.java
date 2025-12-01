package cine;

import java.util.Scanner;

public class MenuAdministrador {

    /**
     * Muestra el menú principal del administrador.
     * Recibe el objeto Administrador para futuras funciones (por ejemplo, auditoría).
     */
    public static void mostrarMenu(Administrador admin) {

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== MENÚ ADMINISTRADOR ===");
            System.out.println("1. Buscar cliente por nickname");
            System.out.println("2. Salir");
            System.out.print("Seleccione opción: ");

            String opcion = sc.nextLine();

            switch (opcion) {

                case "1":
                    System.out.print("Ingrese nickname total o parcial: ");
                    String nick = sc.nextLine();
                    Reportes.buscarClientePorNickname(nick);
                    break;

                case "2":
                    System.out.println("Saliendo del panel administrador.");
                    return;

                default:
                    System.out.println("Opción inválida.");
            }
        }
    }
}
