package com.udistrital.Model;

public class Campo {

	public String nombre;
    public String tipo; // "INT", "TEXT", "REAL", "BOOL"
    public boolean esPK;

    public Campo(String nombre, String tipo, boolean esPK) {
        this.nombre = nombre;
        this.tipo   = tipo;
        this.esPK   = esPK;
    }

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public boolean isEsPK() {
		return esPK;
	}

	public void setEsPK(boolean esPK) {
		this.esPK = esPK;
	}
}
