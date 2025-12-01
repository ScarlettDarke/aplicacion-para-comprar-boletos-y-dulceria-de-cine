package cine;

import java.util.Scanner;

public class MenuCliente {

    public static void mostrarMenu(Cliente cliente) {

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== MENÚ CLIENTE ===");
            System.out.println("1. Comprar boletos");
            System.out.println("2. Comprar dulcería");
            System.out.println("3. Revisar notificaciones de dulcería");
            System.out.println("4. Cerrar sesión");
            System.out.print("Seleccione opción: ");

            String opcion = sc.nextLine();

            switch (opcion) {

                case "3":
                    System.out.print("Ingresa tu clave de compra: ");
                    String clave = sc.nextLine();
                    String mensaje = GestorArchivos.leerNotificacion(clave);
                    System.out.println("\n--- NOTIFICACIÓN ---");
                    System.out.println(mensaje);
                    break;

                case "4":
                    System.out.println("Sesión cerrada.");
                    return;

                default:
                    System.out.println("Función no disponible.");
            }
        }
    }
}
