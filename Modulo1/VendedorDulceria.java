package cine;

/**
 * 
 * Clase Dulceria.
 * Clase que representa a un vendedor de dulcería.
 * 
 * @author 
 */

import java.io.Serializable;

public class VendedorDulceria extends Empleado implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String descanso;
    
    public VendedorDulceria(String nombre, String apellidoPaterno, String apellidoMaterno, String nickname, String contrasena, String correo, String celular, String turno, String descanso) {
        super(nombre, apellidoPaterno, apellidoMaterno, nickname, contrasena, correo, celular, turno);
        this.descanso = descanso;
    }
    
    public String getDiaDescanso() { 
        return descanso; 
    }

    public void setDiaDescanso(String descanso) { 
        this.descanso = descanso; 
    }
    
    @Override
    public String toString() {
        return super.toString() + "\nDescanso: " + descanso;
    }
}