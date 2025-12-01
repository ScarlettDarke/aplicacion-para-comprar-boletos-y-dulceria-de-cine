package cine;
import java.time.LocalDate;
import java.time.LocalTime;
public class TestModulo2 {
    public static void main(String[] args) {
        System.out.println("=== INICIANDO PRUEBAS DEL MÓDULO 2 ===");
        // 1. PRUEBA DE PERSISTENCIA
        // Al iniciar, debería cargar lo que haya en el archivo (o nada si es la primera vez)
        System.out.println("\n--- 1. Carga de Datos ---");
        Cartelera.inicializar(); 
        
        // Agregamos unas películas de prueba
        // Si corres este código 2 veces, verás que se duplican o se mantienen, 
        // demostrando que el archivo funciona.
        if (Cartelera.peliculas.isEmpty()) {
            System.out.println("Agregando películas de prueba...");
            Cartelera.agregarPelicula("Avatar 2", "Ciencia Ficción", "Pitufos gigantes", "03:10");
            Cartelera.agregarPelicula("El Padrino", "Drama", "Oferta irrechazable", "02:55");
        } else {
            System.out.println("¡Ya existen películas cargadas desde el archivo!");
        }
        // 2. PRUEBA DE SALAS (Especialmente la B que es rara)
        System.out.println("\n--- 2. Verificación de Salas ---");
        Sala salaA = new Sala("Sala 1", "A");
        Sala salaB = new Sala("Sala 2", "B"); // La problemática
        Sala salaVIP = new Sala("Sala VIP", "VIP");
        System.out.println("Capacidad Sala A (Esperado 150): " + salaA.getCapacidad());
        System.out.println("Capacidad Sala B (Esperado 118): " + salaB.getCapacidad()); 
        // Cálculo Sala B: (4 filas * 7) + (6 filas * 15) = 28 + 90 = 118.
        System.out.println("Capacidad Sala VIP (Esperado 48): " + salaVIP.getCapacidad());
        // 3. PRUEBA DE HORARIOS Y CONFLICTOS (La parte crítica)
        System.out.println("\n--- 3. Prueba de Fuego: Conflictos de Horario ---");
        
        Pelicula peli = Cartelera.peliculas.get(0); // Usamos la primera que encuentre
        LocalDate hoy = LocalDate.now();
        try {
            // A) Programamos una función a las 15:00 (3:00 PM)
            // Duración Avatar: 3h 10m -> Termina 18:10 + 30min limpieza = 18:40 libre.
            System.out.println("Intentando programar Función 1 a las 15:00...");
            Cartelera.agregarFuncion(peli, salaA, hoy, LocalTime.of(15, 0));
            
            // B) Intentamos meter otra a las 18:00 (¡ERROR! La sala sigue ocupada)
            System.out.println("Intentando programar Función 2 a las 18:00 (Debería fallar)...");
            Cartelera.agregarFuncion(peli, salaA, hoy, LocalTime.of(18, 0));
            
        } catch (HorarioOcupadoException e) {
            System.out.println("¡ÉXITO! El sistema detectó el conflicto:");
            System.err.println("  >> " + e.getMessage());
        }
        try {
            // C) Intentamos meter una a las 19:00 (Debería funcionar, ya pasó la limpieza)
            System.out.println("\nIntentando programar Función 3 a las 19:00 (Debería funcionar)...");
            Cartelera.agregarFuncion(peli, salaA, hoy, LocalTime.of(19, 0));
            
        } catch (HorarioOcupadoException e) {
            System.err.println("  >> Error inesperado: " + e.getMessage());
        }
        System.out.println("\n=== PRUEBAS FINALIZADAS ===");
    }
}