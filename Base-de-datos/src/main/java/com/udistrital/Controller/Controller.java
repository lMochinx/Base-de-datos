package com.udistrital.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.udistrital.Model.ArbolAVL;
import com.udistrital.Model.Campo;
import com.udistrital.Model.Fila;
import com.udistrital.Model.Persistencia;
import com.udistrital.Model.Tabla;
import com.udistrital.View.Consola;
import com.udistrital.View.VentanaPrincipal;

public class Controller {
	
	private VentanaPrincipal vista;
	private Map<String, Tabla> tablas = new HashMap<>();
	private Consola consola;
	private static final Set<String> tiposPermitidos = Set.of("INT", "TEXT", "REAL", "BOOL");
	
	
	public Controller() {
		consola = new Consola();
		this.tablas = Persistencia.cargarTodas();
	    if (!tablas.isEmpty())
	        consola.mostrarMensaje(">> Tablas restauradas: " + tablas.keySet());
		iniciar();
	}
	
	// Constructor para GUI (nuevo)
	public Controller(VentanaPrincipal vista) {
	    this.vista = vista;
	    this.tablas = Persistencia.cargarTodas();
	    registrarListeners();
	    actualizarVistaTablas();
	    
	 // Mostrar mensaje si hay tablas restauradas
	    if (!tablas.isEmpty())
	        vista.mostrarResultadoEnTablas(">> Tablas restauradas: " + tablas.keySet());
	}

	private void registrarListeners() {
	    // Tablas
	    vista.addCrearTablaListener(e -> guiCrearTabla());
	    vista.addEliminarTablaListener(e -> guiEliminarTabla());
	    vista.addListarTablasListener(e -> guiListarTablas());
	    vista.addRefrescarTablasListener(e -> actualizarVistaTablas());

	    // Registros
	    vista.addInsertarListener(e -> guiInsertar());
	    vista.addConsultarListener(e -> guiConsultar());
	    vista.addActualizarListener(e -> guiActualizar());
	    vista.addEliminarListener(e -> guiEliminarRegistro());
	    vista.addCargarDatasetListener(e -> mostrarDialogoCargarDataset());
	}
	
	private void mostrarDialogoCargarDataset() {
	    String[] opciones = {"Dataset Pequeño (5 registros)", "Dataset Mediano (30 registros)", "Cancelar"};
	    int seleccion = javax.swing.JOptionPane.showOptionDialog(
	        null,
	        "Seleccione el dataset de prueba:",
	        "Cargar Datos de Prueba",
	        javax.swing.JOptionPane.DEFAULT_OPTION,
	        javax.swing.JOptionPane.QUESTION_MESSAGE,
	        null,
	        opciones,
	        opciones[0]
	    );
	    
	    if (seleccion == 0) {
	        cargarDataset(1);  // Dataset pequeño
	    } else if (seleccion == 1) {
	        cargarDataset(2);  // Dataset mediano
	    }
	}
	
	public void iniciar() {
        consola.mostrarMensaje("MiniDB v1  — escribe HELP para ayuda");
        while (true) {
            consola.mostrarMensaje("db> ");
            String linea = consola.leerString().trim();
            if (linea.isEmpty()) continue;
            if (linea.equalsIgnoreCase("EXIT")) break;
            consola.mostrarMensaje(procesar(linea));
        }
    }

    private String procesar(String cmd) {
        String upper = cmd.toUpperCase();

        if (upper.startsWith("CREATE TABLE")) return parsearCreate(cmd);
        if (upper.startsWith("DROP TABLE"))   return parsearDrop(cmd);
        if (upper.equalsIgnoreCase("SHOW TABLES")) return listar();
        if (upper.equalsIgnoreCase("HELP"))    return ayuda();
        if (upper.startsWith("INSERT INTO"))    return parsearInsert(cmd);
        if (upper.startsWith("SELECT * FROM"))  return parsearSelect(cmd);
        if (upper.startsWith("UPDATE"))         return parsearUpdate(cmd);
        if (upper.startsWith("DELETE FROM"))    return parsearDelete(cmd);

        return "Comando no reconocido. Escribe HELP.";
    }

    //Todos los parsear
    //Descomponen el texto del usuario en partes en donde java lo pueda entender para procesar la instruccion.
    private String parsearInsert(String cmd) {
        try {
            String sin   = cmd.substring("INSERT INTO".length()).trim();
            int p1       = sin.indexOf('(');
            String tabla = sin.substring(0, p1).trim();
            int p2       = sin.indexOf(')');
            String[] cols = sin.substring(p1 + 1, p2).split(",");

            String valPart = sin.substring(sin.toUpperCase().indexOf("VALUES") + 6).trim();
            String[] vals  = valPart.substring(valPart.indexOf('(') + 1, valPart.lastIndexOf(')')).split(",");

            List<String> columnas = Arrays.stream(cols).map(String::trim).toList();
            List<String> valores  = Arrays.stream(vals).map(String::trim).toList();
            return insertar(tabla, columnas, valores);
        } catch (Exception e) {
            return "ERROR de sintaxis. Uso: INSERT INTO tabla (col1, col2) VALUES (v1, v2)";
        }
    }

    private String parsearSelect(String cmd) {
        try {
            String sin = cmd.substring("SELECT * FROM".length()).trim();
            int wi     = sin.toUpperCase().indexOf("WHERE");
            if (wi != -1) {
                String tabla = sin.substring(0, wi).trim();
                String[] tok = sin.substring(wi + 5).trim().split("=", 2);
                return seleccionar(tabla, tok[0].trim(), tok[1].trim());
            }
            return seleccionar(sin, null, null); // sin condición
        } catch (Exception e) {
            return "ERROR de sintaxis. Uso: SELECT * FROM tabla [WHERE campo = valor]";
        }
    }

    private String parsearUpdate(String cmd) {
        try {
            String sin = cmd.substring("UPDATE".length()).trim();
            int si     = sin.toUpperCase().indexOf(" SET ");
            int wi     = sin.toUpperCase().indexOf(" WHERE ");
            String tabla  = sin.substring(0, si).trim();
            String[] setCols = sin.substring(si + 5, wi == -1 ? sin.length() : wi).trim().split("=", 2);

            String condCampo = null, condValor = null;
            if (wi != -1) {
                String[] condCols = sin.substring(wi + 7).trim().split("=", 2);
                condCampo = condCols[0].trim();
                condValor = condCols[1].trim();
            }
            return actualizar(tabla, setCols[0].trim(), setCols[1].trim(), condCampo, condValor);
        } catch (Exception e) {
            return "ERROR de sintaxis. Uso: UPDATE tabla SET campo = valor [WHERE campo = valor]";
        }
    }

    private String parsearDelete(String cmd) {
        try {
            String sin = cmd.substring("DELETE FROM".length()).trim();
            int wi     = sin.toUpperCase().indexOf("WHERE");
            if (wi != -1) {
                String tabla = sin.substring(0, wi).trim();
                String cond  = sin.substring(wi + 5).trim();
                
                String[] tok = cond.split("\\s*=\\s*", 2);
                return eliminar(tabla, tok[0].trim(), tok[1].trim());
            }
            return eliminar(sin.trim(), null, null);
        } catch (Exception e) {
            consola.mostrarMensaje("DEBUG excepcion: " + e.getMessage());
            return "ERROR de sintaxis. Uso: DELETE FROM tabla WHERE campo = valor";
        }
    }
    
    private String parsearCreate(String cmd) {
        try {
            String sin = cmd.substring("CREATE TABLE".length()).trim();
            int paren  = sin.indexOf('(');
            String nom = sin.substring(0, paren).trim();
            String def = sin.substring(paren + 1, sin.lastIndexOf(')')).trim();

            List<Campo> campos = new ArrayList<>();
            for (String parte : def.split(",")) {
                String[] tok = parte.trim().split("\\s+");
                boolean pk   = tok.length >= 3 && tok[2].equalsIgnoreCase("PK");
                campos.add(new Campo(tok[0], tok[1].toUpperCase(), pk));
            }
            return crearTabla(nom, campos);
        } catch (Exception e) {
            return "ERROR de sintaxis. Uso: CREATE TABLE nombre (campo TIPO [PK], ...)";
        }
    }

    private String parsearDrop(String cmd) {
        String nom = cmd.substring("DROP TABLE".length()).trim();
        return eliminarTabla(nom);
    }
    
    //Lista todas las tablas registradas en el gestor junto con
    //la definición de sus campos, tipo de dato y si son clave primaria.
    private String listar() {
    	if (listarTablas().isEmpty()) return "(ninguna tabla creada)";
        
        StringBuilder sb = new StringBuilder();
        for (String nombre : listarTablas()) {
            Tabla tabla = obtenerTabla(nombre);
            sb.append("► ").append(nombre).append("\n");
            for (Campo campo : tabla.campos) {
                sb.append("   - ")
                  .append(campo.nombre)
                  .append(" (").append(campo.tipo).append(")")
                  .append(campo.esPK ? " PK" : "")
                  .append("\n");
            }
        }
        return sb.toString().trim();
    }

    //Muestra los comandos disponibles para usar en la bd.
    private String ayuda() {
        return """
               Comandos disponibles:
                 CREATE TABLE nombre (campo TIPO [PK], ...)
                 DROP TABLE nombre
                 SHOW TABLES
                 INSERT INTO nombre (var1, var2, var3) VALUES (1, 'Ana', 25)
                 SELECT * FROM nombre
                 SELECT * FROM nombre WHERE condicion
                 UPDATE nombre SET nombre2 = 'x'
                 UPDATE nombre SET nombre2 = 'x' WHERE condicion
                 DELETE FROM nombre
                 DELETE FROM nombre WHERE condicion
                 EXIT
               Tipos: INT, TEXT, REAL, BOOL""";
    }
	
    
    //metodos mas 
    public String crearTabla(String nombre, List<Campo> campos) {
        if (tablas.containsKey(nombre))
            return "ERROR: la tabla '" + nombre + "' ya existe.";
        
        // Valida tipos de cada campo
        for (Campo campo : campos) {
            if (!tiposPermitidos.contains(campo.tipo.toUpperCase()))
                return "ERROR: tipo '" + campo.tipo + "' no permitido en campo '" + campo.nombre + "'. Tipos válidos: INT, TEXT, REAL, BOOL";
        }
        
        tablas.put(nombre, new Tabla(nombre, campos));
        Persistencia.guardarTabla(tablas.get(nombre));
        return "Tabla '" + nombre + "' creada.";
    }

    public String eliminarTabla(String nombre) {
        if (!tablas.containsKey(nombre))
            return "ERROR: la tabla '" + nombre + "' no existe.";
        tablas.remove(nombre);
        Persistencia.eliminarArchivo(nombre);
        return "Tabla '" + nombre + "' eliminada.";
    }

    public Tabla obtenerTabla(String nombre) {
        return tablas.get(nombre);
    }

    public Set<String> listarTablas() {
        return tablas.keySet();
    }
    
    public String insertar(String nombreTabla, List<String> columnas, List<String> valores) {
        Tabla tabla = obtenerTabla(nombreTabla);
        if (tabla == null)
            return "ERROR: la tabla '" + nombreTabla + "' no existe.";
        
        if (columnas.size() != valores.size())
            return "ERROR: numero de columnas y valores no coincide.";

        Map<String, Object> datos = new LinkedHashMap<>();
        Object claveValor = null;
        int claveHash = -1;

        for (int i = 0; i < columnas.size(); i++) {
            String nombreCampo = columnas.get(i);
            Campo campo = tabla.campos.stream()
                .filter(c -> c.nombre.equalsIgnoreCase(nombreCampo))
                .findFirst().orElse(null);

            if (campo == null)
                return "ERROR: el campo '" + nombreCampo + "' no existe en la tabla.";

            Object valorTipado = parsearValor(valores.get(i), campo.tipo);
            if (valorTipado == null)
                return "ERROR: el valor '" + valores.get(i) + "' no es válido para tipo " + campo.tipo;

            datos.put(campo.nombre, valorTipado);

            if (campo.esPK) {
                if (valorTipado instanceof Boolean)
                    return "ERROR: el campo PK no puede ser de tipo BOOL.";

                claveValor = valorTipado;

                claveHash = obtenerHash(valorTipado); 
            }
        }

        if (claveValor == null)
            return "ERROR: debes incluir el campo PK en el INSERT.";

        // Verifica que la clave no exista
        if (tabla.indice.buscar(claveHash) != null)
            return "ERROR: ya existe una fila con clave " + claveValor + ".";

        tabla.indice.insertar(claveHash, new Fila(datos));
        Persistencia.guardarTabla(tabla);
        return "Fila insertada en '" + nombreTabla + "'.";
    }

    public String seleccionar(String nombreTabla, String campoCond, String valorCond) {
        Tabla tabla = obtenerTabla(nombreTabla);
        if (tabla == null)
            return "ERROR: la tabla '" + nombreTabla + "' no existe.";

        List<Fila> filas = tabla.indice.inOrdenFilas();
        if (filas.isEmpty()) return "(tabla vacía)";

        // Sin WHERE devuelve todo, con WHERE filtra
        StringBuilder sb = new StringBuilder();
        for (Fila fila : filas) {
            if (campoCond == null || coincide(fila, campoCond, valorCond))
                sb.append(fila).append("\n");
        }
        return sb.length() == 0 ? "(sin resultados)" : sb.toString().trim();
    }

    public String actualizar(String nombreTabla, String campoSet, String nuevoValor, String campoCond, String valorCond) {
    	Tabla tabla = obtenerTabla(nombreTabla);
        if (tabla == null)
            return "ERROR: la tabla '" + nombreTabla + "' no existe.";

        Campo campo = tabla.campos.stream()
            .filter(c -> c.nombre.equalsIgnoreCase(campoSet))
            .findFirst().orElse(null);
        if (campo == null) return "ERROR: campo '" + campoSet + "' no existe.";

        Object valorTipado = parsearValor(nuevoValor, campo.tipo);
        if (valorTipado == null) return "ERROR: valor inválido para tipo " + campo.tipo;

        List<Fila> filas = tabla.indice.inOrdenFilas();
        int count = 0;
        for (Fila fila : filas) {
            if (campoCond == null || coincide(fila, campoCond, valorCond)) { // ← el fix
                fila.datos.put(campo.nombre, valorTipado);
                count++;
            }
        }
        Persistencia.guardarTabla(tabla);
        return count + " fila(s) actualizada(s).";
    }

    public String eliminar(String nombreTabla, String campoCond, String valorCond) {
        Tabla tabla = obtenerTabla(nombreTabla);
        if (tabla == null)
            return "ERROR: la tabla '" + nombreTabla + "' no existe.";

        if (campoCond == null) {
            tabla.indice = new ArbolAVL();
            Persistencia.guardarTabla(tabla);
            return "Todas las filas eliminadas de '" + nombreTabla + "'.";
        }

        Campo pk = tabla.campos.stream().filter(c -> c.esPK).findFirst().orElse(null);

        if (pk != null && pk.nombre.equalsIgnoreCase(campoCond)) {
            Object valorTipado = parsearValor(valorCond, pk.tipo); // ← limpia comillas y respeta tipo
            if (valorTipado == null)
                return "ERROR: valor inválido para tipo " + pk.tipo;

            int claveHash = obtenerHash(valorTipado);
            if (tabla.indice.buscar(claveHash) == null)
                return "ERROR: no existe fila con " + campoCond + " = " + valorCond;

            tabla.indice.eliminar(claveHash);
            Persistencia.guardarTabla(tabla);
            return "Fila eliminada.";
        }

        // Condición sobre campo no-PK
        List<Fila> filas = tabla.indice.inOrdenFilas();
        List<Integer> clavesAEliminar = new ArrayList<>();
        for (Fila fila : filas) {
            if (coincide(fila, campoCond, valorCond)) {
                if (pk != null) {
                    Object valorPK = fila.datos.get(pk.nombre);
                    clavesAEliminar.add(obtenerHash(valorPK)); // ← hash en lugar de cast
                }
            }
        }
        if (clavesAEliminar.isEmpty()) return "(ninguna fila coincide)";
        clavesAEliminar.forEach(c -> tabla.indice.eliminar(c));
        return clavesAEliminar.size() + " fila(s) eliminada(s).";
    }

    // quita comillas simples y otros signos.
    private Object parsearValor(String raw, String tipo) {
        String v = raw.trim().replaceAll("^'|'$", ""); 
        try {
            return switch (tipo) {
                case "INT"  -> Integer.parseInt(v);
                case "REAL" -> Double.parseDouble(v);
                case "BOOL" -> {
                    if (v.equalsIgnoreCase("true")  || v.equals("1")) yield true;
                    if (v.equalsIgnoreCase("false") || v.equals("0")) yield false;
                    yield null;
                }
                case "TEXT" -> v;
                default     -> null;
            };
        } catch (NumberFormatException e) {
            return null;
        }
    }

    //dado un campo y un valor, revisa si esa fila los tiene
    private boolean coincide(Fila fila, String campo, String valorRaw) {
        Object actual = fila.datos.get(campo);
        //no sabe nada de null
        if (actual == null) return false;
        String valorLimpio = valorRaw.trim().replaceAll("^'|'$", "");
        return actual.toString().equalsIgnoreCase(valorLimpio);
    }
    
    // Agrega este helper en MotorCRUD
    private int obtenerHash(Object valor) {
    	if (valor instanceof Integer) return (Integer) valor;
        if (valor instanceof Double)  return valor.hashCode();
        if (valor instanceof String)  return valor.hashCode();
        return valor.hashCode();
    }
	
	 private void guiCrearTabla() {
	     vista.mostrarDialogoCrearTabla();
	     String nombre = vista.getNombreTablaCreada();
	     List<String[]> camposData = vista.getCamposCreados();
	
	     if (nombre == null || nombre.isEmpty()) return;
	     if (camposData == null || camposData.isEmpty()) {
	         vista.mostrarResultadoEnTablas("ERROR: debe agregar al menos un campo.");
	         return;
	     }
	
	     List<Campo> campos = new ArrayList<>();
	     for (String[] d : camposData) {
	         boolean esPK = Boolean.parseBoolean(d[2]);
	         if (esPK && d[1].equalsIgnoreCase("BOOL")) { // ← validación nueva
	             vista.mostrarResultadoEnTablas("ERROR: el campo PK no puede ser de tipo BOOL.");
	             return;
	         }
	         campos.add(new Campo(d[0], d[1], esPK));
	     }
	
	     String resultado = crearTabla(nombre, campos); // ← reutiliza lógica existente
	     vista.mostrarResultadoEnTablas(resultado);
	     actualizarVistaTablas();
	 }
	
	 private void guiEliminarTabla() {
	     vista.mostrarDialogoEliminarTabla(new ArrayList<>(tablas.keySet()));
	     String nombre = vista.getTablaAEliminar();
	     if (nombre == null) return;
	
	     String resultado = eliminarTabla(nombre); // ← reutiliza lógica existente
	     vista.mostrarResultadoEnTablas(resultado);
	     actualizarVistaTablas();
	 }
	
	 private void guiListarTablas() {
	     vista.mostrarResultadoEnTablas(listar()); // ← reutiliza lógica existente
	 }
	
	 private void guiInsertar() {
	     String nombreTabla = vista.getTablaSeleccionadaEnRegistros();
	     if (nombreTabla == null) {
	         vista.mostrarResultadoEnRegistros("ERROR: selecciona una tabla primero.");
	         return;
	     }
	     Tabla tabla = obtenerTabla(nombreTabla);
	
	     // Preparar campos para el diálogo
	     List<String[]> camposData = tabla.campos.stream()
	         .map(c -> new String[]{c.nombre, c.tipo, String.valueOf(c.esPK)})
	         .toList();
	
	     vista.mostrarDialogoInsertar(nombreTabla, camposData);
	     List<String> valores = vista.getDatosInsertar();
	     if (valores == null) return;
	
	     // Columnas en el mismo orden que los campos de la tabla
	     List<String> columnas = tabla.campos.stream().map(c -> c.nombre).toList();
	
	     String resultado = insertar(nombreTabla, columnas, valores); // ← reutiliza lógica existente
	     vista.mostrarResultadoEnRegistros(resultado);
	     actualizarVistaTablas();
	 }
	
	 private void guiConsultar() {
		    String nombreTabla = vista.getTablaSeleccionadaEnRegistros();
		    if (nombreTabla == null || nombreTabla.isEmpty()) {
		        vista.mostrarResultadoEnRegistros("ERROR: selecciona una tabla primero.");
		        return;
		    }

		    Tabla tabla = obtenerTabla(nombreTabla);
		    if (tabla == null) {
		        vista.mostrarResultadoEnRegistros("ERROR: la tabla '" + nombreTabla + "' no existe.");
		        return;
		    }
		    
		    List<String[]> camposData = tabla.campos.stream()
		            .map(c -> new String[]{c.nombre, c.tipo, String.valueOf(c.esPK)})
		            .toList();
		        vista.mostrarEstructuraTabla(nombreTabla, camposData);

		    vista.mostrarDialogoConsultar(nombreTabla);
		    int opcion = vista.getOpcionConsultar();
		    if (opcion == -1) return;

		    String resultado;
		    switch (opcion) {
		        case 0 -> resultado = seleccionar(nombreTabla, null, null);
		        case 1 -> {
		            Campo pk = tabla.campos.stream()
		                .filter(c -> c.esPK)
		                .findFirst().orElse(null);
		            String campoPK = pk != null ? pk.nombre : "";
		            resultado = seleccionar(nombreTabla, campoPK, vista.getIdConsultar());
		        }
		        case 2 -> resultado = seleccionar(nombreTabla,
		                    vista.getCampoConsultar(),
		                    vista.getValorConsultar());
		        default -> resultado = "Opción no válida.";
		    }
		    vista.mostrarResultadoEnRegistros(resultado);
		}
	
	 private void guiActualizar() {
	     String nombreTabla = vista.getTablaSeleccionadaEnRegistros();
	     if (nombreTabla == null) {
	         vista.mostrarResultadoEnRegistros("ERROR: selecciona una tabla primero.");
	         return;
	     }
	     Tabla tabla = obtenerTabla(nombreTabla);
	
	     List<String[]> camposData = tabla.campos.stream()
	         .map(c -> new String[]{c.nombre, c.tipo, String.valueOf(c.esPK)})
	         .toList();
	
	     vista.mostrarDialogoActualizar(nombreTabla, camposData);
	     String idPK     = vista.getIdActualizar();
	     List<String> nuevosValores = vista.getNuevosValores();
	     if (idPK == null || idPK.isEmpty()) return;
	
	     // Buscar la PK
	     Campo pk = tabla.campos.stream().filter(c -> c.esPK).findFirst().orElse(null);
	     if (pk == null) { vista.mostrarResultadoEnRegistros("ERROR: tabla sin PK."); return; }
	
	     // Actualizar campo por campo (solo los que no estén vacíos)
	     List<Campo> camposNoP = tabla.campos.stream().filter(c -> !c.esPK).toList();
	     StringBuilder sb = new StringBuilder();
	     for (int i = 0; i < camposNoP.size() && i < nuevosValores.size(); i++) {
	         String nuevoVal = nuevosValores.get(i);
	         if (nuevoVal == null || nuevoVal.isEmpty()) continue;
	         String resultado = actualizar(nombreTabla,
	             camposNoP.get(i).nombre, nuevoVal,
	             pk.nombre, idPK); // ← reutiliza lógica existente
	         sb.append(resultado).append("\n");
	     }
	     vista.mostrarResultadoEnRegistros(sb.toString().trim());
	     actualizarVistaTablas();
	 }
	
	 private void guiEliminarRegistro() {
		 String nombreTabla = vista.getTablaSeleccionadaEnRegistros();
		    if (nombreTabla == null || nombreTabla.isEmpty()) {
		        vista.mostrarResultadoEnRegistros("ERROR: selecciona una tabla primero.");
		        return;
		    }
		    Tabla tabla = obtenerTabla(nombreTabla);
		    if (tabla == null) {
		        vista.mostrarResultadoEnRegistros("ERROR: la tabla no existe.");
		        return;
		    }
	
	     // Poblar camposActuales antes de abrir el diálogo
	     List<String[]> camposData = tabla.campos.stream()
	         .map(c -> new String[]{c.nombre, c.tipo, String.valueOf(c.esPK)})
	         .toList();
	     vista.mostrarEstructuraTabla(nombreTabla, camposData);
	
	     vista.mostrarDialogoEliminar(nombreTabla);
	     int opcion = vista.getOpcionEliminar();
	     if (opcion == -1) return;
	
	     String resultado;
	     Campo pk = tabla.campos.stream().filter(c -> c.esPK).findFirst().orElse(null);
	     switch (opcion) {
	         case 1 -> resultado = eliminar(nombreTabla,
	                     pk != null ? pk.nombre : "",
	                     vista.getIdEliminar());
	         case 2 -> resultado = eliminar(nombreTabla,
	                     vista.getCampoEliminar(),
	                     vista.getValorEliminar());
	         case 3 -> resultado = eliminar(nombreTabla, null, null);
	         default -> resultado = "Opción no válida.";
	     }
	     vista.mostrarResultadoEnRegistros(resultado); // ← reutiliza lógica existente
	     actualizarVistaTablas();
	 }
	
	 // Actualiza la lista de tablas y estructura en todos los paneles
	 private void actualizarVistaTablas() {
	     if (vista == null) return;
	     List<String> nombres = new ArrayList<>(tablas.keySet());
	     vista.actualizarListaTablas(nombres);
	
	     // Si hay una tabla seleccionada mostrar su estructura
	     String sel = vista.getTablaSeleccionada();
	     if (sel != null && tablas.containsKey(sel)) {
	         List<String[]> campos = tablas.get(sel).campos.stream()
	             .map(c -> new String[]{c.nombre, c.tipo, String.valueOf(c.esPK)})
	             .toList();
	         vista.mostrarEstructuraTabla(sel, campos);
	     }
	
	 }
	
	 // Genera el texto del árbol AVL para mostrarlo en AVLPanel
	 private String generarTextoArbol(String nombreTabla) {
	     Tabla tabla = tablas.get(nombreTabla);
	     if (tabla == null) return "Tabla no encontrada.";
	     List<Fila> filas = tabla.indice.inOrdenFilas();
	     if (filas.isEmpty()) return "(árbol vacío)";
	     StringBuilder sb = new StringBuilder();
	     sb.append("=== ÁRBOL AVL: ").append(nombreTabla).append(" ===\n\n");
	     sb.append("Recorrido InOrden (orden ascendente por PK):\n\n");
	     filas.forEach(f -> sb.append("  ").append(f).append("\n"));
	     return sb.toString();
	 }
	 
	// Método para cargar dataset (llamado desde la GUI)
	 public void cargarDataset(int opcion) {
	     CargadorDataset cargador = new CargadorDataset(this);
	     
	     if (opcion == 1) {
	         cargador.cargarDatasetPequeno();
	         if (vista != null) {
	             vista.mostrarResultadoEnRegistros("✅ Dataset PEQUEÑO cargado exitosamente (5 registros)");
	             // Actualizar vista
	             actualizarVistaTablas();
	             // Mostrar los datos cargados
	             String resultado = seleccionar("estudiantes", null, null);
	             vista.mostrarResultadoEnRegistros(resultado);
	         }
	     } else if (opcion == 2) {
	         cargador.cargarDatasetMediano();
	         if (vista != null) {
	             vista.mostrarResultadoEnRegistros("✅ Dataset MEDIANO cargado exitosamente (30 registros)");
	             actualizarVistaTablas();
	             String resultado = seleccionar("estudiantes", null, null);
	             vista.mostrarResultadoEnRegistros(resultado);
	         }
	     }
	 }
}
