package com.udistrital.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.udistrital.Model.Campo;
import com.udistrital.Model.Fila;
import com.udistrital.Model.Tabla;
import com.udistrital.View.Consola;

public class Controller {
	
	private Map<String, Tabla> tablas = new HashMap<>();
	private Consola consola;
	
	public Controller() {
		consola = new Consola();
		iniciar();
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
                String[] tok = sin.substring(wi + 5).trim().split("=", 2);
                return eliminar(tabla, tok[0].trim(), tok[1].trim());
            }
            return eliminar(sin.trim(), null, null); // sin condición
        } catch (Exception e) {
            return "ERROR de sintaxis. Uso: DELETE FROM tabla [WHERE campo = valor]";
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
        tablas.put(nombre, new Tabla(nombre, campos));
        return "Tabla '" + nombre + "' creada.";
    }

    public String eliminarTabla(String nombre) {
        if (!tablas.containsKey(nombre))
            return "ERROR: la tabla '" + nombre + "' no existe.";
        tablas.remove(nombre);
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
        int claveValor = -1;

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
                if (!(valorTipado instanceof Integer))
                    return "ERROR: la clave primaria debe ser INT.";
                claveValor = (Integer) valorTipado;
            }
        }

        if (claveValor == -1)
            return "ERROR: debes incluir el campo PK en el INSERT.";

        // Verifica que la clave no exista
        if (tabla.indice.buscar(claveValor) != null)
            return "ERROR: ya existe una fila con clave " + claveValor + ".";

        tabla.indice.insertar(claveValor, new Fila(datos));
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

        List<Fila> filas = tabla.indice.inOrdenFilas();
        int count = 0;
        for (Fila fila : filas) {
            if (coincide(fila, campoCond, valorCond)) {
                Campo campo = tabla.campos.stream()
                    .filter(c -> c.nombre.equalsIgnoreCase(campoSet))
                    .findFirst().orElse(null);
                if (campo == null) return "ERROR: campo '" + campoSet + "' no existe.";
                Object valorTipado = parsearValor(nuevoValor, campo.tipo);
                if (valorTipado == null) return "ERROR: valor inválido para tipo " + campo.tipo;
                fila.datos.put(campo.nombre, valorTipado);
                count++;
            }
        }
        return count + " fila(s) actualizada(s).";
    }

    public String eliminar(String nombreTabla, String campoCond, String valorCond) {
        Tabla tabla = obtenerTabla(nombreTabla);
        if (tabla == null)
            return "ERROR: la tabla '" + nombreTabla + "' no existe.";

        // Si la condición es sobre la PK usamos el arbol directamente.
        Campo pk = tabla.campos.stream().filter(c -> c.esPK).findFirst().orElse(null);
        if (pk != null && pk.nombre.equalsIgnoreCase(campoCond)) {
            int clave = Integer.parseInt(valorCond.trim());
            if (tabla.indice.buscar(clave) == null)
                return "ERROR: no existe fila con " + campoCond + " = " + valorCond;
            tabla.indice.eliminar(clave);
            return "Fila eliminada.";
        }

        // Condicion sobre campo no-PK: buscar clave y elimina.
        List<Fila> filas = tabla.indice.inOrdenFilas();
        List<Integer> clavesAEliminar = new ArrayList<>();
        for (Fila fila : filas) {
            if (coincide(fila, campoCond, valorCond)) {
                // recupera la clave PK de esa fila
                if (pk != null) clavesAEliminar.add((Integer) fila.datos.get(pk.nombre));
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
}
