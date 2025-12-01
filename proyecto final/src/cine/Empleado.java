package cine;

/**
 * 
 * Clase Empleado
 * 
 * Clase abstracta que representa a un empleado del cine.
 * 
 * @author
 */

import java.io.Serializable;

public abstract class Empleado extends Persona implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String turno; 
    
    public Empleado(String nombre, String apellidoPaterno, String apellidoMaterno, String nickname, String contrasena, String correo, String celular, String turno) {
        super(nombre, apellidoPaterno, apellidoMaterno, nickname, contrasena, correo, celular);
        this.turno = turno;
    }
    
    public String getTurno() { 
        return turno; 
    }

    public void setTurno(String turno) { 
        this.turno = turno; 
    }
    
    @Override
    public String toString() {
        return super.toString() + "\nTurno: " + turno;
    }
    
}