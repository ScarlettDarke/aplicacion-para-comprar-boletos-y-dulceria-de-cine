package cine;

/**
 * 
 * Clase Cliente
 * 
 * Clase que representa a un cliente del cine.
 * 
 * @author 
 */

import java.io.Serializable;

public class Cliente extends Persona implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private int edad;
    private String numeroTarjeta;
    
    public Cliente(String nombre, String apellidoPaterno, String apellidoMaterno, int edad, String nickname, String contrasena, String correo, 
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
    
    @Override
    public String toString() {
        return super.toString() + "\nEdad: " + edad + "\nNumero de tarjeta: " + numeroTarjeta;
    }
}