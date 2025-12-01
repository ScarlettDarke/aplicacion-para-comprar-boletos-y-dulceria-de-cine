package cine;

import java.io.Serializable;
import java.util.List;

/**
 * Representa una compra completa de boletos
 * hecha por un cliente en una sola operación.
 */
public class CompraBoletos implements Serializable {

    private static final long serialVersionUID = 1L;

    private FuncionDeCine funcion;
    private List<Boleto> boletos;

    public CompraBoletos(FuncionDeCine funcion, List<Boleto> boletos) {
        this.funcion = funcion;
        this.boletos = boletos;
    }

    public FuncionDeCine getFuncion() {
        return funcion;
    }

    public List<Boleto> getBoletos() {
        return boletos;
    }
}
