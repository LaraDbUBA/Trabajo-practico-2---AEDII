package aed.tp2;

//Diccionario sobre ABB asocia claves a valores, ordenado por clave. 

public class ABB<K extends Comparable<K>,V> {
    private Nodo raiz;
    private int cantidad;

    private class Nodo {
        Nodo izq;
        Nodo der;
        K clave;
        V valor;

        Nodo(K k, V v){
            this.clave = k;
            this.valor = v;
            this.izq = null;
            this.der = null;
        }
    }

    public ABB() {
        this.raiz = null;
        this.cantidad = 0;
    }

    public void definir(K k, V v) {
        if (raiz == null) {
            raiz = new Nodo(k,v);
            cantidad++;
            return;
        }

        Nodo actual = raiz;
        Nodo padre = null;

        while (actual != null) {
            padre = actual;
            int comp = k.compareTo(actual.clave);

            if (comp == 0) {
                actual.valor = v; // La clave ya existe, lo pisa el nuevo valor. 
                return;
            } else if (comp < 0) {
                actual = actual.izq;
            } else {
                actual = actual.der;
            }
        }

        Nodo nuevo = new Nodo(k,v);

        if (k.compareTo(padre.clave) < 0) {
            padre.izq = nuevo;
        } else {
            padre.der = nuevo;
        }

        cantidad++;
    // Complejidad: // O(logU) promedio y O(U) en peor caso (usuarios insertados en orden) 
    }

    public V obtener(K k) {
        Nodo actual = raiz;

        while (actual != null) {
            int comp = k.compareTo(actual.clave);

            if (comp == 0) {
                return actual.valor;
            } else if (comp < 0) {
                actual = actual.izq;
            } else {
                actual = actual.der;
            }
        }
        return null;
    // Complejidad: // O(logU) promedio y O(U) en peor caso (usuarios insertados en orden) 
    }
}
