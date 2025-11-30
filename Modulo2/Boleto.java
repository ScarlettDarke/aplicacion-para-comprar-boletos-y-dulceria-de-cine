/**
 * Clase que representa un boleto de acceso.
 * Gestiona la información relacionada con el asiento, precio y tipo de cliente.
 */
public class Boleto {
    
    /** Identificador único del boleto (actualmente no inicializado). */
    private String idBoleto; 
    
    /** Costo monetario del boleto. */
    private double precio;
    
    /** Categoría del cliente (ej. Adulto, Niño, Tercera Edad). */
    private String tipoCliente; // Adulto, Niño, Tercera Edad
    
    /** Código o número del asiento asignado al boleto. */
    private String asientoAsignado;
    /**
     * Constructor para inicializar una nueva instancia de Boleto con los datos requeridos.
     *
     * @param asientoAsignado El identificador del asiento reservado para este boleto.
     * @param precio El valor monetario del boleto.
     * @param tipoCliente La categoría a la que pertenece el cliente (Adulto, Niño, etc.).
     */
    public Boleto(String asientoAsignado, double precio, String tipoCliente) {
        this.asientoAsignado = asientoAsignado;
        this.precio = precio;
        this.tipoCliente = tipoCliente;
    }
    // Getters
    /**
     * Obtiene el identificador del asiento asignado.
     *
     * @return Una cadena de texto con el asiento.
     */
    public String getAsientoAsignado() { return asientoAsignado; }
    /**
     * Obtiene el costo del boleto.
     *
     * @return El precio del boleto como un valor de tipo double.
     */
    public double getPrecio() { return precio; }
}