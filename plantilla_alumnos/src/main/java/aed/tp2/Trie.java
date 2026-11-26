package aed.tp2;

import java.util.ArrayList;

public class Trie<T> implements Diccionario<String, T> {
    private static final int ALFABETO = 256;

    private NodoTrie raiz;

    private class NodoTrie {
        ArrayList<NodoTrie> hijos;
        T valor;

        NodoTrie() {
            hijos = new ArrayList<>();
            for (int i = 0; i < ALFABETO; i++) {
                hijos.add(null);
            }
        }
    }

    public Trie() {
        raiz = new NodoTrie();
    }

    private NodoTrie buscarNodo(String clave) {
        NodoTrie res = this.raiz;
        for (int i = 0; i < clave.length() && res != null; i++) {
            char c = clave.charAt(i);
            res = res.hijos.get(c);
        }
        return res;
    }

    public boolean esta(String clave) {
        NodoTrie nodo = this.buscarNodo(clave);
        return !(nodo == null || nodo.valor == null);
    }

    public T obtener(String clave) {
        //requiere: this.esta(clave)
        NodoTrie nodo = this.buscarNodo(clave);
        return nodo.valor;
    }

    public void definir(String clave, T valor) {
        // requiere: valor != null; 
        NodoTrie actual = raiz;
        for(int i = 0;i<clave.length();i++) {
            char c = clave.charAt(i);
            NodoTrie hijo = actual.hijos.get(c);
            if(hijo == null) {
                NodoTrie nuevo = new NodoTrie();
                actual.hijos.set(c, nuevo);
                actual = nuevo;
            } else {
                actual = hijo;
            }
        }
        actual.valor = valor;
    }
    
    public void borrar(String clave) {
        //requiere: this.esta(clave)
        NodoTrie actual = this.raiz;
        ArrayList<NodoTrie> camino = new ArrayList<>();
        camino.add(actual);

        for (int i = 0; i < clave.length() && actual != null; i++) {
            char c = clave.charAt(i);
            actual = actual.hijos.get(c);
            camino.add(actual);
        }

        actual.valor = null;

        for(int j = clave.length();j>0;j--) {
            NodoTrie nodo = camino.get(j);
            if(!tieneHijos(nodo) && nodo.valor == null) {
                NodoTrie padre = camino.get(j-1);
                padre.hijos.set(clave.charAt(j-1),null);
            }
        }
    }

    private boolean tieneHijos(NodoTrie nodo) {
        boolean res = false;
        for(int i = 0; i < ALFABETO && !res;i++) {
            if(nodo.hijos.get(i) != null) {
                res = true;
            }
        }
        return res;
    }
}