package com.udistrital.View;

import javax.swing.*;
import java.awt.*;

public class InicioPanel extends JPanel {
    
    private CardLayout cardLayout;
    private JPanel contenedor;
    
    public InicioPanel(CardLayout cardLayout, JPanel contenedor) {
        this.cardLayout = cardLayout;
        this.contenedor = contenedor;
        
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 248, 255));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        
        JLabel titulo = new JLabel("MiniDB - Gestor de Base de Datos");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridy = 0;
        add(titulo, gbc);
        
        JButton btnTablas = crearBoton("TABLAS", new Color(52, 152, 219));
        btnTablas.addActionListener(e -> cardLayout.show(contenedor, "tablas"));
        gbc.gridy = 1;
        add(btnTablas, gbc);
        
        JButton btnRegistros = crearBoton("REGISTROS", new Color(46, 204, 113));
        btnRegistros.addActionListener(e -> cardLayout.show(contenedor, "registros"));
        gbc.gridy = 2;
        add(btnRegistros, gbc);
        
        JButton btnArbol = crearBoton("ARBOL AVL", new Color(155, 89, 182));
        btnArbol.addActionListener(e -> cardLayout.show(contenedor, "avl"));
        gbc.gridy = 3;
        add(btnArbol, gbc);
        
        JButton btnAyuda = crearBoton("AYUDA", new Color(241, 196, 15));
        btnAyuda.addActionListener(e -> cardLayout.show(contenedor, "ayuda"));
        gbc.gridy = 4;
        add(btnAyuda, gbc);
        
        JButton btnSalir = crearBoton("SALIR", new Color(231, 76, 60));
        btnSalir.addActionListener(e -> System.exit(0));
        gbc.gridy = 5;
        add(btnSalir, gbc);
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 18));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setPreferredSize(new Dimension(200, 50));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
}