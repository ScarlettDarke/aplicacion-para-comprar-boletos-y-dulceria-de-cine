# aplicacion-para-comprar-boletos-y-dulceria-de-cine
Sistema de gestión y venta de boletos y dulcería para cine, implementado en Java bajo el paradigma de Programación Orientada a Objetos.
Si van a realizar cambios en un codigo de otro primero avisen
la primera persona que haga commit debe subir la estructura de carpetas vacías (dentro de src/main/java/cine/) y crear el archivo CineApp.java (el main).
Ejemplo: El responsable del Módulo 1 debe crear el paquete cine.usuarios y empezar a definir la clase Persona.java dentro de él.

## Estructura del Proyecto: Módulos y Paquetes

| Módulo Asignado | Nombre del Paquete (Carpeta) | Clases Clave que Contiene |
| :-------------- | :--------------------------- | :------------------------ |
| **Módulo 1** | `cine.usuarios`              | `Persona`, `Cliente`, `Empleado`, `Administrador`, `VendedorDeDulceria`. |
| **Módulo 2** | `cine.cartelera`             | `Pelicula`, `Sala`, `FuncionDeCine`. |
| **Módulo 3** | `cine.transacciones`         | `TransaccionBancaria`, `BarraDeProgreso` (Hilos), `LógicaDeCompra`. |
| **Módulo 4** | `cine.servicio`              | `PreparaDulceria` (Hilo), `OrdenDeDulceria`, Clases de Combos y Productos (`Palomitas`, `Combo`, etc.). |
| **Común** | `cine.utilidades`            | `Autenticacion`, `ManejoArchivos` (para I/O genérico), `ExcepcionesPersonalizadas`. |

# Aplicación para Comprar Boletos y Dulcería de Cine

Este proyecto es el desarrollo de un sistema de software integral para la administración de cartelera, venta de boletos y gestión de pedidos de dulcería para una cadena de cines.

###  Objetivo del Proyecto

Aplicar y consolidar los conocimientos adquiridos en la asignatura de **Programación Orientada a Objetos (POO)** a través del desarrollo de una aplicación funcional en el lenguaje **Java**.

###  Características Principales

* **Gestión de Usuarios:** Soporte para tres roles: **Administrador**, **Vendedor de Dulcería** y **Cliente**
* **Seguridad:** Persistencia de cuentas de usuario en archivos de bytes.
* **Cartelera Dinámica:** Registro y administración de películas y funciones con validación de horario.
* **Transacciones Concurrentes:** Utilización de **hilos** para simular la transacción bancaria y la barra de progreso durante la compra de boletos y dulcería.
* **Servicio Concurrente:** Gestión de pedidos de dulcería mediante un hilo de servicio asignado a un `VendedorDeDulceria`.
* **Modelado POO Avanzado:** Implementación de herencia, composición, polimorfismo, atributos privados (encapsulamiento), y manejo de excepciones y archivos.


###  Estructura de Colaboración

El desarrollo se ha dividido en cuatro módulos principales para asegurar una distribución equitativa de la carga de trabajo y facilitar la integración:

1.  **Módulo 1:** Gestión de Usuarios y Autenticación.
2.  **Módulo 2:** Gestión de Películas y Funciones.
3.  **Módulo 3:** Compra de Boletos y Dulcería.
4.  **Módulo 4:** Notificaciones, Empleados y Reportes.

---

###  Cómo Contribuir

1.  Clona el repositorio.
2.  Trabaja dentro del paquete o directorio asignado a tu módulo.
3.  [cite_start]Asegúrate de documentar tu código con **Javadoc**.
4.  Realiza *commits* con mensajes claros que describan la funcionalidad implementada.
5.  Mantente sincronizado con la rama principal (`main`/`master`) para minimizar conflictos de integración.
