# Motor de Base de Datos.

Este repositorio contiene la implementación de un motor de base de datos en memoria desarrollado en Java. El sistema utiliza el patrón arquitectónico Modelo-Vista-Controlador (MVC) y basa su indexación principal en un Árbol AVL auto-balanceable, lo que garantiza un rendimiento de $O(\log n)$ en las operaciones de búsqueda, inserción y eliminación.

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

El motor interactúa mediante una interfaz de línea de comandos (REPL) que acepta un lenguaje estilo SQL simplificado. Las palabras clave son insensibles a mayúsculas y **no** requieren punto y coma al final.

### Definición de Datos (DDL)
* **Crear tabla:** `CREATE TABLE nombre (campo TIPO [PK], campo TIPO, ...)`
    * Define una tabla con sus columnas y tipos. 
    * Requiere obligatoriamente un campo marcado como clave primaria (`[PK]`), el cual será indexado en el Árbol AVL. Este campo no puede ser de tipo `BOOL`.
* **Eliminar tabla:** `DROP TABLE nombre`
    * Elimina de forma irreversible la estructura de la tabla y su archivo de persistencia.
* **Listar tablas:** `SHOW TABLES`
    * Muestra el esquema de todas las tablas existentes, detallando campos, tipos de datos y claves primarias.

### Manipulación de Datos (DML)
* **Insertar fila:** `INSERT INTO nombre (campo1, ...) VALUES (valor1, ...)`
    * Añade un registro. La PK es obligatoria y debe ser única; el comando se rechaza si hay colisión de claves. Los valores de texto deben ir entre comillas simples (`' '`).
* **Consultar filas:** `SELECT * FROM nombre [WHERE campo = valor]`
    * Sin `WHERE`: Retorna todas las filas ordenadas ascendentemente por su PK (recorrido inorden del Árbol AVL).
    * Con `WHERE`: Filtra devolviendo solo las coincidencias exactas.
* **Actualizar filas:** `UPDATE nombre SET campo = valor [WHERE campo = valor]`
    * Modifica el valor de un campo específico (o de toda la tabla si se omite el `WHERE`).
    * *Nota:* La clave primaria (PK) es inmutable. Para cambiarla, es necesario eliminar e insertar nuevamente la fila.
* **Eliminar filas:** `DELETE FROM nombre [WHERE campo = valor]`
    * Borra registros específicos o vacía la tabla completa si no se provee la cláusula `WHERE` (conservando el esquema).

### Tipos de Datos Soportados
* **`INT`**: Números enteros (ej. `22`, `-5`).
* **`REAL`**: Números decimales (ej. `4.2`, `3.14`).
* **`TEXT`**: Cadenas de texto (ej. `'Cristian'`, `'Calle 80'`).
* **`BOOL`**: Valores lógicos (`true`, `false`).

### Restricciones del Lenguaje
* Solo se soporta selección total de columnas (`SELECT *`).
* La cláusula `WHERE` está limitada a una única condición de igualdad simple (no soporta operadores relacionales de rango, `AND` ni `OR`).

### Utilidades
* **`HELP`**: Despliega la lista de ayuda.
* **`EXIT`**: Finaliza la ejecución de la base de datos de manera segura.

## Tecnologías Utilizadas
* **Lenguaje:** Java (JDK 8+)
* **Arquitectura:** MVC (Modelo-Vista-Controlador)
* **Estructura de Datos:** Árbol AVL (Auto-balanceable)
* **Persistencia:** JSON (Manejo de archivos I/O)
* **Entorno de desarrollo:** NetBeans / Eclipse

## Ejemplo rápido
Para crear y poblar una tabla rápidamente:

1. `CREATE TABLE usuarios (id INT [PK], nombre TEXT)`
2. `INSERT INTO usuarios (id, nombre) VALUES (1, 'Cristian')`
3. `INSERT INTO usuarios (id, nombre) VALUES (2, 'Valentina')`
4. `SELECT * FROM usuarios`

## Autores
* **Mateo Baez** - [lMochinx](https://github.com/lMochinx)
* **Valentina Alfonso** - [Savior-arch](https://github.com/Savior-arch)
* **Cristian Parroquiano** - [Sator100](https://github.com/Sator100)
