package com.udistrital.View;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class RegistrosPanel extends JPanel {
    
    private CardLayout cardLayout;
    private JPanel contenedor;
    
    // Componentes de la GUI
    private JComboBox<String> comboTablas;
    private JTextArea areaResultados;
    private JPanel panelCamposDinamicos;
    
    // Botones
    private JButton btnInsertar;
    private JButton btnConsultar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnCargarDataset;
    
    // Diálogos
    private JDialog dialogInsertar;
    private JDialog dialogConsultar;
    private JDialog dialogActualizar;
    private JDialog dialogEliminar;
    
    // Datos temporales para los diálogos
    private String tablaActual;
    private List<String> camposActuales;
    private List<String> tiposActuales;
    private boolean tienePK;
    
    public RegistrosPanel(CardLayout cardLayout, JPanel contenedor) {
        this.cardLayout = cardLayout;
        this.contenedor = contenedor;
        this.camposActuales = new ArrayList<>();
        this.tiposActuales  = new ArrayList<>();
        
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

    	JLabel titulo = new JLabel("Gestión de Registros", JLabel.CENTER);
    	titulo.setFont(new Font("Arial", Font.BOLD, 18));
    	titulo.setForeground(Color.WHITE);

    	panelCabecera.add(btnVolver, BorderLayout.WEST);
    	panelCabecera.add(titulo, BorderLayout.CENTER);

    	// ===== PANEL DE SELECCIÓN DE TABLA =====
    	JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
    	panelSeleccion.setBackground(Color.WHITE);
    	panelSeleccion.setBorder(BorderFactory.createTitledBorder(
    	    BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
    	    "Seleccionar Tabla",
    	    TitledBorder.LEFT,
    	    TitledBorder.TOP,
    	    new Font("Arial", Font.BOLD, 12),
    	    new Color(46, 204, 113)
    	));

    	comboTablas = new JComboBox<>();
    	comboTablas.setPreferredSize(new Dimension(200, 30));
    	comboTablas.setFont(new Font("Arial", Font.PLAIN, 12));

    	panelSeleccion.add(new JLabel("Tabla:"));
    	panelSeleccion.add(comboTablas);

    	// ← reemplaza el add(panelSeleccion...) por esto:
    	JPanel panelSuperior = new JPanel(new BorderLayout());
    	panelSuperior.add(panelCabecera, BorderLayout.NORTH);
    	panelSuperior.add(panelSeleccion, BorderLayout.CENTER);

    	add(panelSuperior, BorderLayout.NORTH);
        
        // ===== PANEL CENTRAL =====
        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        panelCentral.setBackground(Color.WHITE);
        
        // Panel de botones CRUD
        JPanel panelBotones = new JPanel(new GridLayout(1, 5, 15, 15));
        panelBotones.setBorder(BorderFactory.createTitledBorder("Operaciones CRUD"));
        panelBotones.setBackground(Color.WHITE);
        
        btnInsertar = new JButton("Insertar Registro");
        btnConsultar = new JButton("Consultar Registros");
        btnActualizar = new JButton("Actualizar Registro");
        btnEliminar = new JButton("Eliminar Registro");
        
        btnCargarDataset = new JButton("Cargar Dataset");
        btnCargarDataset.setBackground(new Color(155, 89, 182));
        btnCargarDataset.setForeground(Color.WHITE);
        btnCargarDataset.setFont(new Font("Arial", Font.BOLD, 12));
        btnCargarDataset.setFocusPainted(false);
        btnCargarDataset.setCursor(new Cursor(Cursor.HAND_CURSOR));

        configurarBoton(btnInsertar, new Color(46, 204, 113));
        configurarBoton(btnConsultar, new Color(52, 152, 219));
        configurarBoton(btnActualizar, new Color(241, 196, 15));
        configurarBoton(btnEliminar, new Color(231, 76, 60));
        configurarBoton(btnCargarDataset, new Color(90, 242, 221));

        panelBotones.add(btnInsertar);
        panelBotones.add(btnConsultar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCargarDataset);
        
        configurarBoton(btnInsertar, new Color(46, 204, 113));
        configurarBoton(btnConsultar, new Color(52, 152, 219));
        configurarBoton(btnActualizar, new Color(241, 196, 15));
        configurarBoton(btnEliminar, new Color(231, 76, 60));
        
        panelBotones.add(btnInsertar);
        panelBotones.add(btnConsultar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        
        // Panel de estructura de tabla
        panelCamposDinamicos = new JPanel();
        panelCamposDinamicos.setLayout(new BoxLayout(panelCamposDinamicos, BoxLayout.Y_AXIS));
        panelCamposDinamicos.setBackground(Color.WHITE);
        panelCamposDinamicos.setBorder(BorderFactory.createTitledBorder("Estructura de la Tabla"));
        
        JScrollPane scrollCampos = new JScrollPane(panelCamposDinamicos);
        scrollCampos.setPreferredSize(new Dimension(850, 120));
        
        // Área de resultados
        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaResultados.setBackground(new Color(252, 252, 252));
        areaResultados.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JScrollPane scrollResultados = new JScrollPane(areaResultados);
        scrollResultados.setBorder(BorderFactory.createTitledBorder("Resultados"));
        scrollResultados.setPreferredSize(new Dimension(850, 200));
        
        panelCentral.add(panelBotones, BorderLayout.NORTH);
        panelCentral.add(scrollCampos, BorderLayout.CENTER);
        
        add(panelCentral, BorderLayout.CENTER);
        add(scrollResultados, BorderLayout.SOUTH);
    }
    
    private void configurarBoton(JButton boton, Color color) {
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    // ===== MÉTODOS PARA REGISTRAR LISTENERS (LLAMADOS POR EL CONTROLADOR) =====
    
    public void addInsertarListener(ActionListener listener) {
        btnInsertar.addActionListener(listener);
    }
    
    public void addConsultarListener(ActionListener listener) {
        btnConsultar.addActionListener(listener);
    }
    
    public void addActualizarListener(ActionListener listener) {
        btnActualizar.addActionListener(listener);
    }
    
    public void addEliminarListener(ActionListener listener) {
        btnEliminar.addActionListener(listener);
    }
    
    public void addCargarDatasetListener(ActionListener listener) {
        btnCargarDataset.addActionListener(listener);
    }
    
    // ===== MÉTODOS PARA ACTUALIZAR LA VISTA =====
    
    public void actualizarListaTablas(List<String> nombresTablas) {
        comboTablas.removeAllItems();
        for (String nombre : nombresTablas) {
            comboTablas.addItem(nombre);
        }
        if (nombresTablas.isEmpty()) {
            panelCamposDinamicos.removeAll();
            panelCamposDinamicos.add(new JLabel("No hay tablas creadas. Use la opción TABLAS."));
            panelCamposDinamicos.revalidate();
            panelCamposDinamicos.repaint();
        }
    }
    
    public void mostrarEstructuraTabla(String nombreTabla, List<String[]> campos) {
        panelCamposDinamicos.removeAll();
        
        if (campos.isEmpty()) {
            panelCamposDinamicos.add(new JLabel("No hay campos en esta tabla."));
        } else {
            JPanel panel = new JPanel(new GridLayout(campos.size(), 3, 15, 8));
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            // Encabezados
            JLabel lblNombre = new JLabel("CAMPO");
            lblNombre.setFont(new Font("Arial", Font.BOLD, 12));
            lblNombre.setForeground(new Color(52, 73, 94));
            JLabel lblTipo = new JLabel("TIPO");
            lblTipo.setFont(new Font("Arial", Font.BOLD, 12));
            lblTipo.setForeground(new Color(52, 73, 94));
            JLabel lblPK = new JLabel("PK");
            lblPK.setFont(new Font("Arial", Font.BOLD, 12));
            lblPK.setForeground(new Color(52, 73, 94));
            
            panel.add(lblNombre);
            panel.add(lblTipo);
            panel.add(lblPK);
            
            // Datos
            for (String[] campo : campos) {
                JLabel lblNom = new JLabel(campo[0]);
                lblNom.setFont(new Font("Arial", Font.PLAIN, 12));
                JLabel lblTip = new JLabel(campo[1]);
                lblTip.setFont(new Font("Monospaced", Font.PLAIN, 12));
                JLabel lblPk = new JLabel(campo[2].equals("true") ? "✓ SI" : "");
                lblPk.setForeground(campo[2].equals("true") ? new Color(231, 76, 60) : Color.GRAY);
                
                panel.add(lblNom);
                panel.add(lblTip);
                panel.add(lblPk);
            }
            
            panelCamposDinamicos.add(panel);
        }
        
        panelCamposDinamicos.revalidate();
        panelCamposDinamicos.repaint();
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
        return (String) comboTablas.getSelectedItem();
    }
    
    // ===== DIÁLOGO INSERTAR =====
    
    public void mostrarDialogoInsertar(String nombreTabla, List<String[]> campos) {
        this.tablaActual = nombreTabla;
        this.camposActuales = new ArrayList<>();
        this.tiposActuales = new ArrayList<>();
        
        for (String[] campo : campos) {
            camposActuales.add(campo[0]);
            tiposActuales.add(campo[1]);
            if (campo[2].equals("true")) {
                tienePK = true;
            }
        }
        
        dialogInsertar = new JDialog();
        dialogInsertar.setTitle("Insertar Registro - " + nombreTabla);
        dialogInsertar.setModal(true);
        dialogInsertar.setSize(450, 400);
        dialogInsertar.setLocationRelativeTo(this);
        dialogInsertar.setLayout(new BorderLayout(10, 10));
        
        JPanel panelCampos = new JPanel(new GridLayout(camposActuales.size(), 2, 10, 10));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        java.util.Map<String, JTextField> camposTexto = new java.util.HashMap<>();
        
        for (int i = 0; i < camposActuales.size(); i++) {
            String nombre = camposActuales.get(i);
            String tipo = tiposActuales.get(i);
            
            JLabel label = new JLabel(nombre + " (" + tipo + "):");
            label.setFont(new Font("Arial", Font.BOLD, 12));
            panelCampos.add(label);
            
            JTextField txt = new JTextField();
            panelCampos.add(txt);
            camposTexto.put(nombre, txt);
        }
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnAceptar = new JButton("Insertar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnAceptar.setBackground(new Color(46, 204, 113));
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnCancelar.setBackground(new Color(149, 165, 166));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Almacenar datos temporalmente para que el Controlador los recupere
        btnAceptar.addActionListener(e -> {
            this.datosInsertar = new ArrayList<>();
            for (String nombre : camposActuales) {
                this.datosInsertar.add(camposTexto.get(nombre).getText());
            }
            dialogInsertar.dispose();
        });
        
        btnCancelar.addActionListener(e -> {
            this.datosInsertar = null;
            dialogInsertar.dispose();
        });
        
        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);
        
        dialogInsertar.add(panelCampos, BorderLayout.CENTER);
        dialogInsertar.add(panelBotones, BorderLayout.SOUTH);
        dialogInsertar.setVisible(true);
    }
    
    private List<String> datosInsertar;
    
    public List<String> getDatosInsertar() {
        return datosInsertar;
    }
    
    // ===== DIÁLOGO CONSULTAR =====
    
    private int opcionConsultar;
    private String idConsultar;
    private String campoConsultar;
    private String valorConsultar;
    
    public void mostrarDialogoConsultar(String nombreTabla) {
        dialogConsultar = new JDialog();
        dialogConsultar.setTitle("Consultar Registros - " + nombreTabla);
        dialogConsultar.setModal(true);
        dialogConsultar.setSize(450, 300);
        dialogConsultar.setLocationRelativeTo(this);
        dialogConsultar.setLayout(new BorderLayout(10, 10));
        
        JPanel panelOpciones = new JPanel();
        panelOpciones.setLayout(new BoxLayout(panelOpciones, BoxLayout.Y_AXIS));
        panelOpciones.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        ButtonGroup grupo = new ButtonGroup();
        JRadioButton rbTodos = new JRadioButton("Ver todos los registros", true);
        JRadioButton rbPorPK = new JRadioButton("Buscar por clave primaria (PK)");
        JRadioButton rbPorCampo = new JRadioButton("Buscar por campo específico");
        
        grupo.add(rbTodos);
        grupo.add(rbPorPK);
        grupo.add(rbPorCampo);
        
        JPanel panelPK = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPK.add(new JLabel("Valor de la PK:"));
        JTextField txtPK = new JTextField(15);
        panelPK.add(txtPK);
        panelPK.setVisible(false);
        
        JPanel panelCampo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCampo.add(new JLabel("Campo:"));
        List<String> campos = (camposActuales != null) ? camposActuales : new ArrayList<>();
        JComboBox<String> cmbCampo = new JComboBox<>(campos.toArray(new String[0]));
        panelCampo.add(cmbCampo);
        panelCampo.add(new JLabel("Valor:"));
        JTextField txtValor = new JTextField(15);
        panelCampo.add(txtValor);
        panelCampo.setVisible(false);
        
        rbPorPK.addActionListener(e -> {
            panelPK.setVisible(true);
            panelCampo.setVisible(false);
        });
        
        rbPorCampo.addActionListener(e -> {
            panelPK.setVisible(false);
            panelCampo.setVisible(true);
        });
        
        rbTodos.addActionListener(e -> {
            panelPK.setVisible(false);
            panelCampo.setVisible(false);
        });
        
        panelOpciones.add(rbTodos);
        panelOpciones.add(Box.createRigidArea(new Dimension(0, 10)));
        panelOpciones.add(rbPorPK);
        panelOpciones.add(panelPK);
        panelOpciones.add(Box.createRigidArea(new Dimension(0, 10)));
        panelOpciones.add(rbPorCampo);
        panelOpciones.add(panelCampo);
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnConsultar = new JButton("Consultar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnConsultar.setBackground(new Color(52, 152, 219));
        btnConsultar.setForeground(Color.WHITE);
        btnConsultar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnConsultar.addActionListener(e -> {
            if (rbTodos.isSelected()) {
                opcionConsultar = 0;
            } else if (rbPorPK.isSelected()) {
                opcionConsultar = 1;
                idConsultar = txtPK.getText().trim();
            } else {
                opcionConsultar = 2;
                campoConsultar = (String) cmbCampo.getSelectedItem();
                valorConsultar = txtValor.getText().trim();
            }
            dialogConsultar.dispose();
        });
        
        btnCancelar.addActionListener(e -> {
            opcionConsultar = -1;
            dialogConsultar.dispose();
        });
        
        panelBotones.add(btnConsultar);
        panelBotones.add(btnCancelar);
        
        dialogConsultar.add(panelOpciones, BorderLayout.CENTER);
        dialogConsultar.add(panelBotones, BorderLayout.SOUTH);
        dialogConsultar.setVisible(true);
    }
    
    public int getOpcionConsultar() {
        return opcionConsultar;
    }
    
    public String getIdConsultar() {
        return idConsultar;
    }
    
    public String getCampoConsultar() {
        return campoConsultar;
    }
    
    public String getValorConsultar() {
        return valorConsultar;
    }
    
    // ===== DIÁLOGO ACTUALIZAR =====
    
    private String idActualizar;
    private List<String> nuevosValores;
    
    public void mostrarDialogoActualizar(String nombreTabla, List<String[]> campos) {
        dialogActualizar = new JDialog();
        dialogActualizar.setTitle("Actualizar Registro - " + nombreTabla);
        dialogActualizar.setModal(true);
        dialogActualizar.setSize(450, 400);
        dialogActualizar.setLocationRelativeTo(this);
        dialogActualizar.setLayout(new BorderLayout(10, 10));
        
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel panelPK = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPK.add(new JLabel("ID del registro a actualizar:"));
        JTextField txtPK = new JTextField(15);
        panelPK.add(txtPK);
        
        JPanel panelCampos = new JPanel(new GridLayout(campos.size(), 2, 10, 10));
        panelCampos.setBorder(BorderFactory.createTitledBorder("Nuevos valores (dejar vacío para no modificar)"));
        
        java.util.Map<String, JTextField> camposTexto = new java.util.HashMap<>();
        
        for (String[] campo : campos) {
            if (!campo[2].equals("true")) { // No incluir PK
                JLabel label = new JLabel(campo[0] + " (" + campo[1] + "):");
                label.setFont(new Font("Arial", Font.PLAIN, 12));
                panelCampos.add(label);
                JTextField txt = new JTextField();
                panelCampos.add(txt);
                camposTexto.put(campo[0], txt);
            }
        }
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnActualizar.setBackground(new Color(241, 196, 15));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnActualizar.addActionListener(e -> {
            idActualizar = txtPK.getText().trim();
            nuevosValores = new ArrayList<>();
            for (String nombre : camposTexto.keySet()) {
                nuevosValores.add(camposTexto.get(nombre).getText());
            }
            dialogActualizar.dispose();
        });
        
        btnCancelar.addActionListener(e -> {
            idActualizar = null;
            dialogActualizar.dispose();
        });
        
        panelBotones.add(btnActualizar);
        panelBotones.add(btnCancelar);
        
        panelPrincipal.add(panelPK, BorderLayout.NORTH);
        panelPrincipal.add(panelCampos, BorderLayout.CENTER);
        
        dialogActualizar.add(panelPrincipal, BorderLayout.CENTER);
        dialogActualizar.add(panelBotones, BorderLayout.SOUTH);
        dialogActualizar.setVisible(true);
    }
    
    public String getIdActualizar() {
        return idActualizar;
    }
    
    public List<String> getNuevosValores() {
        return nuevosValores;
    }
    
    // ===== DIÁLOGO ELIMINAR =====
    
    private int opcionEliminar;
    private String idEliminar;
    private String campoEliminar;
    private String valorEliminar;
    private boolean eliminarTodos;
    
    public void mostrarDialogoEliminar(String nombreTabla) {
        dialogEliminar = new JDialog();
        dialogEliminar.setTitle("Eliminar Registros - " + nombreTabla);
        dialogEliminar.setModal(true);
        dialogEliminar.setSize(450, 320);
        dialogEliminar.setLocationRelativeTo(this);
        dialogEliminar.setLayout(new BorderLayout(10, 10));
        
        JPanel panelOpciones = new JPanel();
        panelOpciones.setLayout(new BoxLayout(panelOpciones, BoxLayout.Y_AXIS));
        panelOpciones.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        ButtonGroup grupo = new ButtonGroup();
        JRadioButton rbPorPK = new JRadioButton("Eliminar por clave primaria (PK)", true);
        JRadioButton rbPorCampo = new JRadioButton("Eliminar por campo específico");
        JRadioButton rbTodos = new JRadioButton("Eliminar TODOS los registros");
        
        grupo.add(rbPorPK);
        grupo.add(rbPorCampo);
        grupo.add(rbTodos);
        
        JPanel panelPK = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPK.add(new JLabel("Valor de la PK:"));
        JTextField txtPK = new JTextField(15);
        panelPK.add(txtPK);
        
        JPanel panelCampo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCampo.add(new JLabel("Campo:"));
        List<String> campos = (camposActuales != null) ? camposActuales : new ArrayList<>();
        JComboBox<String> cmbCampo = new JComboBox<>(campos.toArray(new String[0]));
        panelCampo.add(cmbCampo);
        panelCampo.add(new JLabel("Valor:"));
        JTextField txtValor = new JTextField(15);
        panelCampo.add(txtValor);
        
        rbPorCampo.addActionListener(e -> {
            panelPK.setVisible(false);
            panelCampo.setVisible(true);
        });
        
        rbPorPK.addActionListener(e -> {
            panelPK.setVisible(true);
            panelCampo.setVisible(false);
        });
        
        rbTodos.addActionListener(e -> {
            panelPK.setVisible(false);
            panelCampo.setVisible(false);
        });
        
        panelPK.setVisible(true);
        panelCampo.setVisible(false);
        
        panelOpciones.add(rbPorPK);
        panelOpciones.add(panelPK);
        panelOpciones.add(Box.createRigidArea(new Dimension(0, 10)));
        panelOpciones.add(rbPorCampo);
        panelOpciones.add(panelCampo);
        panelOpciones.add(Box.createRigidArea(new Dimension(0, 10)));
        panelOpciones.add(rbTodos);
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnEliminar.addActionListener(e -> {
            if (rbPorPK.isSelected()) {
                opcionEliminar = 1;
                idEliminar = txtPK.getText().trim();
                eliminarTodos = false;
            } else if (rbPorCampo.isSelected()) {
                opcionEliminar = 2;
                campoEliminar = (String) cmbCampo.getSelectedItem();
                valorEliminar = txtValor.getText().trim();
                eliminarTodos = false;
            } else {
                opcionEliminar = 3;
                eliminarTodos = true;
            }
            dialogEliminar.dispose();
        });
        
        btnCancelar.addActionListener(e -> {
            opcionEliminar = -1;
            dialogEliminar.dispose();
        });
        
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCancelar);
        
        // Advertencia
        JTextArea advertencia = new JTextArea("⚠ ADVERTENCIA: Esta acción no se puede deshacer");
        advertencia.setFont(new Font("Arial", Font.BOLD, 12));
        advertencia.setForeground(new Color(231, 76, 60));
        advertencia.setBackground(new Color(255, 240, 240));
        advertencia.setEditable(false);
        advertencia.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        dialogEliminar.add(panelOpciones, BorderLayout.CENTER);
        dialogEliminar.add(advertencia, BorderLayout.NORTH);
        dialogEliminar.add(panelBotones, BorderLayout.SOUTH);
        dialogEliminar.setVisible(true);
    }
    
    public int getOpcionEliminar() {
        return opcionEliminar;
    }
    
    public String getIdEliminar() {
        return idEliminar;
    }
    
    public String getCampoEliminar() {
        return campoEliminar;
    }
    
    public String getValorEliminar() {
        return valorEliminar;
    }
    
    public boolean isEliminarTodos() {
        return eliminarTodos;
    }
}  