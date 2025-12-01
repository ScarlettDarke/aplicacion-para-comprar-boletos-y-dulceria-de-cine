package cine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Representa una orden de compra en dulcería.
 * Maneja cantidades, precios, clave de compra y fecha.
 */
public class OrdenDulceria {

    // Cantidades
    private int palomitas;
    private int refrescos;
    private int nachos;

    // Precios unitarios (totales por cada producto en la orden)
    private double precioPalomitas;
    private double precioRefrescos;
    private double precioNachos;

    // Productos adicionales (texto descriptivo)
    private ArrayList<String> productos;

    // Fecha y clave
    private LocalDateTime fechaCompra;
    private String claveCompra;

    private static final DateTimeFormatter TF = DateTimeFormatter.ofPattern("yyyyMMdd:HHmm");

    // Descuento por política (si aplica)
    private static final double DESCUENTO = 0.10;

    /**
     * Constructor principal.
     *
     * @param pal número de porciones de palomitas
     * @param ref número de refrescos
     * @param nac número de nachos
     * @param precioP precio total de palomitas (sumado)
     * @param precioR precio total de refrescos (sumado)
     * @param precioN precio total de nachos (sumado)
     */
    public OrdenDulceria(int pal, int ref, int nac,double precioP, double precioR, double precioN) {
        this.palomitas = pal;
        this.refrescos = ref;
        this.nachos = nac;

        this.precioPalomitas = precioP;
        this.precioRefrescos = precioR;
        this.precioNachos = precioN;

        this.productos = new ArrayList<>();
        this.fechaCompra = LocalDateTime.now();
        this.claveCompra = ""; // puede setearse externamente o auto-generarse
    }

    /**
     * Agrega un producto extra (sabor/tamaño u otro) y opcionalmente ajusta precios.
     *
     * @param nombre nombre del extra
     * @param precio importe adicional (0 si solo es descripción)
     */
    public void agregarProducto(String nombre, double precio) {
        this.productos.add(nombre + (precio > 0 ? " ($" + String.format("%.2f", precio) + ")" : ""));
        // No alteramos cantidades por defecto; el controlador deberá sumar si es necesario.
        if (nombre.toLowerCase().contains("palomita")) this.precioPalomitas += precio;
        if (nombre.toLowerCase().contains("refresco")) this.precioRefrescos += precio;
        if (nombre.toLowerCase().contains("nacho")) this.precioNachos += precio;
    }

    /**
     * Aplica un descuento general (ej. 10%) sobre los precios totales — usado por combos.
     */
    public void aplicarDescuento() {
        this.precioPalomitas *= (1 - DESCUENTO);
        this.precioRefrescos *= (1 - DESCUENTO);
        this.precioNachos *= (1 - DESCUENTO);
    }

    /**
     * Calcula el total actual de la orden.
     * @return total (double)
     */
    public double calcularTotal() {
        return precioPalomitas + precioRefrescos + precioNachos;
    }

    /** Genera una clave simple por fecha; puede override si se necesita otro formato. */
    public String generarClaveAutomatica() {
        return "ORD-" + fechaCompra.format(TF);
    }

    public void setClaveCompra(String clave) {
        this.claveCompra = clave;
    }

    public String getClaveCompra() {
        return this.claveCompra;
    }

    public LocalDateTime getFechaCompra() {
        return this.fechaCompra;
    }

    /**
     * Tipo simple de orden para el módulo de historial/notificaciones.
     * @return "Combo / Estándar" o "Personalizada"
     */
    public String getTipoOrden() {
        if (this.productos.isEmpty()) return "Combo / Estándar";
        return "Personalizada";
    }

    /**
     * Resumen amigable de la orden para mostrar al cliente.
     * @return cadena con resumen
     */
    public String resumenOrden() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RESUMEN DE ORDEN ===\n");
        sb.append("Fecha: ").append(fechaCompra.format(TF)).append("\n");
        sb.append("Palomitas: ").append(palomitas).append("  Precio: $").append(String.format("%.2f", precioPalomitas)).append("\n");
        sb.append("Refrescos: ").append(refrescos).append("  Precio: $").append(String.format("%.2f", precioRefrescos)).append("\n");
        sb.append("Nachos: ").append(nachos).append("  Precio: $").append(String.format("%.2f", precioNachos)).append("\n");

        if (!productos.isEmpty()) {
            sb.append("Extras:\n");
            for (String s : productos) sb.append(" - ").append(s).append("\n");
        }

        sb.append("TOTAL: $").append(String.format("%.2f", calcularTotal())).append("\n");
        sb.append("Clave de compra: ").append(claveCompra.isEmpty() ? generarClaveAutomatica() : claveCompra).append("\n");

        return sb.toString();
    }

    // Getters útiles
    public int getPalomitas() { return palomitas; }
    public int getRefrescos() { return refrescos; }
    public int getNachos() { return nachos; }
    public double getPrecioPalomitas() { return precioPalomitas; }
    public double getPrecioRefrescos() { return precioRefrescos; }
    public double getPrecioNachos() { return precioNachos; }
    public ArrayList<String> getProductos() { return productos; }
}
