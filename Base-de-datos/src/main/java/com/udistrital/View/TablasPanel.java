package com.udistrital.View;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TablasPanel extends JPanel {
    
    private CardLayout cardLayout;
    private JPanel contenedor;
    
    // Componentes principales
    private JList<String> listaTablas;
    private DefaultListModel<String> modeloLista;
    private JTextArea areaResultados;
    
    // Botones
    private JButton btnCrear;
    private JButton btnEliminar;
    private JButton btnListar;
    private JButton btnRefrescar;
    
    // Diálogo de creación de tabla
    private JDialog dialogCrearTabla;
    private JTextField txtNombreTabla;
    private JPanel panelCampos;
    private List<JTextField> camposNombre;
    private List<JComboBox<String>> camposTipo;
    private List<JCheckBox> camposPK;
    
    // Datos temporales para el diálogo
    private String nombreTablaCreada;
    private List<String[]> camposCreados;
    
    public TablasPanel(CardLayout cardLayout, JPanel contenedor) {
        this.cardLayout = cardLayout;
        this.contenedor = contenedor;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);
        
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        // ===== PANEL SUPERIOR CON BOTÓN VOLVER =====
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
        
        JLabel titulo = new JLabel("Gestión de Tablas", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);
        
        panelCabecera.add(btnVolver, BorderLayout.WEST);
        panelCabecera.add(titulo, BorderLayout.CENTER);
        
        add(panelCabecera, BorderLayout.NORTH);
        
        // ===== PANEL CENTRAL DIVIDIDO =====
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(280);
        splitPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Panel Izquierdo - Lista de tablas
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            "Tablas Existentes",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(52, 152, 219)
        ));
        panelIzquierdo.setBackground(Color.WHITE);
        
        modeloLista = new DefaultListModel<>();
        listaTablas = new JList<>(modeloLista);
        listaTablas.setFont(new Font("Monospaced", Font.PLAIN, 14));
        listaTablas.setBackground(Color.WHITE);
        
        JScrollPane scrollLista = new JScrollPane(listaTablas);
        panelIzquierdo.add(scrollLista, BorderLayout.CENTER);
        
        btnRefrescar = new JButton("🔄 Refrescar");
        btnRefrescar.setBackground(new Color(52, 152, 219));
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setFont(new Font("Arial", Font.BOLD, 12));
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelIzquierdo.add(btnRefrescar, BorderLayout.SOUTH);
        
        // Panel Derecho - Acciones
        JPanel panelDerecho = new JPanel(new BorderLayout(10, 10));
        panelDerecho.setBackground(Color.WHITE);
        
        JPanel panelBotones = new JPanel(new GridLayout(1, 3, 15, 15));
        panelBotones.setBorder(BorderFactory.createTitledBorder("Acciones"));
        panelBotones.setBackground(Color.WHITE);
        
        btnCrear = new JButton("Crear Tabla");
        btnEliminar = new JButton("Eliminar Tabla");
        btnListar = new JButton("Listar Tabls");
        
        configurarBoton(btnCrear, new Color(46, 204, 113));
        configurarBoton(btnEliminar, new Color(231, 76, 60));
        configurarBoton(btnListar, new Color(52, 152, 219));
        
        panelBotones.add(btnCrear);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnListar);
        
        // Área de resultados
        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaResultados.setBackground(new Color(252, 252, 252));
        areaResultados.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JScrollPane scrollResultados = new JScrollPane(areaResultados);
        scrollResultados.setBorder(BorderFactory.createTitledBorder("Resultados"));
        
        panelDerecho.add(panelBotones, BorderLayout.NORTH);
        panelDerecho.add(scrollResultados, BorderLayout.CENTER);
        
        splitPane.setLeftComponent(panelIzquierdo);
        splitPane.setRightComponent(panelDerecho);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private void configurarBoton(JButton boton, Color color) {
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    // ===== MÉTODOS PARA REGISTRAR LISTENERS (LLAMADOS POR EL CONTROLADOR) =====
    
    public void addCrearTablaListener(ActionListener listener) {
        btnCrear.addActionListener(listener);
    }
    
    public void addEliminarTablaListener(ActionListener listener) {
        btnEliminar.addActionListener(listener);
    }
    
    public void addListarTablasListener(ActionListener listener) {
        btnListar.addActionListener(listener);
    }
    
    public void addRefrescarListener(ActionListener listener) {
        btnRefrescar.addActionListener(listener);
    }
    
    // ===== MÉTODOS PARA ACTUALIZAR LA VISTA =====
    
    public void actualizarListaTablas(List<String> nombresTablas) {
        modeloLista.clear();
        for (String nombre : nombresTablas) {
            modeloLista.addElement(nombre);
        }
        
        if (nombresTablas.isEmpty()) {
            modeloLista.addElement("(No hay tablas creadas)");
        }
    }
    
    public void mostrarResultado(String resultado) {
        areaResultados.append(resultado + "\n");
        areaResultados.setCaretPosition(areaResultados.getDocument().getLength());
    }
    
    public void limpiarResultados() {
        areaResultados.setText("");
    }
    
    // ===== MÉTODOS PARA OBTENER DATOS DE LA VISTA =====
    
    public String getTablaSeleccionada() {
        String seleccion = listaTablas.getSelectedValue();
        if (seleccion != null && seleccion.equals("(No hay tablas creadas)")) {
            return null;
        }
        return seleccion;
    }
    
    // ===== DIÁLOGO CREAR TABLA =====
    
    public void mostrarDialogoCrearTabla() {
        dialogCrearTabla = new JDialog();
        dialogCrearTabla.setTitle("Crear Tabla");
        dialogCrearTabla.setModal(true);
        dialogCrearTabla.setSize(550, 500);
        dialogCrearTabla.setLocationRelativeTo(this);
        dialogCrearTabla.setLayout(new BorderLayout(10, 10));
        
        // Panel de formulario
        JPanel panelForm = new JPanel(new BorderLayout(10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Nombre de la tabla
        JPanel panelNombre = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNombre.add(new JLabel("Nombre de la tabla:"));
        txtNombreTabla = new JTextField(25);
        txtNombreTabla.setFont(new Font("Arial", Font.PLAIN, 12));
        panelNombre.add(txtNombreTabla);
        
        // Campos de la tabla
        panelCampos = new JPanel();
        panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
        panelCampos.setBackground(Color.WHITE);
        
        JScrollPane scrollCampos = new JScrollPane(panelCampos);
        scrollCampos.setBorder(BorderFactory.createTitledBorder("Campos"));
        scrollCampos.setPreferredSize(new Dimension(500, 300));
        
        // Inicializar listas de campos
        camposNombre = new ArrayList<>();
        camposTipo = new ArrayList<>();
        camposPK = new ArrayList<>();
        
        // Agregar un campo inicial
        agregarFilaCampo();
        
        // Botones para agregar/eliminar campos
        JPanel panelCamposControl = new JPanel(new FlowLayout());
        
        JButton btnAgregarCampo = new JButton("+ Agregar Campo");
        btnAgregarCampo.setBackground(new Color(46, 204, 113));
        btnAgregarCampo.setForeground(Color.WHITE);
        btnAgregarCampo.setFont(new Font("Arial", Font.BOLD, 11));
        btnAgregarCampo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgregarCampo.addActionListener(e -> agregarFilaCampo());
        
        JButton btnEliminarUltimo = new JButton("- Eliminar Último Campo");
        btnEliminarUltimo.setBackground(new Color(231, 76, 60));
        btnEliminarUltimo.setForeground(Color.WHITE);
        btnEliminarUltimo.setFont(new Font("Arial", Font.BOLD, 11));
        btnEliminarUltimo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEliminarUltimo.addActionListener(e -> eliminarUltimaFilaCampo());
        
        panelCamposControl.add(btnAgregarCampo);
        panelCamposControl.add(btnEliminarUltimo);
        
        panelForm.add(panelNombre, BorderLayout.NORTH);
        panelForm.add(scrollCampos, BorderLayout.CENTER);
        panelForm.add(panelCamposControl, BorderLayout.SOUTH);
        
        // Botones de acción
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnAceptar = new JButton("Crear Tabla");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnAceptar.setBackground(new Color(46, 204, 113));
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.setFont(new Font("Arial", Font.BOLD, 12));
        btnAceptar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnCancelar.setBackground(new Color(149, 165, 166));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 12));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnAceptar.addActionListener(e -> {
            nombreTablaCreada = txtNombreTabla.getText().trim();
            camposCreados = new ArrayList<>();
            
            for (int i = 0; i < camposNombre.size(); i++) {
                String nombreCampo = camposNombre.get(i).getText().trim();
                if (!nombreCampo.isEmpty()) {
                    String tipo = (String) camposTipo.get(i).getSelectedItem();
                    boolean esPK = camposPK.get(i).isSelected();
                    camposCreados.add(new String[]{nombreCampo, tipo, String.valueOf(esPK)});
                }
            }
            
            dialogCrearTabla.dispose();
        });
        
        btnCancelar.addActionListener(e -> {
            nombreTablaCreada = null;
            camposCreados = null;
            dialogCrearTabla.dispose();
        });
        
        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);
        
        dialogCrearTabla.add(panelForm, BorderLayout.CENTER);
        dialogCrearTabla.add(panelBotones, BorderLayout.SOUTH);
        dialogCrearTabla.setVisible(true);
    }
    
    private void agregarFilaCampo() {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        fila.setBackground(Color.WHITE);
        
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 11));
        JTextField txtNombre = new JTextField(15);
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 11));
        
        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setFont(new Font("Arial", Font.PLAIN, 11));
        JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"INT", "TEXT", "REAL", "BOOL"});
        cmbTipo.setFont(new Font("Arial", Font.PLAIN, 11));
        
        JCheckBox chkPK = new JCheckBox("Clave Primaria (PK)");
        chkPK.setFont(new Font("Arial", Font.PLAIN, 11));
        chkPK.setBackground(Color.WHITE);
        
        fila.add(lblNombre);
        fila.add(txtNombre);
        fila.add(lblTipo);
        fila.add(cmbTipo);
        fila.add(chkPK);
        
        camposNombre.add(txtNombre);
        camposTipo.add(cmbTipo);
        camposPK.add(chkPK);
        
        panelCampos.add(fila);
        panelCampos.revalidate();
        panelCampos.repaint();
    }
    
    private void eliminarUltimaFilaCampo() {
        if (!camposNombre.isEmpty()) {
            // Eliminar el último componente del panel
            panelCampos.remove(panelCampos.getComponentCount() - 1);
            
            // Eliminar de las listas
            camposNombre.remove(camposNombre.size() - 1);
            camposTipo.remove(camposTipo.size() - 1);
            camposPK.remove(camposPK.size() - 1);
            
            panelCampos.revalidate();
            panelCampos.repaint();
        }
    }
    
    public String getNombreTablaCreada() {
        return nombreTablaCreada;
    }
    
    public List<String[]> getCamposCreados() {
        return camposCreados;
    }
    
    // ===== DIÁLOGO ELIMINAR TABLA =====
    
    private String tablaAEliminar;
    
    public void mostrarDialogoEliminarTabla(List<String> nombresTablas) {
        if (nombresTablas.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No hay tablas para eliminar.", 
                "Eliminar Tabla", 
                JOptionPane.WARNING_MESSAGE);
            tablaAEliminar = null;
            return;
        }
        
        String[] opciones = nombresTablas.toArray(new String[0]);
        String seleccion = (String) JOptionPane.showInputDialog(
            this,
            "Seleccione la tabla a eliminar:",
            "Eliminar Tabla",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]
        );
        
        if (seleccion != null) {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar la tabla '" + seleccion + "'?\n¡Todos los datos se perderán!",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                tablaAEliminar = seleccion;
            } else {
                tablaAEliminar = null;
            }
        } else {
            tablaAEliminar = null;
        }
    }
    
    public String getTablaAEliminar() {
        return tablaAEliminar;
    }
}