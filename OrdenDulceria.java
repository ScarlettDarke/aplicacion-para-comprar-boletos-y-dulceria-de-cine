package cine;

import java.util.ArrayList;

public class OrdenDulceria {

    // Cantidades
    private int palomitas;
    private int refrescos;
    private int nachos;

    // Precios por producto
    private double precioPalomitas;
    private double precioRefrescos;
    private double precioNachos;

    // Lista de productos adicionales (sabores, tamaños, extras)
    private ArrayList<String> productos;

    // Clave de compra generada
    private String claveCompra;

    // Descuento aplicado (10%)
    private static final double DESCUENTO = 0.10;

    // Constructor para combos
    public OrdenDulceria(int pal, int ref, int nac, double precioP, double precioR, double precioN) {
        this.palomitas = pal;
        this.refrescos = ref;
        this.nachos = nac;
        this.precioPalomitas = precioP;
        this.precioRefrescos = precioR;
        this.precioNachos = precioN;
        this.productos = new ArrayList<>();
        this.claveCompra = "";
    }

    // Agregar producto adicional (por ejemplo, sabor o tamaño)
    public void agregarProducto(String nombre, double precio) {
        productos.add(nombre + (precio > 0 ? " ($" + precio + ")" : ""));
        // Si agregamos un producto con precio, sumamos al total correspondiente
        if (nombre.toLowerCase().contains("palomitas")) precioPalomitas += precio;
        if (nombre.toLowerCase().contains("refresco")) precioRefrescos += precio;
        if (nombre.toLowerCase().contains("nachos")) precioNachos += precio;
    }

    // Aplicar descuento del 10% a los productos principales
    public void aplicarDescuento() {
        precioPalomitas *= (1 - DESCUENTO);
        precioRefrescos *= (1 - DESCUENTO);
        precioNachos *= (1 - DESCUENTO);
    }

    // Generar clave de compra
    public void setClaveCompra(String clave) {
        this.claveCompra = clave;
    }

    // Resumen de la orden
    public String resumenOrden() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RESUMEN DE ORDEN ===\n");
        sb.append("Palomitas: ").append(palomitas).append(" ($").append(String.format("%.2f", precioPalomitas)).append(")\n");
        sb.append("Refrescos: ").append(refrescos).append(" ($").append(String.format("%.2f", precioRefrescos)).append(")\n");
        sb.append("Nachos: ").append(nachos).append(" ($").append(String.format("%.2f", precioNachos)).append(")\n");

        if (!productos.isEmpty()) {
            sb.append("Productos adicionales:\n");
            for (String p : productos) {
                sb.append(" - ").append(p).append("\n");
            }
        }

        double total = precioPalomitas + precioRefrescos + precioNachos;
        sb.append("TOTAL: $").append(String.format("%.2f", total)).append("\n");
        sb.append("Clave de compra: ").append(claveCompra).append("\n");

        return sb.toString();
    }

    // Métodos para aumentar cantidades (por ejemplo, combos)
    public void agregarPalomitas(int cantidad, double precioUnitario) {
        this.palomitas += cantidad;
        this.precioPalomitas += precioUnitario * cantidad;
    }

    public void agregarRefrescos(int cantidad, double precioUnitario) {
        this.refrescos += cantidad;
        this.precioRefrescos += precioUnitario * cantidad;
    }

    public void agregarNachos(int cantidad, double precioUnitario) {
        this.nachos += cantidad;
        this.precioNachos += precioUnitario * cantidad;
    }
}
