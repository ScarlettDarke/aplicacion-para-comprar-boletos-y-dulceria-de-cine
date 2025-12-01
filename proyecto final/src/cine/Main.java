package cine;

public class Main {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("         SISTEMA DE CINE");
        System.out.println("====================================");

        try {
            // 1. Inicializar carpetas y archivos base
            System.out.println("\n> Inicializando sistema de archivos...");
            GestorArchivos.inicializarSistema();

            // 2. Cargar cartelera (Películas)
            System.out.println("> Cargando cartelera...");
            Cartelera.inicializar();

            // 3. Inicializar autenticación
            System.out.println("> Iniciando módulo de autenticación...");
            SistemaAutenticacion sistema = new SistemaAutenticacion();

            // 4. Iniciar pantalla principal
            System.out.println("\n> Sistema listo. Bienvenido.");
            sistema.mostrarPantallaInicial();

        } catch (Exception e) {
            System.out.println("\nERROR CRÍTICO EN MAIN:");
            e.printStackTrace();
        }

        System.out.println("\nSistema finalizado.");
    }
}
