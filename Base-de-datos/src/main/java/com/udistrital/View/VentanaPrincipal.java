package com.udistrital.View;

import javax.swing.*;

import com.udistrital.Controller.Controller;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel panelContenedor;
    
    // Paneles
    private InicioPanel inicioPanel;
    private TablasPanel tablasPanel;
    private RegistrosPanel registrosPanel;
    private AVLPanel avlPanel;
    private AyudaPanel ayudaPanel;
    
    public VentanaPrincipal() {
        setTitle("MiniDB - Gestor de Base de Datos");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        
        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);
        
        // Crear los paneles (sin pasar el controlador)
        inicioPanel = new InicioPanel(cardLayout, panelContenedor);
        tablasPanel = new TablasPanel(cardLayout, panelContenedor);
        registrosPanel = new RegistrosPanel(cardLayout, panelContenedor);
        avlPanel = new AVLPanel(cardLayout, panelContenedor);
        ayudaPanel = new AyudaPanel(cardLayout, panelContenedor);
        
        // Agregar paneles al contenedor
        panelContenedor.add(inicioPanel, "inicio");
        panelContenedor.add(tablasPanel, "tablas");
        panelContenedor.add(registrosPanel, "registros");
        panelContenedor.add(avlPanel, "avl");
        panelContenedor.add(ayudaPanel, "ayuda");
        
        add(panelContenedor, BorderLayout.CENTER);
        
        // Mostrar pantalla de inicio
        cardLayout.show(panelContenedor, "inicio");
    }
    
    // ===== MÉTODOS PARA REGISTRAR LISTENERS (LLAMADOS POR EL CONTROLADOR) =====
    
    // TablasPanel
    public void addCrearTablaListener(ActionListener listener) {
        tablasPanel.addCrearTablaListener(listener);
    }
    
    public void addEliminarTablaListener(ActionListener listener) {
        tablasPanel.addEliminarTablaListener(listener);
    }
    
    public void addListarTablasListener(ActionListener listener) {
        tablasPanel.addListarTablasListener(listener);
    }
    
    public void addRefrescarTablasListener(ActionListener listener) {
        tablasPanel.addRefrescarListener(listener);
    }
    
    // RegistrosPanel
    public void addInsertarListener(ActionListener listener) {
        registrosPanel.addInsertarListener(listener);
    }
    
    public void addConsultarListener(ActionListener listener) {
        registrosPanel.addConsultarListener(listener);
    }
    
    public void addActualizarListener(ActionListener listener) {
        registrosPanel.addActualizarListener(listener);
    }
    
    public void addEliminarListener(ActionListener listener) {
        registrosPanel.addEliminarListener(listener);
    }
    
    public void addCargarDatasetListener(ActionListener listener) {
        registrosPanel.addCargarDatasetListener(listener);
    }
    
    // ===== MÉTODOS PARA ACTUALIZAR LA VISTA =====
    
    // TablasPanel
    public void actualizarListaTablas(List<String> nombresTablas) {
        tablasPanel.actualizarListaTablas(nombresTablas);
        registrosPanel.actualizarListaTablas(nombresTablas);
    }
    
    public void mostrarResultadoEnTablas(String resultado) {
        tablasPanel.mostrarResultado(resultado);
    }
    
    public void mostrarResultadoEnRegistros(String resultado) {
        registrosPanel.mostrarResultado(resultado);
    }
    
    public void mostrarResultado(String resultado) {
        // Mostrar en ambos paneles (o en el activo)
        tablasPanel.mostrarResultado(resultado);
        registrosPanel.mostrarResultado(resultado);
    }
    
    public void limpiarResultados() {
        tablasPanel.limpiarResultados();
        registrosPanel.limpiarResultados();
    }
    
    // AVLPanel
    public void setControllerParaAVL(Controller controller) {
        avlPanel.setController(controller);
    }
    
    public void mostrarPanelInicio() {
        cardLayout.show(panelContenedor, "inicio");
    }
    // ===== MÉTODOS PARA OBTENER DATOS DE LA VISTA =====
    
    // TablasPanel
    public String getTablaSeleccionada() {
        String seleccion = tablasPanel.getTablaSeleccionada();
        if (seleccion == null) {
            seleccion = registrosPanel.getTablaSeleccionada();
        }
        return seleccion;
    }
    
    public String getTablaSeleccionadaEnTablas() {
        return tablasPanel.getTablaSeleccionada();
    }
    
    public String getTablaSeleccionadaEnRegistros() {
        return registrosPanel.getTablaSeleccionada();
    }
    
    // Diálogo Crear Tabla
    public void mostrarDialogoCrearTabla() {
        tablasPanel.mostrarDialogoCrearTabla();
    }
    
    public String getNombreTablaCreada() {
        return tablasPanel.getNombreTablaCreada();
    }
    
    public List<String[]> getCamposCreados() {
        return tablasPanel.getCamposCreados();
    }
    
    // Diálogo Eliminar Tabla
    public void mostrarDialogoEliminarTabla(List<String> nombresTablas) {
        tablasPanel.mostrarDialogoEliminarTabla(nombresTablas);
    }
    
    public String getTablaAEliminar() {
        return tablasPanel.getTablaAEliminar();
    }
    
    // ===== REGISTROS =====
    
    // Insertar
    public void mostrarDialogoInsertar(String nombreTabla, List<String[]> campos) {
        registrosPanel.mostrarDialogoInsertar(nombreTabla, campos);
    }
    
    public List<String> getDatosInsertar() {
        return registrosPanel.getDatosInsertar();
    }
    
    // Consultar
    public void mostrarDialogoConsultar(String nombreTabla) {
        registrosPanel.mostrarDialogoConsultar(nombreTabla);
    }
    
    public int getOpcionConsultar() {
        return registrosPanel.getOpcionConsultar();
    }
    
    public String getIdConsultar() {
        return registrosPanel.getIdConsultar();
    }
    
    public String getCampoConsultar() {
        return registrosPanel.getCampoConsultar();
    }
    
    public String getValorConsultar() {
        return registrosPanel.getValorConsultar();
    }
    
    // Actualizar
    public void mostrarDialogoActualizar(String nombreTabla, List<String[]> campos) {
        registrosPanel.mostrarDialogoActualizar(nombreTabla, campos);
    }
    
    public String getIdActualizar() {
        return registrosPanel.getIdActualizar();
    }
    
    public List<String> getNuevosValores() {
        return registrosPanel.getNuevosValores();
    }
    
    // Eliminar
    public void mostrarDialogoEliminar(String nombreTabla) {
        registrosPanel.mostrarDialogoEliminar(nombreTabla);
    }
    
    public int getOpcionEliminar() {
        return registrosPanel.getOpcionEliminar();
    }
    
    public String getIdEliminar() {
        return registrosPanel.getIdEliminar();
    }
    
    public String getCampoEliminar() {
        return registrosPanel.getCampoEliminar();
    }
    
    public String getValorEliminar() {
        return registrosPanel.getValorEliminar();
    }
    
    public boolean isEliminarTodos() {
        return registrosPanel.isEliminarTodos();
    }
    
    // ===== MÉTODOS PARA MOSTRAR ESTRUCTURA DE TABLA =====
    
    public void mostrarEstructuraTabla(String nombreTabla, List<String[]> campos) {
        registrosPanel.mostrarEstructuraTabla(nombreTabla, campos);
    }
}