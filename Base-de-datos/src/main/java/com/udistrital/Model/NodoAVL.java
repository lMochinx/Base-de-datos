package com.udistrital.Model;

// Archivo: NodoAVL.java
public class NodoAVL {
    int clave;
    Fila dato; 
    int altura;
    NodoAVL izquierdo;
    NodoAVL derecho;

    public NodoAVL(int clave, Fila dato) {
        this.clave = clave;
        this.dato = dato;
        this.altura = 1; 
    }

	public int getClave() {
		return clave;
	}

	public void setClave(int clave) {
		this.clave = clave;
	}

	public Fila getDato() {
		return dato;
	}

	public void setDato(Fila dato) {
		this.dato = dato;
	}

	public int getAltura() {
		return altura;
	}

	public void setAltura(int altura) {
		this.altura = altura;
	}

	public NodoAVL getIzquierdo() {
		return izquierdo;
	}

	public void setIzquierdo(NodoAVL izquierdo) {
		this.izquierdo = izquierdo;
	}

	public NodoAVL getDerecho() {
		return derecho;
	}

	public void setDerecho(NodoAVL derecho) {
		this.derecho = derecho;
	}
}