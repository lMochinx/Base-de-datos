package com.udistrital.Controller;

import java.util.List;
import java.util.ArrayList;
import com.udistrital.Model.Campo;

public class CargadorDataset {
    
    private Controller controller;
    
    public CargadorDataset(Controller controller) {
        this.controller = controller;
    }
    
    // ===== DATASET PEQUEÑO (5 registros) =====
    public void cargarDatasetPequeno() {
      
        List<Campo> campos = List.of(
            new Campo("id", "INT", true),
            new Campo("nombre", "TEXT", false),
            new Campo("edad", "INT", false)
        );
        
        if (controller.obtenerTabla("estudiantes") == null) {
            controller.crearTabla("estudiantes", campos);
        } else {
            controller.eliminar("estudiantes", null, null);
        }

        controller.insertar("estudiantes", List.of("id", "nombre", "edad"), List.of("1", "'Ana'", "20"));
        controller.insertar("estudiantes", List.of("id", "nombre", "edad"), List.of("2", "'Luis'", "22"));
        controller.insertar("estudiantes", List.of("id", "nombre", "edad"), List.of("3", "'Carla'", "19"));
        controller.insertar("estudiantes", List.of("id", "nombre", "edad"), List.of("4", "'Pedro'", "21"));
        controller.insertar("estudiantes", List.of("id", "nombre", "edad"), List.of("5", "'Maria'", "20"));
    }
    
    // ===== DATASET MEDIANO (30 registros) =====
    public void cargarDatasetMediano() {
        List<Campo> campos = List.of(
            new Campo("id", "INT", true),
            new Campo("nombre", "TEXT", false),
            new Campo("edad", "INT", false)
        );
        
        if (controller.obtenerTabla("estudiantes") == null) {
            controller.crearTabla("estudiantes", campos);
        } else {
            controller.eliminar("estudiantes", null, null);
        }

        String[] nombres = {
            "Ana Lopez", "Luis Perez", "Carla Gomez", "Pedro Ruiz", "Maria Diaz",
            "Jose Martinez", "Laura Fernandez", "Carlos Sanchez", "Sofia Ramirez", "Diego Torres",
            "Valentina Gomez", "Andres Herrera", "Camila Ortiz", "Felipe Castro", "Natalia Rojas",
            "Sebastian Morales", "Daniela Jimenez", "Mateo Romero", "Paula Mendoza", "Simon Gutierrez",
            "Gabriela Serrano", "Julian Herrera", "Andrea Paredes", "Ricardo Salazar", "Marcela Aguirre",
            "Esteban Leon", "Veronica Mora", "Hector Velez", "Lorena Espinoza", "Oscar Dominguez"
        };
        
        for (int i = 0; i < nombres.length; i++) {
            int id = i + 1;
            String nombre = "'" + nombres[i] + "'";
            int edad = 19 + (int)(Math.random() * 7);
            controller.insertar("estudiantes", 
                List.of("id", "nombre", "edad"), 
                List.of(String.valueOf(id), nombre, String.valueOf(edad)));
        }
    }
    
    // ===== MÉTODO PARA MOSTRAR RESUMEN =====
    public String getResumenDataset() {
        var tabla = controller.obtenerTabla("estudiantes");
        if (tabla == null) {
            return "No hay datos cargados en la tabla 'estudiantes'";
        }
        
        List<com.udistrital.Model.Fila> filas = tabla.getIndice().inOrdenFilas();
        return "Tabla 'estudiantes' tiene " + filas.size() + " registro(s)";
    }
}