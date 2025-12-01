package cine;

import java.util.Scanner;

public class MenuDulceria {

    private Scanner sc = new Scanner(System.in);
    private ControladorDulceria controlador = new ControladorDulceria();

    public void mostrarMenu(Cliente cliente) {

        boolean repetir = true;

        while (repetir) {

            System.out.println("========= MENÚ DULCERÍA =========");
            System.out.println("A. Combo \"amix\" (palomitas + 2 refrescos jumbo)");
            System.out.println("B. Combo \"nachos\" (palomitas + 2 refrescos jumbo + nachos jumbo)");
            System.out.println("C. Combo \"buen trío\" (palomitas + 3 refrescos jumbo + nachos mega)");
            System.out.println("D. Combo \"qué me ves\" (palomitas + refresco + nachos jumbo)");
            System.out.println("E. Orden personalizada");
            System.out.println("F. Cancelar compra");
            System.out.print("Elige una opción: ");

            String op = sc.nextLine().trim().toUpperCase();

            switch (op) {

                case "A":
                case "B":
                case "C":
                case "D":
                    // Para combos, creamos una orden vacía y el controlador la llenará
                    OrdenDulceria ordenCombo = new OrdenDulceria(0, 0, 0, 0, 0, 0);
                    controlador.procesarCombo(ordenCombo, op);
                    repetir = false;
                    break;

                case "E":
                    // SOLUCIÓN: Solo llamamos al método sin parámetros
                    // El controlador se encarga de todo el proceso
                    controlador.procesarPersonalizada();
                    repetir = false;
                    break;

                case "F":
                    System.out.println("Compra cancelada.");
                    repetir = false;
                    break;

                default:
                    System.out.println("Opción inválida, intenta de nuevo.\n");
            }
        }
    }
}