package aed.tp2;

public class Trie {
    private class Promocion {
        double factor;
        double tope;
        boolean tieneTope;

        Promocion(double factor) {
            this.factor = factor;
            this.tieneTope = false;
        }

        Promocion(double factor, double tope) {
            this.factor = factor;
            this.tope = tope;
            this.tieneTope = true;
        }
    }

    private class NodoTrie {
        NodoTrie[] hijos;
        Promocion promocion;

        NodoTrie() {
            hijos = new NodoTrie[26];
            promocion = null;
        }
    }

    private NodoTrie raiz;

    public Trie() {
        raiz = new NodoTrie();
    }

    private int indice(char c) {
        return c - 'A';
    }

    public void insertar(String nombre, double factor) {
        insertarPromocion(nombre, new Promocion(factor));
    }

    public void insertarConTope(
            String nombre, double factor, double tope) {
        insertarPromocion(nombre, new Promocion(factor, tope));
    }

    private void insertarPromocion(String nombre, Promocion promocion) {
        NodoTrie actual = raiz;

        for (int i = 0; i < nombre.length(); i++) {
            int pos = indice(nombre.charAt(i));

            if (actual.hijos[pos] == null) {
                actual.hijos[pos] = new NodoTrie();
            }

            actual = actual.hijos[pos];
        }

        actual.promocion = promocion;
    }

    public boolean existe(String nombre) {
        return obtenerNodo(nombre) != null
                && obtenerNodo(nombre).promocion != null;
    }

    private NodoTrie obtenerNodo(String nombre) {
        NodoTrie actual = raiz;

        for (int i = 0; i < nombre.length(); i++) {
            int pos = indice(nombre.charAt(i));

            if (actual.hijos[pos] == null) {
                return null;
            }

            actual = actual.hijos[pos];
        }

        return actual;
    }

    public void eliminar(String nombre) {
        NodoTrie nodo = obtenerNodo(nombre);

        if (nodo != null) {
            nodo.promocion = null;
        }
    }

    public double obtenerFactor(String nombre) {
        NodoTrie nodo = obtenerNodo(nombre);

        if (nodo == null || nodo.promocion == null) {
            throw new RuntimeException("Promoción inexistente");
        }

        return nodo.promocion.factor;
    }

    public boolean tieneTope(String nombre) {
        NodoTrie nodo = obtenerNodo(nombre);

        if (nodo == null || nodo.promocion == null) {
            throw new RuntimeException("Promoción inexistente");
        }

        return nodo.promocion.tieneTope;
    }

    public double obtenerTope(String nombre) {
        NodoTrie nodo = obtenerNodo(nombre);

        if (nodo == null || nodo.promocion == null) {
            throw new RuntimeException("Promoción inexistente");
        }

        if (!nodo.promocion.tieneTope) {
            throw new RuntimeException("La promoción no tiene tope");
        }

        return nodo.promocion.tope;
    }
}
