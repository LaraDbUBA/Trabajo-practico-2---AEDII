

import java.util.*;

// Todos los tipos de datos "Comparables" tienen el método compareTo()
// elem1.compareTo(elem2) devuelve un entero. Si es mayor a 0, entonces elem1 > elem2
public class ABB<T extends Comparable<T>> {
    private Nodo raiz;
    private int cantidad;

    private class Nodo {
        T elem;
        Nodo izq;
        Nodo der;
        Nodo padre;

        Nodo(T elem){
            this.elem = elem;
            this.izq = null;
            this.der = null;
            this.padre = null;
        }
    }

    public ABB() {
        this.raiz = null;
        this.cantidad = 0;
    }

    public int cardinal() {
        return this.cantidad;
    }

    public T minimo(){
        if (raiz == null){
            throw new RuntimeException();
        }

        Nodo actual = raiz;

        while(actual.izq != null){
            actual = actual.izq ;
        }

        return actual.elem;
    }

    public T maximo(){
        if (raiz == null){
            throw new RuntimeException();
        }

        Nodo actual = raiz;

        while(actual.der != null){
            actual = actual.der;
        }

        return actual.elem;
    }

    
    public void insertar(T elem) {
        if (raiz == null) {
            raiz = new Nodo(elem);
            cantidad++;
            return;
        }

        Nodo actual = raiz;
        Nodo padre = null;

        while (actual != null) {
            padre = actual;
            int comp = elem.compareTo(actual.elem);

            if (comp == 0) {
                return; // El elemento ya existe
            } else if (comp < 0) {
                actual = actual.izq;
            } else {
                actual = actual.der;
            }
        }

        Nodo nuevo = new Nodo(elem);
        nuevo.padre = padre;

        if (elem.compareTo(padre.elem) < 0) {
            padre.izq = nuevo;
        } else {
            padre.der = nuevo;
        }

        cantidad++;
    }


    public boolean pertenece(T elem){
        Nodo actual = raiz;
        
        while (actual != null){
            int comp = elem.compareTo(actual.elem);

            if (comp == 0){
                return true;
            } else if (comp <0){
                actual = actual.izq;
            } else {
                actual = actual.der;
            }
        }
        return false;
    }

        public void eliminar(T elem) {
            Nodo actual = raiz;

            while (actual != null && !actual.elem.equals(elem)) {
                int comp = elem.compareTo(actual.elem);

                if (comp < 0) {
                    actual = actual.izq;
                } else {
                    actual = actual.der;
                }
            }

            if (actual == null) {
                return;
            }

            if (actual.izq == null) {
                reemplazar(actual, actual.der);
            }
          
            else if (actual.der == null) {
                reemplazar(actual, actual.izq);
            }
            
            else {
                Nodo sucesor = actual.der;

                while (sucesor.izq != null) {
                    sucesor = sucesor.izq;
                }

                actual.elem = sucesor.elem;
                reemplazar(sucesor, sucesor.der);
            }

            cantidad--;
        }
        
    public T obtener(T elem) {
        Nodo actual = raiz;

        while (actual != null) {
            int comp = elem.compareTo(actual.elem);

            if (comp == 0) {
                return actual.elem;
            } else if (comp < 0) {
                actual = actual.izq;
            } else {
                actual = actual.der;
            }
        }

        return null;
    }

    private void reemplazar(Nodo viejo, Nodo nuevo){
        if (viejo.padre == null){
            raiz = nuevo;
        } else if(viejo == viejo.padre.izq){
            viejo.padre.izq = nuevo;
        } else{
            viejo.padre.der = nuevo;
        }

        if (nuevo!= null){
            nuevo.padre = viejo.padre;
        }
    }

    @Override
    public String toString(){
        String resultado = "{";
        ABB_Iterador it = iterador();
        
        while(it.haySiguiente()){
            resultado += it.siguiente();

            if (it.haySiguiente()){
                resultado += ",";
            }
        }

        resultado += "}";

        return resultado;
    }

    public class ABB_Iterador {
        private Nodo _actual;

        public boolean haySiguiente() {            
            return _actual != null;    
        }

        private Nodo sucesor(Nodo nodo) {
            if (nodo.der != null) {
                Nodo actual = nodo.der;

                while (actual.izq != null) {
                    actual = actual.izq;
                }

                return actual;
            }

            Nodo actual = nodo;
            Nodo padre = actual.padre;

            while (padre != null && actual == padre.der) {
                actual = padre;
                padre = padre.padre;
            }

            return padre;
        }
    
        public T siguiente() {
            if (_actual == null){
                throw new RuntimeException();
            }   

            T resultado = _actual.elem;
            _actual = sucesor(_actual);

            return resultado;
        }
    }

    public ABB_Iterador iterador() {
        ABB_Iterador it = new ABB_Iterador();
        it._actual = raiz;

        if (it._actual != null) {
            while (it._actual.izq != null) {
                it._actual = it._actual.izq;
            }
        }

        return it;
    }

}
