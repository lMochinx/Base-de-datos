package com.udistrital.View;

import java.util.Scanner;

import com.udistrital.Model.ArbolAVL;
import com.udistrital.Model.Fila;
import com.udistrital.Model.NodoAVL;

public class MenuAVL {
    private ArbolAVL arbol;
    private Scanner scanner;

    public MenuAVL() {
        this.arbol = new ArbolAVL();
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        int opcion = 0;
        do {
            System.out.println("\n-----------------------------");
            System.out.println("      MENÚ ÁRBOL AVL       ");
            System.out.println("-----------------------------");
            System.out.println("1. Insertar nodo");
            System.out.println("2. Eliminar nodo");
            System.out.println("3. Buscar nodo");
            System.out.println("4. Update logico (Actualizar dato)");
            System.out.println("5. Imprimir en Pre-Orden");
            System.out.println("6. Imprimir en In-Orden");
            System.out.println("7. Imprimir en Post-Orden");
            System.out.println("8. Salir");
            System.out.print("Elige una opción: ");
            
            try {
                opcion = Integer.parseInt(scanner.nextLine());
                //procesarOpcion(opcion);
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor ingresa un número válido.");
            }
        } while (opcion != 8);
    }
    /*
    private void procesarOpcion(int opcion) {
        int clave;
        String dato;

        switch (opcion) {
            case 1:
                System.out.print("Ingresa la clave (número): ");
                clave = Integer.parseInt(scanner.nextLine());
                System.out.print("Ingresa el dato (texto): ");
                dato="";
                arbol.insertar(clave, dato);
                System.out.println(">> Nodo insertado correctamente.");
                break;

            case 2:
                System.out.print("Ingresa la clave del nodo a eliminar: ");
                clave = Integer.parseInt(scanner.nextLine());
                arbol.eliminar(clave);
                System.out.println(">> Se ejecutó la orden de eliminación.");
                break;

            case 3:
                System.out.print("Ingresa la clave a buscar: ");
                clave = Integer.parseInt(scanner.nextLine());
                NodoAVL encontrado = arbol.buscar(clave);
                if (encontrado != null) {
                    System.out.println(">> Nodo encontrado -> Clave: " + encontrado.getClave() + " | Dato: " + encontrado.getDato());
                } else {
                    System.out.println(">> El nodo con clave " + clave + " no existe en el árbol.");
                }
                break;

            case 4:
                System.out.print("Ingresa la clave del nodo a actualizar: ");
                clave = Integer.parseInt(scanner.nextLine());
                System.out.print("Ingresa el nuevo dato (texto): ");
                dato = scanner.nextLine();
                boolean exito = arbol.updateLogico(clave, dato);
                if (exito) {
                    System.out.println(">> Update lógico realizado con éxito.");
                } else {
                    System.out.println(">> No se pudo actualizar. El nodo no existe.");
                }
                break;

            case 5:
                arbol.imprimirPreOrden();
                break;

            case 6:
                arbol.imprimirInOrden();
                break;

            case 7:
                arbol.imprimirPostOrden();
                break;

            case 8:
                System.out.println("Saliendo del programa");
                break;

            default:
                System.out.println("Opcion no valida");
        }
    }
    */
}
