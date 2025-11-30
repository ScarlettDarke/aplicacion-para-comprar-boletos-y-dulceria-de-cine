package cine;

import java.security.SecureRandom;

public class TransaccionBancaria extends Thread {

    private boolean aprobada;   // Resultado de la transacción
    private boolean finalizada; // Indica si ya terminó
    private static final SecureRandom numAleatorio = new SecureRandom();

    public TransaccionBancaria() {
        this.aprobada = false;
        this.finalizada = false;
    }

    @Override
    public void run() {
        try {
            // Tiempo entre 2s y 5s 
            int tiempo = 2000 + numAleatorio.nextInt(3000); 

            Thread.sleep(tiempo);

            // Resultado: 97% éxito, 3% fallo
            int exitoTransaccion = numAleatorio.nextInt(100);
            this.aprobada = exitoTransaccion < 97;

        } catch (InterruptedException e) {
            this.aprobada = false;
        }

        this.finalizada = true;
    }

    public boolean isAprobada() {
        return aprobada;
    }

    public boolean isFinalizada() {
        return finalizada;
    }
}
