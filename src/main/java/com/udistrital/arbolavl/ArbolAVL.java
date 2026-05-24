package com.udistrital.arbolavl;

// Archivo: ArbolAVL.java
public class ArbolAVL {
    private NodoAVL raiz;


    private int altura(NodoAVL N) {
        if (N == null) return 0;
        return N.altura;
    }

    private int max(int a, int b) {
        return (a > b) ? a : b;
    }

    private int obtenerBalance(NodoAVL N) {
        if (N == null) return 0;
        return altura(N.izquierdo) - altura(N.derecho);
    }

    // Rotaciones para balancear
    private NodoAVL rotacionDerecha(NodoAVL y) {
        NodoAVL x = y.izquierdo;
        NodoAVL T2 = x.derecho;

        x.derecho = y;
        y.izquierdo = T2;

        y.altura = max(altura(y.izquierdo), altura(y.derecho)) + 1;
        x.altura = max(altura(x.izquierdo), altura(x.derecho)) + 1;

        return x;
    }

    private NodoAVL rotacionIzquierda(NodoAVL x) {
        NodoAVL y = x.derecho;
        NodoAVL T2 = y.izquierdo;

        y.izquierdo = x;
        x.derecho = T2;

        x.altura = max(altura(x.izquierdo), altura(x.derecho)) + 1;
        y.altura = max(altura(y.izquierdo), altura(y.derecho)) + 1;

        return y;
    }

    // Insertar Valores
    public void insertar(int clave, String dato) {
        raiz = insertarRecursivo(raiz, clave, dato);
    }

    private NodoAVL insertarRecursivo(NodoAVL nodo, int clave, String dato) {
        if (nodo == null) {
            return new NodoAVL(clave, dato);
        }

        // Valores iguales van hacia la izquierda (Regla dada en clase)
        if (clave <= nodo.clave) {
            nodo.izquierdo = insertarRecursivo(nodo.izquierdo, clave, dato);
        } else {
            nodo.derecho = insertarRecursivo(nodo.derecho, clave, dato);
        }

        nodo.altura = 1 + max(altura(nodo.izquierdo), altura(nodo.derecho));
        int balance = obtenerBalance(nodo);

        // Arbol desbalanceado y rotaciones
        if (balance > 1 && clave <= nodo.izquierdo.clave) {
            return rotacionDerecha(nodo); // Izquierda-Izquierda
        }
        if (balance < -1 && clave > nodo.derecho.clave) {
            return rotacionIzquierda(nodo); // Derecha-Derecha
        }
        if (balance > 1 && clave > nodo.izquierdo.clave) {
            nodo.izquierdo = rotacionIzquierda(nodo.izquierdo); // Izquierda-Derecha
            return rotacionDerecha(nodo);
        }
        if (balance < -1 && clave <= nodo.derecho.clave) {
            nodo.derecho = rotacionDerecha(nodo.derecho); // Derecha-Izquierda
            return rotacionIzquierda(nodo);
        }

        return nodo;
    }

    // Eliminación de nodos
    private NodoAVL nodoConValorMinimo(NodoAVL nodo) {
        NodoAVL actual = nodo;
        while (actual.izquierdo != null) {
            actual = actual.izquierdo;
        }
        return actual;
    }

    public void eliminar(int clave) {
        raiz = eliminarRecursivo(raiz, clave);
    }

    private NodoAVL eliminarRecursivo(NodoAVL nodo, int clave) {
        if (nodo == null) return nodo;

        if (clave < nodo.clave) {
            nodo.izquierdo = eliminarRecursivo(nodo.izquierdo, clave);
        } else if (clave > nodo.clave) {
            nodo.derecho = eliminarRecursivo(nodo.derecho, clave);
        } else {
            if ((nodo.izquierdo == null) || (nodo.derecho == null)) {
                NodoAVL temp = (nodo.izquierdo != null) ? nodo.izquierdo : nodo.derecho;
                if (temp == null) {
                    nodo = null;
                } else {
                    nodo = temp;
                }
            } else {
                NodoAVL temp = nodoConValorMinimo(nodo.derecho);
                nodo.clave = temp.clave;
                nodo.dato = temp.dato;
                nodo.derecho = eliminarRecursivo(nodo.derecho, temp.clave);
            }
        }

        if (nodo == null) return nodo;

        nodo.altura = max(altura(nodo.izquierdo), altura(nodo.derecho)) + 1;
        int balance = obtenerBalance(nodo);

        // Balancea el arbol 
        if (balance > 1 && obtenerBalance(nodo.izquierdo) >= 0) {
            return rotacionDerecha(nodo);
        }
        if (balance > 1 && obtenerBalance(nodo.izquierdo) < 0) {
            nodo.izquierdo = rotacionDerecha(nodo.izquierdo);
            return rotacionDerecha(nodo);
        }
        if (balance < -1 && obtenerBalance(nodo.derecho) <= 0) {
            return rotacionIzquierda(nodo);
        }
        if (balance < -1 && obtenerBalance(nodo.derecho) > 0) {
            nodo.derecho = rotacionDerecha(nodo.derecho);
            return rotacionIzquierda(nodo);
        }

        return nodo;
    }

    // Busqueda
    public NodoAVL buscar(int clave) {
        return buscarRecursivo(raiz, clave);
    }

    private NodoAVL buscarRecursivo(NodoAVL nodo, int clave) {
        if (nodo == null || nodo.clave == clave) return nodo;
        if (nodo.clave > clave) return buscarRecursivo(nodo.izquierdo, clave);
        return buscarRecursivo(nodo.derecho, clave);
    }

    // Update Lógico
    public boolean updateLogico(int clave, String nuevoDato) {
        NodoAVL nodo = buscar(clave);
        if (nodo != null) {
            nodo.dato = nuevoDato;
            return true;
        }
        return false;
    }

    // Recorrido preorde, inorden y postorden
    public void imprimirPreOrden() { System.out.print("Pre: "); preOrden(raiz); System.out.println(); }
    private void preOrden(NodoAVL nodo) {
        if (nodo != null) {
            System.out.print("[" + nodo.clave + "] ");
            preOrden(nodo.izquierdo);
            preOrden(nodo.derecho);
        }
    }

    public void imprimirInOrden() { System.out.print("In: "); inOrden(raiz); System.out.println(); }
    private void inOrden(NodoAVL nodo) {
        if (nodo != null) {
            inOrden(nodo.izquierdo);
            System.out.print("[" + nodo.clave + "] ");
            inOrden(nodo.derecho);
        }
    }

    public void imprimirPostOrden() { System.out.print("Post: "); postOrden(raiz); System.out.println(); }
    private void postOrden(NodoAVL nodo) {
        if (nodo != null) {
            postOrden(nodo.izquierdo);
            postOrden(nodo.derecho);
            System.out.print("[" + nodo.clave + "] ");
        }
    }
}