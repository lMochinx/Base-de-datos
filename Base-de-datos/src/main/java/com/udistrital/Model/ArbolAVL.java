package com.udistrital.Model;

import java.util.ArrayList;
import java.util.List;

// Archivo: ArbolAVL.java
public class ArbolAVL {
    private NodoAVL raiz;


    private int altura(NodoAVL N) {
        if (N == null) return 0;
        return N.getAltura();
    }

    private int max(int a, int b) {
        return (a > b) ? a : b;
    }

    private int obtenerBalance(NodoAVL N) {
        if (N == null) return 0;
        return altura(N.getIzquierdo()) - altura(N.getDerecho());
    }

    // Rotaciones para balancear
    private NodoAVL rotacionDerecha(NodoAVL y) {
        NodoAVL x = y.getIzquierdo();
        NodoAVL T2 = x.getDerecho();

        x.setDerecho(y);
        y.setIzquierdo(T2);

        y.setAltura(max(altura(y.getIzquierdo()), altura(y.getDerecho())) + 1);
        x.setAltura(max(altura(x.getIzquierdo()), altura(x.getDerecho())) + 1);

        return x;
    }

    private NodoAVL rotacionIzquierda(NodoAVL x) {
        NodoAVL y = x.getDerecho();
        NodoAVL T2 = y.getIzquierdo();

        y.setIzquierdo(x);
        x.setDerecho(T2);

        x.setAltura(max(altura(x.getIzquierdo()), altura(x.getDerecho())) + 1);
        y.setAltura(max(altura(y.getIzquierdo()), altura(y.getDerecho())) + 1);

        return y;
    }

    // Insertar Valores
    public void insertar(int clave, Fila dato) {
        raiz = insertarRecursivo(raiz, clave, dato);
    }

    private NodoAVL insertarRecursivo(NodoAVL nodo, int clave, Fila dato) {
        if (nodo == null) {
            return new NodoAVL(clave, dato);
        }

        // Valores iguales van hacia la izquierda (Regla dada en clase)
        if (clave <= nodo.getClave()) {
        	nodo.setIzquierdo(insertarRecursivo(nodo.getIzquierdo(), clave, dato));
        } else {
        	nodo.setDerecho(insertarRecursivo(nodo.getDerecho(), clave, dato));
        }

        nodo.setAltura(1 + max(altura(nodo.getIzquierdo()), altura(nodo.getDerecho())));
        int balance = obtenerBalance(nodo);

        // Arbol desbalanceado y rotaciones
        if (balance > 1 && clave <= nodo.getIzquierdo().getClave()) {
            return rotacionDerecha(nodo); // Izquierda-Izquierda
        }
        if (balance < -1 && clave > nodo.getDerecho().getClave()) {
            return rotacionIzquierda(nodo); // Derecha-Derecha
        }
        if (balance > 1 && clave > nodo.getIzquierdo().getClave()) {
        	nodo.setIzquierdo(rotacionIzquierda(nodo.getIzquierdo()));// Izquierda-Derecha
            return rotacionDerecha(nodo);
        }
        if (balance < -1 && clave <= nodo.getDerecho().getClave()) {
        	nodo.setDerecho(rotacionDerecha(nodo.getDerecho())); // Derecha-Izquierda
            return rotacionIzquierda(nodo);
        }

        return nodo;
    }

    // Eliminación de nodos
    private NodoAVL nodoConValorMinimo(NodoAVL nodo) {
        NodoAVL actual = nodo;
        while (actual.getIzquierdo() != null) {
            actual = actual.getIzquierdo();
        }
        return actual;
    }

    public void eliminar(int clave) {
        raiz = eliminarRecursivo(raiz, clave);
    }

    private NodoAVL eliminarRecursivo(NodoAVL nodo, int clave) {
        if (nodo == null) return nodo;

        if (clave < nodo.getClave()) {
        	nodo.setIzquierdo(eliminarRecursivo(nodo.getIzquierdo(), clave));
        } else if (clave > nodo.getClave()) {
        	nodo.setDerecho(eliminarRecursivo(nodo.getDerecho(), clave));
        } else {
            if ((nodo.getIzquierdo() == null) || (nodo.getDerecho() == null)) {
                NodoAVL temp = (nodo.getIzquierdo() != null) ? nodo.getIzquierdo() : nodo.getDerecho();
                if (temp == null) {
                    nodo = null;
                } else {
                    nodo = temp;
                }
            } else {
                NodoAVL temp = nodoConValorMinimo(nodo.getDerecho());
                nodo.setClave(temp.getClave());
                nodo.setDato(temp.getDato());
                nodo.setDerecho(eliminarRecursivo(nodo.getDerecho(), temp.getClave()));
            }
        }

        if (nodo == null) return nodo;
        
        nodo.setAltura(max(altura(nodo.getIzquierdo()), altura(nodo.getDerecho())) + 1);
        int balance = obtenerBalance(nodo);

        // Balancea el arbol 
        if (balance > 1 && obtenerBalance(nodo.getIzquierdo()) >= 0) {
            return rotacionDerecha(nodo);
        }
        if (balance > 1 && obtenerBalance(nodo.getIzquierdo()) < 0) {
        	nodo.setIzquierdo(rotacionDerecha(nodo.getIzquierdo()));
            return rotacionDerecha(nodo);
        }
        if (balance < -1 && obtenerBalance(nodo.getDerecho()) <= 0) {
            return rotacionIzquierda(nodo);
        }
        if (balance < -1 && obtenerBalance(nodo.getDerecho()) > 0) {
        	nodo.setDerecho(rotacionDerecha(nodo.getDerecho()));
            return rotacionIzquierda(nodo);
        }

        return nodo;
    }

    // Busqueda
    public NodoAVL buscar(int clave) {
        return buscarRecursivo(raiz, clave);
    }

    private NodoAVL buscarRecursivo(NodoAVL nodo, int clave) {
        if (nodo == null || nodo.getClave() == clave) return nodo;
        if (nodo.getClave() > clave) return buscarRecursivo(nodo.getIzquierdo(), clave);
        return buscarRecursivo(nodo.getDerecho(), clave);
    }

    // Update Lógico
    public boolean updateLogico(int clave, Fila nuevoDato) {
        NodoAVL nodo = buscar(clave);
        if (nodo != null) {
        	nodo.setDato(nuevoDato);
            return true;
        }
        return false;
    }

    // Recorrido preorde, inorden y postorden
    public void imprimirPreOrden() { System.out.print("Pre: "); preOrden(raiz); System.out.println(); }
    private void preOrden(NodoAVL nodo) {
        if (nodo != null) {
            System.out.print("[" + nodo.getClave() + "] ");
            preOrden(nodo.getIzquierdo());
            preOrden(nodo.getDerecho());
        }
    }

    public void imprimirInOrden() { System.out.print("In: "); inOrden(raiz); System.out.println(); }
    private void inOrden(NodoAVL nodo) {
        if (nodo != null) {
            inOrden(nodo.getIzquierdo());
            System.out.print("[" + nodo.getClave() + "] ");
            inOrden(nodo.getDerecho());
        }
    }

    public void imprimirPostOrden() { System.out.print("Post: "); postOrden(raiz); System.out.println(); }
    private void postOrden(NodoAVL nodo) {
        if (nodo != null) {
            postOrden(nodo.getIzquierdo());
            postOrden(nodo.getDerecho());
            System.out.print("[" + nodo.getClave() + "] ");
        }
    }
    
    // model/ArbolAVL.java
    public List<Fila> inOrdenFilas() {
        List<Fila> lista = new ArrayList<>();
        inOrdenRecursivo(raiz, lista);
        return lista;
    }

    private void inOrdenRecursivo(NodoAVL nodo, List<Fila> lista) {
        if (nodo != null) {
            inOrdenRecursivo(nodo.izquierdo, lista);
            lista.add(nodo.getDato());
            inOrdenRecursivo(nodo.derecho, lista);
        }
    }
}