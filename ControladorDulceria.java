package cine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ControladorDulceria {

    private Scanner sc = new Scanner(System.in);
    private ProcesadorPago procesadorPago = new ProcesadorPago();

    private double leerPrecio(String nombreArchivo) {
        try {
            File f = new File("precios/" + nombreArchivo);
            Scanner lector = new Scanner(f);
            double precio = lector.nextDouble();
            lector.close();
            return precio;
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: No se encontró " + nombreArchivo);
            return 0;
        }
    }

    /** Procesar los combos A-D */
    public void procesarCombo(OrdenDulceria orden, String tipo) {
        System.out.println("\nHas elegido el combo " + tipo);

        // Sabor
        System.out.print("Sabor de palomitas \n(mantequilla / queso / jalapeño): ");
        String saborP = sc.nextLine();
        System.out.print("Sabor de refrescos (cola / light / naranja / manzana / toronja): ");
        String saborR = sc.nextLine();

        // Cantidades y tamaños según combo
        int pal = 1, ref = 0, nac = 0;
        String archivoP = "", archivoR = "", archivoN = "";

        switch(tipo) {
            case "A": 
                ref = 2; 
                archivoP = "palomitas_jumbo.txt"; 
                archivoR = "refresco_jumbo.txt"; 
                break;
            case "B": 
                ref = 2; nac = 1; 
                archivoP = "palomitas_jumbo.txt"; 
                archivoR = "refresco_jumbo.txt"; 
                archivoN = "nachos_jumbo.txt"; 
                break;
            case "C": 
                ref = 3; nac = 1; 
                archivoP = "palomitas_jumbo.txt"; 
                archivoR = "refresco_jumbo.txt"; 
                archivoN = "nachos_mega.txt"; 
                break;
            case "D": 
                ref = 1; nac = 1; 
                archivoP = "palomitas_jumbo.txt"; 
                archivoR = "refresco_jumbo.txt"; 
                archivoN = "nachos_jumbo.txt"; 
                break;
        }

        double precioP = leerPrecio(archivoP);
        double precioR = leerPrecio(archivoR);
        double precioN = (archivoN.isEmpty()) ? 0 : leerPrecio(archivoN);

        OrdenDulceria combo = new OrdenDulceria(pal, ref, nac, precioP * pal, precioR * ref, precioN * nac);
        combo.agregarProducto("Sabor palomitas: " + saborP, 0);
        combo.agregarProducto("Sabor refrescos: " + saborR, 0);

        combo.aplicarDescuento(); // Descuento 10%
        finalizarOrden(combo);
    }

    /** Procesar la orden personalizada - VERSIÓN CORREGIDA */
    public void procesarPersonalizada() {
        System.out.println("\n=== ORDEN PERSONALIZADA ===");

        int pal = 0, ref = 0, nac = 0;
        double precioPal = 0, precioRef = 0, precioNac = 0;
        String saborPalomitas = "", saborRefresco = "", saborNachos = "";

        // ------------------ PALOMITAS ------------------
        System.out.print("¿Desea palomitas? (s/n): ");
        String respuesta = sc.nextLine().trim();
        if(respuesta.equalsIgnoreCase("s")) {
            System.out.println("Tamaño: \n1) Med \n2) Gde \n3) Jumbo \n4) Mega");
            System.out.print("Seleccione opción: ");
            String t = sc.nextLine().trim();
            
            switch(t) {
                case "1": 
                    precioPal = leerPrecio("palomitas_med.txt"); 
                    break;
                case "2": 
                    precioPal = leerPrecio("palomitas_gde.txt"); 
                    break;
                case "3": 
                    precioPal = leerPrecio("palomitas_jumbo.txt"); 
                    break;
                case "4": 
                    precioPal = leerPrecio("palomitas_mega.txt"); 
                    break;
                default:
                    System.out.println("Opción inválida, se asignará tamaño Mediano");
                    precioPal = leerPrecio("palomitas_med.txt");
            }
            pal = 1;

            System.out.print("Sabor de palomitas \n(mantequilla / queso / jalapeño): ");
            saborPalomitas = sc.nextLine().trim();
        }

        // ------------------ REFRESCO ------------------
        System.out.print("¿Desea refresco? (s/n): ");
        respuesta = sc.nextLine().trim();
        if(respuesta.equalsIgnoreCase("s")) {
            System.out.println("Tamaño: \n1) Med \n2) Gde \n3) Jumbo \n4) Mega");
            System.out.print("Seleccione opción: ");
            String t = sc.nextLine().trim();
            
            switch(t) {
                case "1": 
                    precioRef = leerPrecio("refresco_med.txt"); 
                    break;
                case "2": 
                    precioRef = leerPrecio("refresco_gde.txt"); 
                    break;
                case "3": 
                    precioRef = leerPrecio("refresco_jumbo.txt"); 
                    break;
                case "4": 
                    precioRef = leerPrecio("refresco_mega.txt"); 
                    break;
                default:
                    System.out.println("Opción inválida, se asignará tamaño Mediano");
                    precioRef = leerPrecio("refresco_med.txt");
            }
            ref = 1;

            System.out.print("Sabor de refresco \n(cola / light / naranja / manzana / toronja): ");
            saborRefresco = sc.nextLine().trim();
        }

        // ------------------ NACHOS ------------------
        System.out.print("¿Desea nachos? (s/n): ");
        respuesta = sc.nextLine().trim();
        if(respuesta.equalsIgnoreCase("s")) {
            System.out.println("Tamaño: \n1) Personal \n2) Jumbo \n3) Mega");
            System.out.print("Seleccione opción: ");
            String t = sc.nextLine().trim();
            
            switch(t) {
                case "1": 
                    precioNac = leerPrecio("nachos_personal.txt"); 
                    break;
                case "2": 
                    precioNac = leerPrecio("nachos_jumbo.txt"); 
                    break;
                case "3": 
                    precioNac = leerPrecio("nachos_mega.txt"); 
                    break;
                default:
                    System.out.println("Opción inválida, se asignará tamaño Personal");
                    precioNac = leerPrecio("nachos_personal.txt");
            }
            nac = 1;

            System.out.print("Sabor de nachos \n(queso / nacho / jalapeño): ");
            saborNachos = sc.nextLine().trim();
        }

        // Crear la orden final
        OrdenDulceria ordenFinal = new OrdenDulceria(pal, ref, nac, precioPal, precioRef, precioNac);
        
        // Agregar sabores como productos adicionales
        if(!saborPalomitas.isEmpty()) {
            ordenFinal.agregarProducto("Sabor palomitas: " + saborPalomitas, 0);
        }
        if(!saborRefresco.isEmpty()) {
            ordenFinal.agregarProducto("Sabor refresco: " + saborRefresco, 0);
        }
        if(!saborNachos.isEmpty()) {
            ordenFinal.agregarProducto("Sabor nachos: " + saborNachos, 0);
        }
        
        finalizarOrden(ordenFinal);
    }

    /** Finalizar la orden */
    private void finalizarOrden(OrdenDulceria orden) {
        System.out.println("\nProcesando pago...");
        TransaccionBancaria t = new TransaccionBancaria();
        BarraProgreso b = new BarraProgreso(200, 20);
        t.start();
        b.start();
        try { 
            t.join(); 
            b.join(); 
        } catch (InterruptedException e) {}

        orden.setClaveCompra(procesadorPago.generarClaveCompra("Cliente","Cine","XD"));
        System.out.println("\n" + orden.resumenOrden());
        System.out.println("Revisa la sección de notificaciones para saber cuando tu orden esté lista 😉");
        System.out.println("Presiona Enter para regresar al menú...");
        sc.nextLine();
    }
}