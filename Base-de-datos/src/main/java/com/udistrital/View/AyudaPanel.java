package com.udistrital.View;

import javax.swing.*;
import java.awt.*;

public class AyudaPanel extends JPanel {
    
    private CardLayout cardLayout;
    private JPanel contenedor;
    
    public AyudaPanel(CardLayout cardLayout, JPanel contenedor) {
        this.cardLayout = cardLayout;
        this.contenedor = contenedor;
        
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // ============================================================
        // CABECERA
        // ============================================================
        JPanel panelCabecera = new JPanel(new BorderLayout());
        panelCabecera.setBackground(new Color(52, 73, 94));
        panelCabecera.setPreferredSize(new Dimension(900, 50));
        panelCabecera.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JButton btnVolver = new JButton("← Volver al Inicio");
        btnVolver.setFont(new Font("Arial", Font.BOLD, 14));
        btnVolver.setBackground(new Color(231, 76, 60));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> cardLayout.show(contenedor, "inicio"));
        
        JLabel titulo = new JLabel("Ayuda - Comandos Disponibles", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);
        
        panelCabecera.add(btnVolver, BorderLayout.WEST);
        panelCabecera.add(titulo, BorderLayout.CENTER);
        
        add(panelCabecera, BorderLayout.NORTH);
        
        // ============================================================
        // CONTENIDO
        // ============================================================
        JTextArea areaAyuda = new JTextArea();
        areaAyuda.setEditable(false);
        areaAyuda.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaAyuda.setBackground(Color.WHITE);
        areaAyuda.setForeground(Color.BLACK);
        areaAyuda.setText(
            "\n" +
            "╔══════════════════════════════════════════════════════════════════════════════════════════╗\n" +
            "║                                    COMANDOS DISPONIBLES                                   \n" +
            "╚══════════════════════════════════════════════════════════════════════════════════════════╝\n" +
            "\n" +
            "┌─────────────────────────────────────────────────────────────────────────────────────────┐\n" +
            "│  Crear tabla                                                                       \n" +
            "├─────────────────────────────────────────────────────────────────────────────────────────┤\n" +
            "│  Sintaxis: CREATE TABLE nombre (campo TIPO [PK], ...)                                   \n" +
            "│  Ejemplo:  CREATE TABLE alumnos (id INT PK, nombre TEXT, edad INT)                      \n" +
            "└─────────────────────────────────────────────────────────────────────────────────────────┘\n" +
            "\n" +
            "┌─────────────────────────────────────────────────────────────────────────────────────────┐\n" +
            "│  Borrar tabla                                                                         \n" +
            "├─────────────────────────────────────────────────────────────────────────────────────────┤\n" +
            "│  Sintaxis: DROP TABLE nombre                                                            \n" +
            "│  Ejemplo:  DROP TABLE alumnos                                                           \n" +
            "└─────────────────────────────────────────────────────────────────────────────────────────┘\n" +
            "\n" +
            "┌─────────────────────────────────────────────────────────────────────────────────────────┐\n" +
            "│  Mostrar tablas                                                                      \n" +
            "├─────────────────────────────────────────────────────────────────────────────────────────┤\n" +
            "│  Sintaxis: SHOW TABLES                                                                  \n" +
            "│  Ejemplo:  SHOW TABLES                                                                  \n" +
            "└─────────────────────────────────────────────────────────────────────────────────────────┘\n" +
            "\n" +
            "┌─────────────────────────────────────────────────────────────────────────────────────────┐\n" +
            "│  Insertar                                                                       \n" +
            "├─────────────────────────────────────────────────────────────────────────────────────────┤\n" +
            "│  Sintaxis: INSERT INTO nombre (col1, col2) VALUES (v1, v2)                              \n" +
            "│  Ejemplo:  INSERT INTO alumnos (id, nombre, edad) VALUES (1, 'Ana', 20)                 \n" +
            "└─────────────────────────────────────────────────────────────────────────────────────────┘\n" +
            "\n" +
            "┌─────────────────────────────────────────────────────────────────────────────────────────┐\n" +
            "│  Seleccionar                                                                           \n" +
            "├─────────────────────────────────────────────────────────────────────────────────────────┤\n" +
            "│  Sintaxis: SELECT * FROM nombre [WHERE campo = valor]                                   \n" +
            "│  Ejemplo:  SELECT * FROM alumnos                                                        \n" +
            "│  Ejemplo:  SELECT * FROM alumnos WHERE id = 1                                           \n" +
            "└─────────────────────────────────────────────────────────────────────────────────────────┘\n" +
            "\n" +
            "┌─────────────────────────────────────────────────────────────────────────────────────────┐\n" +
            "│  Actualizacion                                                                          \n" +
            "├─────────────────────────────────────────────────────────────────────────────────────────┤\n" +
            "│  Sintaxis: UPDATE nombre SET campo = valor [WHERE condición]                            \n" +
            "│  Ejemplo:  UPDATE alumnos SET nombre = 'Maria' WHERE id = 1                             \n" +
            "└─────────────────────────────────────────────────────────────────────────────────────────┘\n" +
            "\n" +
            "┌─────────────────────────────────────────────────────────────────────────────────────────┐\n" +
            "│  Brorrar                                                                             \n" +
            "├─────────────────────────────────────────────────────────────────────────────────────────┤\n" +
            "│  Sintaxis: DELETE FROM nombre [WHERE campo = valor]                                     \n" +
            "│  Ejemplo:  DELETE FROM alumnos WHERE id = 1                                             \n" +
            "└─────────────────────────────────────────────────────────────────────────────────────────┘\n" +
            "\n" +
            "╔══════════════════════════════════════════════════════════════════════════════════════════╗\n" +
            "│                                    TIPOS DE DATOS                                        \n" +
            "╚══════════════════════════════════════════════════════════════════════════════════════════╝\n" +
            "\n" +
            "┌─────────────────────────────────────────────────────────────────────────────────────────┐\n" +
            "│  🔢 INT      → Números enteros                     (Ejemplo: 25, -10, 0, 1000)         \n" +
            "│  📝 TEXT     → Texto o cadena de caracteres       (Ejemplo: 'Ana', 'Hola Mundo')       \n" +
            "│  🔢 REAL     → Números decimales                  (Ejemplo: 3.14, -2.5, 0.0)           \n" +
            "│  ✅ BOOL     → Valores booleanos                  (Ejemplo: true, false, 1, 0)         \n" +
            "└─────────────────────────────────────────────────────────────────────────────────────────┘\n" +
            "\n" +
            "╔══════════════════════════════════════════════════════════════════════════════════════════╗\n" +
            "│                                   NOTAS IMPORTANTES                                      \n" +
            "╚══════════════════════════════════════════════════════════════════════════════════════════╝\n" +
            "\n" +
            "┌─────────────────────────────────────────────────────────────────────────────────────────┐\n" +
            "│  • El campo marcado como PK es obligatorio en cada INSERT                               \n" +
            "│  • Solo un campo puede ser PK por tabla                                                 \n" +
            "│  • Los valores TEXT pueden ir con o sin comillas simples                                \n" +
            "│  • El árbol AVL garantiza búsquedas en O(log n)                                         \n" +
            "│  • Escriba EXIT para salir de la aplicación                                             \n" +
            "└─────────────────────────────────────────────────────────────────────────────────────────┘\n"
        );
        
        JScrollPane scrollPane = new JScrollPane(areaAyuda);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);
    }
}