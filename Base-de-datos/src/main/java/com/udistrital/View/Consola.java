package com.udistrital.View;

import java.util.Scanner;

public class Consola {

	private Scanner sc;
	
	public Consola() {
		sc = new Scanner(System.in);
	}
	
	public void mostrarMensaje(String mensaje) {
		System.out.println(mensaje);
	}
	
	public String leerString() {
		String mensaje = sc.nextLine();
		return mensaje;
	}
}
