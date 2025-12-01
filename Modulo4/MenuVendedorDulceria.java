package cine;

import java.util.Scanner;

public class MenuVendedorDulceria {

    public static void mostrarMenu(VendedorDulceria vendedor) {

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== MENÚ VENDEDOR DE DULCERÍA ===");
            System.out.println("1. Ver historial de órdenes atendidas");
            System.out.println("2. Cerrar sesión");
            System.out.print("Seleccione opción: ");

            String opcion = sc.nextLine();

            switch (opcion) {
                case "1":
                    System.out.println("\n--- HISTORIAL DE ÓRDENES ---");
                    Reportes.mostrarHistorialVendedor(vendedor.getNickname());
                    break;

                case "2":
                    System.out.println("Sesión cerrada.");
                    return;

                default:
                    System.out.println("Opción inválida.");
            }
        }
    }
}
