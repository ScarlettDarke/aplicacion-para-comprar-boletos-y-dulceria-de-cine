package cine;

public class BarraProgreso extends Thread {
    private int velocidad; // ms entre pasos
    private int tamaño;    // cantidad de pasos

    public BarraProgreso(int velocidad, int tamaño) {
        this.velocidad = velocidad;
        this.tamaño = tamaño;
    }

    @Override
    public void run() {
        for (int i = 0; i <= tamaño; i++) {
            int porcentaje = i * 100 / tamaño;
            System.out.print(" [");
            for (int j = 0; j < i; j++) System.out.print("=");
            for (int j = i; j < tamaño; j++) System.out.print(" ");
            System.out.print("] " + porcentaje + "%");
            try { Thread.sleep(velocidad); } catch (InterruptedException e) {}
        }
        System.out.println("\n¡Pago completado!");
    }
}
