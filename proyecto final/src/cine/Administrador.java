package cine;

/**
 * 
 * Clase Administrador 
 * 
 * Clase que representa a un administrador del sistema.
 * 
 * @author
 */

import java.io.Serializable;

public class Administrador extends Empleado implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String administrador; 
    
    public Administrador(String nombre, String apellidoPaterno, String apellidoMaterno, String nickname, String contrasena, String correo, String celular, String administrador, String turno) {
        super(nombre, apellidoPaterno, apellidoMaterno, nickname, contrasena, correo, celular, turno);
        this.administrador = administrador;
    }
    
    public String getTipoAdministrador() { 
        return administrador; 
    }

    public void setTipoAdministrador(String administrador) { 
        this.administrador = administrador; 
    }
    
    @Override
    public String toString() {
        return super.toString() + "\nAdministrador: " + administrador;
    }
}