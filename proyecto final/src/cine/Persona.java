package cine;

/**
 * Clase Persona
 * 
 * Clase base que representa a una persona en el sistema de cine.
 * 
 * @author 
 */

import java.io.Serializable;

public abstract class Persona implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nickname;
    private String contrasena;
    private String correo;
    private String celular;
    
    public Persona(String nombre, String apellidoPaterno, String apellidoMaterno, String nickname, String contrasena, String correo, String celular) {

        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.nickname = nickname;
        this.contrasena = contrasena;
        this.correo = correo;
        this.celular = celular;

    }
    
    public String getNombre() { 
        return nombre; 
    }

    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }
    
    public String getApellidoPaterno() { 
        return apellidoPaterno; 
    }

    public void setApellidoPaterno(String apellidoPaterno) { 
        this.apellidoPaterno = apellidoPaterno; 
    }
    
    public String getApellidoMaterno() { 
        return apellidoMaterno; 
    }

    public void setApellidoMaterno(String apellidoMaterno) { 
        this.apellidoMaterno = apellidoMaterno; 
    }
    
    public String getNickname() { 
        return nickname; 
    }

    public void setNickname(String nickname) { 
        this.nickname = nickname; 
    }
    
    public String getContrasena() { 
        return contrasena; 
    }

    public void setContrasena(String contrasena) { 
        this.contrasena = contrasena; 
    }
    
    public String getCorreo() { 
        return correo; 
    }

    public void setCorreo(String correo) { 
        this.correo = correo; 
    }
    
    public String getCelular() { 
        return celular; 
    }

    public void setCelular(String celular) { 
        this.celular = celular; 
    }
    
    public boolean validarCredenciales(String nickname, String contrasena) {
        return this.nickname.equals(nickname) && this.contrasena.equals(contrasena);
    }
    
    @Override
    public String toString() {
        return "Nombre: " + nombre + " " + apellidoPaterno + " " + apellidoMaterno + "\nNickname: " + nickname +  "\nCorreo: " + correo +  "\nCelular: " + celular;
    }
}