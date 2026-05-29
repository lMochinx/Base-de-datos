package com.udistrital.Model;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Persistencia {

    private static final String TABLES_DIR = "data" + File.separator + "tables";
    
     // Guarda UNA sola tabla en su archivo JSON.
     //Se llama después de cada operación que modifica datos,
     //asi solo reescribe el archivo de la tabla afectada.

    public static void guardarTabla(Tabla tabla) {
        try {
            Files.createDirectories(Path.of(TABLES_DIR));
            String ruta = TABLES_DIR + File.separator + tabla.getNombre() + ".json";

            StringBuilder sb = new StringBuilder();
            sb.append("{\n");

            // nombre de la tabla
            sb.append("  \"nombre\": ").append(jsonString(tabla.getNombre())).append(",\n");

            // esquema: lista de campos
            sb.append("  \"campos\": [\n");
            List<Campo> campos = tabla.getCampos();
            for (int i = 0; i < campos.size(); i++) {
                Campo c = campos.get(i);
                sb.append("    {")
                  .append(" \"nombre\": ").append(jsonString(c.getNombre())).append(",")
                  .append(" \"tipo\": ").append(jsonString(c.getTipo())).append(",")
                  .append(" \"esPK\": ").append(c.isEsPK())
                  .append(" }");
                if (i < campos.size() - 1) sb.append(",");
                sb.append("\n");
            }
            sb.append("  ],\n");

            // filas en recorrido inOrden del arbol AVL
            sb.append("  \"filas\": [\n");
            List<Fila> filas = tabla.getIndice().inOrdenFilas();
            for (int i = 0; i < filas.size(); i++) {
                sb.append("    {");
                List<Map.Entry<String, Object>> entradas =
                    new ArrayList<>(filas.get(i).datos.entrySet());
                for (int j = 0; j < entradas.size(); j++) {
                    String k = entradas.get(j).getKey();
                    Object v = entradas.get(j).getValue();
                    sb.append(" ").append(jsonString(k)).append(": ").append(jsonValor(v));
                    if (j < entradas.size() - 1) sb.append(",");
                }
                sb.append(" }");
                if (i < filas.size() - 1) sb.append(",");
                sb.append("\n");
            }
            sb.append("  ]\n}");

            Files.writeString(Path.of(ruta), sb.toString());

        } catch (IOException e) {
            System.out.println("ERROR al guardar '" + tabla.getNombre() + "': " + e.getMessage());
        }
    }

    
     //Elimina el archivo JSON de una tabla cuando se hace DROP TABLE.
    
    public static void eliminarArchivo(String nombreTabla) {
        try {
            Files.deleteIfExists(Path.of(TABLES_DIR + File.separator + nombreTabla + ".json"));
        } catch (IOException e) {
            System.out.println("ERROR al eliminar archivo de '" + nombreTabla + "'");
        }
    }

    /**
     * Lee todos los archivos .json de data/tables/ al arrancar el programa
     * y reconstruye el Map de tablas con sus arboles AVL ya cargados.
     */
    public static Map<String, Tabla> cargarTodas() {
        Map<String, Tabla> tablas = new LinkedHashMap<>();
        File dir = new File(TABLES_DIR);
        if (!dir.exists()) return tablas;

        File[] archivos = dir.listFiles((d, name) -> name.endsWith(".json"));
        if (archivos == null) return tablas;

        for (File archivo : archivos) {
            try {
                String contenido = Files.readString(archivo.toPath());
                Tabla tabla = parsearTabla(contenido);
                if (tabla != null) tablas.put(tabla.getNombre(), tabla);
            } catch (Exception e) {
                System.out.println("WARN: no se pudo cargar '" + archivo.getName() + "': " + e.getMessage());
            }
        }

        return tablas;
    }

    
     //Convierte el contenido de un archivo JSON a un objeto Tabla.
     
    private static Tabla parsearTabla(String json) {
        try {
            JsonReader jr = new JsonReader(json);
            Map<String, Object> raiz = jr.leerObjeto();

            // reconstruir nombre
            String nombre = (String) raiz.get("nombre");

            // reconstruir campos
            List<Campo> campos = new ArrayList<>();
            @SuppressWarnings("unchecked")
            List<Object> camposRaw = (List<Object>) raiz.get("campos");
            for (Object obj : camposRaw) {
                @SuppressWarnings("unchecked")
                Map<String, Object> c = (Map<String, Object>) obj;
                campos.add(new Campo(
                    (String)  c.get("nombre"),
                    (String)  c.get("tipo"),
                    Boolean.parseBoolean(String.valueOf(c.get("esPK")))
                ));
            }

            Tabla tabla = new Tabla(nombre, campos);
            Campo pk = campos.stream().filter(Campo::isEsPK).findFirst().orElse(null);

            // reconstruir filas e insertarlas en el arbol
            @SuppressWarnings("unchecked")
            List<Object> filasRaw = (List<Object>) raiz.get("filas");
            for (Object obj : filasRaw) {
                @SuppressWarnings("unchecked")
                Map<String, Object> filaRaw = (Map<String, Object>) obj;
                Map<String, Object> datos = new LinkedHashMap<>();

                // convertir cada valor al tipo correcto segun el esquema
                for (Campo c : campos) {
                    Object raw = filaRaw.get(c.getNombre());
                    if (raw != null) datos.put(c.getNombre(), convertir(raw, c.getTipo()));
                }

                if (pk != null) {
                    Object valorPK = datos.get(pk.getNombre());
                    tabla.getIndice().insertar(obtenerHash(valorPK), new Fila(datos));
                }
            }

            return tabla;

        } catch (Exception e) {
            System.out.println("WARN: error parseando tabla: " + e.getMessage());
            return null;
        }
    }

    
     //Convierte un valor leido del JSON al tipo Java correcto según el campo.
     
    private static Object convertir(Object raw, String tipo) {
        String s = String.valueOf(raw).trim();
        try {
            return switch (tipo.toUpperCase()) {
                case "INT"  -> Integer.parseInt(s);
                case "REAL" -> Double.parseDouble(s);
                case "BOOL" -> Boolean.parseBoolean(s);
                case "TEXT" -> s;
                default     -> s;
            };
        } catch (Exception e) {
            return raw;
        }
    }

    //Envuelve un String en comillas y escapa caracteres especiales.
    private static String jsonString(String s) {
        return "\"" + s.replace("\\", "\\\\")
                       .replace("\"", "\\\"")
                       .replace("\n", "\\n")
                       .replace("\r", "\\r") + "\"";
    }

    //Serializa un valor Java a su representación JSON correcta.
    private static String jsonValor(Object v) {
        if (v instanceof String)  return jsonString((String) v);
        if (v instanceof Boolean) return v.toString();           
        if (v instanceof Number)  return v.toString();           
        return jsonString(String.valueOf(v));
    }

    private static int obtenerHash(Object valor) {
        if (valor instanceof Integer) return (Integer) valor;
        return valor.hashCode();
    }
    
     //Parser JSON mínimo que lee caracter por caracter y maneja correctamente comillas escapadas \" y comas dentro de strings.
     
    private static class JsonReader {
        private final String src;
        private int pos;

        JsonReader(String src) {
            this.src = src;
            this.pos = 0;
        }

        //Se salta espacios, tabs y saltos de línea.
        private void skipWhitespace() {
            while (pos < src.length() && Character.isWhitespace(src.charAt(pos))) pos++;
        }

        //Lee el caracter actual y avanza.
        private char consume() {
            return src.charAt(pos++);
        }

        Object leerValor() {
            skipWhitespace();
            if (pos >= src.length()) return null;
            char c = src.charAt(pos);
            if (c == '{') return leerObjeto();
            if (c == '[') return leerArray();
            if (c == '"') return leerString();
            if (c == 't' || c == 'f') return leerBoolean();
            if (c == 'n') { pos += 4; return null; } // null
            return leerNumero();
        }

        //Lee un objeto JSON { "clave": valor, ... } y lo devuelve como Map.
        Map<String, Object> leerObjeto() {
            Map<String, Object> map = new LinkedHashMap<>();
            consume(); // '{'
            skipWhitespace();
            if (src.charAt(pos) == '}') { consume(); return map; }
            while (true) {
                skipWhitespace();
                String clave = leerString();
                skipWhitespace();
                consume(); // ':'
                Object valor = leerValor();
                map.put(clave, valor);
                skipWhitespace();
                char next = consume();
                if (next == '}') break;
            }
            return map;
        }

        //Lee un array JSON [ valor, valor, ... ] y lo devuelve como List.
        List<Object> leerArray() {
            List<Object> list = new ArrayList<>();
            consume(); // '['
            skipWhitespace();
            if (src.charAt(pos) == ']') { consume(); return list; }
            while (true) {
                list.add(leerValor());
                skipWhitespace();
                char next = consume();
                if (next == ']') break;
            }
            return list;
        }

        String leerString() {
            consume();
            StringBuilder sb = new StringBuilder();
            while (pos < src.length()) {
                char c = consume();
                if (c == '"') break;
                if (c == '\\') {
                    char esc = consume();
                    switch (esc) {
                        case '"'  -> sb.append('"');
                        case '\\' -> sb.append('\\');
                        case 'n'  -> sb.append('\n');
                        case 'r'  -> sb.append('\r');
                        case 't'  -> sb.append('\t');
                        default   -> sb.append(esc);
                    }
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }

        //Lee true o false.
        boolean leerBoolean() {
            if (src.startsWith("true", pos))  { pos += 4; return true;  }
            if (src.startsWith("false", pos)) { pos += 5; return false; }
            throw new RuntimeException("Boolean invalido en pos " + pos);
        }

         //Lee un número y lo devuelve como Integer si no tiene punto o como Double si tiene punto decimal.

        Object leerNumero() {
            int inicio = pos;
            if (pos < src.length() && src.charAt(pos) == '-') pos++;
            while (pos < src.length() && (Character.isDigit(src.charAt(pos)))) pos++;
            boolean esDecimal = pos < src.length() && src.charAt(pos) == '.';
            if (esDecimal) {
                pos++;
                while (pos < src.length() && Character.isDigit(src.charAt(pos))) pos++;
            }
            String num = src.substring(inicio, pos);
            if (esDecimal) return Double.parseDouble(num);
            return Integer.parseInt(num);
        }
    }
}