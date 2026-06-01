package com.udistrital.Controller;

import javax.swing.SwingUtilities;

import com.udistrital.View.VentanaPrincipal;

public class MainGUI {

    public static void main(String[] args) {
    	
    	SwingUtilities.invokeLater(() -> {
            VentanaPrincipal vista = new VentanaPrincipal();
            Controller controlador = new Controller(vista); // ← usa el constructor GUI
            vista.setControllerParaAVL(controlador);
            vista.setVisible(true);
        });
    }
}
