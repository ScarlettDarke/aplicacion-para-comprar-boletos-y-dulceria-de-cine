package cine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa a un cliente del cine.
 */
public class Cliente extends Persona implements Serializable {

    private static final long serialVersionUID = 1L;

    private int edad;
    private String numeroTarjeta;

    /** Historial de compras del cliente */
    private List<CompraBoletos> historialCompras = new ArrayList<>();

    public Cliente(String nombre, String apellidoPaterno, String apellidoMaterno, int edad,
                   String nickname, String contrasena, String correo,
                   String celular, String numeroTarjeta) {

        super(nombre, apellidoPaterno, apellidoMaterno, nickname, contrasena, correo, celular);
        this.edad = edad;
        this.numeroTarjeta = numeroTarjeta;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    /** ============================
     *   HISTORIAL DE COMPRAS
     * ============================ */
    public List<CompraBoletos> getHistorialCompras() {
        return historialCompras;
    }

    public void agregarCompra(CompraBoletos compra) {
        if (compra != null) {
            historialCompras.add(compra);
        }
    }

    /** ============================
     *   NOMBRE COMPLETO
     * ============================ */
    public String getNombreCompleto() {
        return getNombre() + " " + getApellidoPaterno() + " " + getApellidoMaterno();
    }

    @Override
    public String toString() {
        return super.toString() +
                "\nEdad: " + edad +
                "\nNumero de tarjeta: " + numeroTarjeta;
    }
}
