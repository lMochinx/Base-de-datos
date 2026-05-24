package com.udistrital.arbolavl;

// Archivo: NodoAVL.java
public class NodoAVL {
    int clave;
    String dato; 
    int altura;
    NodoAVL izquierdo;
    NodoAVL derecho;

    public NodoAVL(int clave, String dato) {
        this.clave = clave;
        this.dato = dato;
        this.altura = 1; 
    }
}