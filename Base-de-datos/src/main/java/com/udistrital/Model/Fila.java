package com.udistrital.Model;

import java.util.Map;
import java.util.LinkedHashMap;

public class Fila {
 public Map<String, Object> datos;

 public Fila(Map<String, Object> datos) {
     this.datos = new LinkedHashMap<>(datos); // los agrega por orden de insercion.
 }

 @Override
 public String toString() {
     StringBuilder sb = new StringBuilder("{ ");
     datos.forEach((k, v) -> sb.append(k).append(": ").append(v).append("  "));
     return sb.append("}").toString();
 }
}
