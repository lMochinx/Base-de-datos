package com.udistrital.View;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import com.udistrital.Controller.Controller;
import com.udistrital.Model.Tabla;

public class AVLPanel extends JPanel {
    
    private CardLayout cardLayout;
    private JPanel contenedor;
    private JComboBox<String> comboTablas;
    private Controller controller;
    private PanelDibujoArbol panelDibujo;
    private JButton btnRefrescar;
    private JLabel lblInfo;
    private JTextArea areaRecorridos;
    
    public AVLPanel(CardLayout cardLayout, JPanel contenedor) {
        this.cardLayout = cardLayout;
        this.contenedor = contenedor;
        
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
  
        // PANEL DE SELECCIÓN DE TABLA 
        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelSeleccion.setBackground(Color.WHITE);
        panelSeleccion.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
            "Seleccionar Tabla",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(155, 89, 182)
        ));
        
        comboTablas = new JComboBox<>();
        comboTablas.setPreferredSize(new Dimension(200, 30));
        comboTablas.setFont(new Font("Arial", Font.PLAIN, 12));
        comboTablas.addActionListener(e -> cargarArbol());
        
        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setBackground(new Color(52, 152, 219));
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setFont(new Font("Arial", Font.BOLD, 11));
        btnRefrescar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefrescar.addActionListener(e -> actualizarListaTablas());
        
        JButton btnVolver = new JButton("← Volver al Inicio");
        btnVolver.setFont(new Font("Arial", Font.BOLD, 11));
        btnVolver.setBackground(new Color(231, 76, 60));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> {
            System.out.println("Volviendo al inicio...");
            if (cardLayout != null && contenedor != null) {
                cardLayout.show(contenedor, "inicio");
            }
        });
        
        panelSeleccion.add(new JLabel("Tabla:"));
        panelSeleccion.add(comboTablas);
        panelSeleccion.add(btnRefrescar);
        panelSeleccion.add(btnVolver);
        
        // PANEL DE INFORMACIÓN
        lblInfo = new JLabel("Seleccione una tabla para visualizar el árbol AVL");
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblInfo.setForeground(new Color(100, 100, 100));
        lblInfo.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // PANEL DE DIBUJO DEL ÁRBOL
        panelDibujo = new PanelDibujoArbol();
        JScrollPane scrollDibujo = new JScrollPane(panelDibujo);
        scrollDibujo.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
            "Representación Gráfica del Árbol AVL",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(155, 89, 182)
        ));
        scrollDibujo.setPreferredSize(new Dimension(850, 400));
        
        // PANEL DE RECORRIDOS
        areaRecorridos = new JTextArea();
        areaRecorridos.setEditable(false);
        areaRecorridos.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaRecorridos.setBackground(new Color(252, 252, 252));
        areaRecorridos.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane scrollRecorridos = new JScrollPane(areaRecorridos);
        scrollRecorridos.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
            "Recorridos del Árbol",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(46, 204, 113)
        ));
        scrollRecorridos.setPreferredSize(new Dimension(850, 140));
        
        // PANEL INFERIOR
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(lblInfo, BorderLayout.NORTH);
        panelInferior.add(scrollRecorridos, BorderLayout.CENTER);
        
        add(panelSeleccion, BorderLayout.NORTH);
        add(scrollDibujo, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
        
        panelDibujo.setPreferredSize(new Dimension(850, 400));
    }
    
    // CONECTAR CON EL CONTROLADOR
    
    public void setController(Controller controller) {
        this.controller = controller;
        actualizarListaTablas();
    }
    
    private void actualizarListaTablas() {
        if (controller == null) return;
        comboTablas.removeAllItems();
        for (String nombre : controller.listarTablas()) {
            comboTablas.addItem(nombre);
        }
        if (comboTablas.getItemCount() > 0) {
            cargarArbol();
        } else {
            panelDibujo.setNodoRaiz(null);
            panelDibujo.repaint();
            areaRecorridos.setText("");
            lblInfo.setText("No hay tablas creadas. Cree una tabla en la sección TABLAS.");
        }
    }
    
    private void cargarArbol() {
        if (controller == null) return;
        String nombreTabla = (String) comboTablas.getSelectedItem();
        if (nombreTabla == null) return;
        
        Tabla tabla = controller.obtenerTabla(nombreTabla);
        if (tabla == null) {
            panelDibujo.setNodoRaiz(null);
            panelDibujo.repaint();
            areaRecorridos.setText("");
            lblInfo.setText("Tabla '" + nombreTabla + "' no encontrada.");
            return;
        }
        
        Object raiz = obtenerRaizArbol(tabla.getIndice());
        
        if (raiz == null) {
            panelDibujo.setNodoRaiz(null);
            panelDibujo.repaint();
            areaRecorridos.setText("");
            lblInfo.setText("Árbol AVL vacío. No hay registros en la tabla '" + nombreTabla + "'.");
        } else {
            panelDibujo.setNodoRaiz(raiz);
            panelDibujo.repaint();
            int altura = obtenerAltura(raiz);
            int nodos = contarNodos(raiz);
            
            List<Integer> inorden = new ArrayList<>();
            List<Integer> preorden = new ArrayList<>();
            List<Integer> postorden = new ArrayList<>();
            
            recorrerInorden(raiz, inorden);
            recorrerPreorden(raiz, preorden);
            recorrerPostorden(raiz, postorden);
            
            StringBuilder sb = new StringBuilder();
            sb.append("╔════════════════════════════════════════════════════════════════════════════════╗\n");
            sb.append("║                              INFORMACIÓN DEL ÁRBOL                            ║\n");
            sb.append("╚════════════════════════════════════════════════════════════════════════════════╝\n\n");
            sb.append("   • Tabla: ").append(nombreTabla).append("\n");
            sb.append("   • Altura del árbol: ").append(altura).append("\n");
            sb.append("   • Número de nodos: ").append(nodos).append("\n\n");
            
            sb.append("╔════════════════════════════════════════════════════════════════════════════════╗\n");
            sb.append("║                              RECORRIDOS DEL ÁRBOL                             ║\n");
            sb.append("╚════════════════════════════════════════════════════════════════════════════════╝\n\n");
            
            sb.append("📋 INORDEN (orden ascendente por PK):\n");
            sb.append("   ").append(inorden).append("\n\n");
            
            sb.append("📋 PREORDEN (raíz primero):\n");
            sb.append("   ").append(preorden).append("\n\n");
            
            sb.append("📋 POSTORDEN (hojas primero):\n");
            sb.append("   ").append(postorden).append("\n");
            
            areaRecorridos.setText(sb.toString());
            lblInfo.setText("Tabla: " + nombreTabla + " | Altura: " + altura + " | Nodos: " + nodos);
        }
    }
    
    // ACCEDER A LA RAIZ
    
    private Object obtenerRaizArbol(Object arbolAVL) {
        try {
            Field campoRaiz = arbolAVL.getClass().getDeclaredField("raiz");
            campoRaiz.setAccessible(true);
            return campoRaiz.get(arbolAVL);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private int obtenerAltura(Object nodo) {
        if (nodo == null) return 0;
        try {
            Field campoAltura = nodo.getClass().getDeclaredField("altura");
            campoAltura.setAccessible(true);
            return (int) campoAltura.get(nodo);
        } catch (Exception e) {
            return 0;
        }
    }
    
    private int contarNodos(Object nodo) {
        if (nodo == null) return 0;
        return 1 + contarNodos(obtenerHijo(nodo, "izquierdo")) + contarNodos(obtenerHijo(nodo, "derecho"));
    }
    
    private Object obtenerHijo(Object nodo, String lado) {
        try {
            Field campoHijo = nodo.getClass().getDeclaredField(lado);
            campoHijo.setAccessible(true);
            return campoHijo.get(nodo);
        } catch (Exception e) {
            return null;
        }
    }
    
    private int obtenerClave(Object nodo) {
        try {
            Field campoClave = nodo.getClass().getDeclaredField("clave");
            campoClave.setAccessible(true);
            return (int) campoClave.get(nodo);
        } catch (Exception e) {
            return -1;
        }
    }
    
    private void recorrerInorden(Object nodo, List<Integer> lista) {
        if (nodo == null) return;
        recorrerInorden(obtenerHijo(nodo, "izquierdo"), lista);
        lista.add(obtenerClave(nodo));
        recorrerInorden(obtenerHijo(nodo, "derecho"), lista);
    }
    
    private void recorrerPreorden(Object nodo, List<Integer> lista) {
        if (nodo == null) return;
        lista.add(obtenerClave(nodo));
        recorrerPreorden(obtenerHijo(nodo, "izquierdo"), lista);
        recorrerPreorden(obtenerHijo(nodo, "derecho"), lista);
    }
    
    private void recorrerPostorden(Object nodo, List<Integer> lista) {
        if (nodo == null) return;
        recorrerPostorden(obtenerHijo(nodo, "izquierdo"), lista);
        recorrerPostorden(obtenerHijo(nodo, "derecho"), lista);
        lista.add(obtenerClave(nodo));
    }
    
    // DIBUJAR EL ÁRBOL
    
    private class PanelDibujoArbol extends JPanel {
        
        private Object nodoRaiz;
        private int radioNodo = 25;
        private int distanciaVertical = 65;
        
        public PanelDibujoArbol() {
            setBackground(Color.WHITE);
        }
        
        public void setNodoRaiz(Object raiz) {
            this.nodoRaiz = raiz;
            repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (nodoRaiz == null) {
                g2.setFont(new Font("Arial", Font.BOLD, 14));
                g2.setColor(Color.GRAY);
                g2.drawString("Árbol vacío - No hay registros en esta tabla", getWidth() / 2 - 180, getHeight() / 2);
                return;
            }
            
            int profundidad = calcularProfundidad(nodoRaiz);
            int anchoTotal = (int) Math.pow(2, profundidad) * (radioNodo * 2 + 10);
            int xInicio = Math.max(radioNodo + 10, (getWidth() - anchoTotal) / 2);
            
            dibujarNodo(g2, nodoRaiz, getWidth() / 2, 40, getWidth() / 4);
        }
        
        private int calcularProfundidad(Object nodo) {
            if (nodo == null) return 0;
            return 1 + Math.max(calcularProfundidad(obtenerHijo(nodo, "izquierdo")), 
                               calcularProfundidad(obtenerHijo(nodo, "derecho")));
        }
        
        private void dibujarNodo(Graphics2D g2, Object nodo, int x, int y, int offsetX) {
            if (nodo == null) return;
            
            int clave = obtenerClave(nodo);
            Object izquierdo = obtenerHijo(nodo, "izquierdo");
            Object derecho = obtenerHijo(nodo, "derecho");
            
            int xIzq = x - Math.max(offsetX, 35);
            int xDer = x + Math.max(offsetX, 35);
            int yHijo = y + distanciaVertical;
            
            g2.setColor(new Color(100, 100, 100));
            g2.setStroke(new BasicStroke(2));
            
            if (izquierdo != null) {
                g2.drawLine(x, y + radioNodo/2, xIzq + radioNodo/2, yHijo - radioNodo/2);
                dibujarNodo(g2, izquierdo, xIzq, yHijo, offsetX / 2);
            }
            
            if (derecho != null) {
                g2.drawLine(x, y + radioNodo/2, xDer + radioNodo/2, yHijo - radioNodo/2);
                dibujarNodo(g2, derecho, xDer, yHijo, offsetX / 2);
            }
            
            g2.setColor(new Color(200, 200, 200));
            g2.fillOval(x - radioNodo/2 + 2, y - radioNodo/2 + 2, radioNodo, radioNodo);
            
            GradientPaint gradiente = new GradientPaint(
                x - radioNodo/2, y - radioNodo/2, new Color(52, 152, 219),
                x + radioNodo/2, y + radioNodo/2, new Color(41, 128, 185));
            g2.setPaint(gradiente);
            g2.fillOval(x - radioNodo/2, y - radioNodo/2, radioNodo, radioNodo);
            
            g2.setColor(new Color(31, 97, 141));
            g2.drawOval(x - radioNodo/2, y - radioNodo/2, radioNodo, radioNodo);
            
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 13));
            String texto = String.valueOf(clave);
            FontMetrics fm = g2.getFontMetrics();
            int anchoTexto = fm.stringWidth(texto);
            g2.drawString(texto, x - anchoTexto/2, y + 5);
            
            int altura = obtenerAltura(nodo);
            g2.setFont(new Font("Arial", Font.PLAIN, 9));
            g2.setColor(new Color(220, 220, 220));
            g2.drawString("h:" + altura, x + radioNodo/2 - 15, y - radioNodo/2 + 12);
        }
    }
}