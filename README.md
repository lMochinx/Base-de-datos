# Motor de Base de Datos.

Este repositorio contiene la implementación de un motor de base de datos en memoria desarrollado en Java. El sistema utiliza el patrón arquitectónico Modelo-Vista-Controlador (MVC) y basa su indexación principal en un **Árbol AVL auto-balanceable**, lo que garantiza un rendimiento de $O(\log n)$ en las operaciones de búsqueda, inserción y eliminación.

Para la gestión de datos duplicados el algoritmo del árbol esta diseñado para enviar los valores repetidos al sub arbol izquierdo de manera estricta.

## Arquitectura del proyecto.

El código está estructurado en el paquete `Base-de-datos`:
* **Controller:** Contiene la clase `Main` (punto de entrada) y `Controller` (orquestador).
* **Model:** Contiene la lógica de almacenamiento (`Tabla`, `Fila`, `Campo`) y el índice (`ArbolAVL`, `NodoAVL`).
    * **Persistencia:** Submódulo que gestiona la lectura y carga masiva de datos en formato JSON mediante `JsonReader`.
* **View:** Interfaces de usuario de línea de comandos (`Consola`, `MenuAVL`).

## Requisitos previos

* Java Development Kit (JDK) 8 o superior.
* Apache NetBeans IDE o Eclipse IDE (Recomendado para la ejecución proyecto).

## Instalación y Ejecución.

1.  Copia el URL y mantenlo en el portapapeles:
    ```bash
    https://github.com/lMochinx/Base-de-datos.git
    ```
2.  Abre el IDE  y selecciona **File > Import > Git > Projects from Git > Clone URL**. pega el URL y dale a Finish.
3.  Navega hasta el paquete `udistrital.Controller` y localiza la clase `Main.java`.
4.  Haz clic derecho sobre `Main.java` y selecciona **Run File**.
5.  Interactúa con el sistema a través del menú de consola que se desplegará.

## Comandos soportados (API)

