package com.udistrital.Model;

import java.util.List;

public class Tabla {

	public String nombre;
    public List<Campo> campos;
    public ArbolAVL indice;   //un arbol por tabla

    public Tabla(String nombre, List<Campo> campos) {
        this.nombre = nombre;
        this.campos = campos;
        this.indice = new ArbolAVL();
    }

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Campo> getCampos() {
		return campos;
	}

	public void setCampos(List<Campo> campos) {
		this.campos = campos;
	}

	public ArbolAVL getIndice() {
		return indice;
	}

	public void setIndice(ArbolAVL indice) {
		this.indice = indice;
	}
}
