package cine;

import java.security.SecureRandom;
import java.time.LocalDateTime;

public class ProcesadorPago {

    private SecureRandom random = new SecureRandom();

    public ProcesadorPago() {}

    // Precio total de ordenes personalizadas
    public double calcularPrecioPersonalizado(double precioPalomitas, double precioRefresco, double precioNachos) {
        return precioPalomitas + precioRefresco + precioNachos;
    }

    // Genera una clave de compra con las iniciales + fecha en formato AAAAMMDD:HHMM
    public String generarClaveCompra(String nombre, String apeP, String apeM) {

        // Iniciales
        String iniciales = ("" + nombre.charAt(0) + apeP.charAt(0) + apeM.charAt(0)).toUpperCase();

        // Fecha y hora usando LocalDateTime
        LocalDateTime ahora = LocalDateTime.now();

        int anio  = ahora.getYear();
        int mes   = ahora.getMonthValue();
        int dia   = ahora.getDayOfMonth();
        int hora  = ahora.getHour();
        int min   = ahora.getMinute();

        // Formato: ABC:AAAAMMDD:HHMM
        return String.format("%s:%04d%02d%02d:%02d%02d",
                iniciales, anio, mes, dia, hora, min);
    }

    // Genera una operación aleatoria de 10 dígitos
    public String generarOperacion() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++)
            sb.append(random.nextInt(10));
        return sb.toString();
    }
}


