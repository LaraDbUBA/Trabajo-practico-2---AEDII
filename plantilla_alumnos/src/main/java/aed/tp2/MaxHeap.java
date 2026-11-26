/*
 * Cuentas Importantes:
 * Buscar el indice Padre es: (i - 1)/ 2
 * Hijo Izquierdo de I es: 2*i + 1
 * Hijo Derecho de I es: 2* i + 2
 * 
*/

package aed.tp2;

import java.util.ArrayList;

public class MaxHeap<T extends Comparable<T>> {
    private ArrayList<Nodo> datos;

    private class Nodo {
        T dato;
        int posicion;

        Nodo(T v, int i) {
            dato = v;
            posicion = i;
        }
    }

    private class HandleHeap implements Handle<T> {
        private Nodo nodo;

        private HandleHeap(Nodo n) {
            this.nodo = n;
        }

        public T valor() {
            // requiere que el handle tenga un nodo que pertenezca al Heap

            return nodo.dato;
        }

        public void eliminar() {
            // requiere que el handle tenga un nodo que pertenezca al Heap
            int pos = this.nodo.posicion;
            Nodo ultimo = datos.remove(datos.size() - 1);
            if (pos != datos.size()) {
                datos.set(pos, ultimo);
                ultimo.posicion = pos;
                subir(pos);
                bajar(ultimo.posicion);
            }
            this.nodo = null;
        }

        public void modificar(T v) {
            // requiere que el handle tenga un nodo que pertenezca al Heap
            nodo.dato = v;
            subir(this.nodo.posicion);
            bajar(this.nodo.posicion);
        }

        public boolean equals(Handle<T> otroHandle) {
            if (otroHandle == null || nodo == null) {
                return false;
            }
            return nodo.dato.equals(otroHandle.valor());
        }

        public String toString() {
            return valor().toString();
        }
    }

    public MaxHeap() {
        this.datos = new ArrayList<>();
    }

    public int tamanio() {
        return datos.size();
    }

    public Handle<T> insertar(T elem) {
        Nodo n = new Nodo(elem, datos.size());
        datos.add(n);
        int pos = datos.size() - 1;
        subir(pos);
        return new HandleHeap(n);
    }

    public ArrayList<T> primeros(int n) {
        // requiere: 0 ≤ n ≤ tamanio()

        ArrayList<Nodo> nodos = new ArrayList<>();
        ArrayList<T> res = new ArrayList<>();

        while (n != 0) { // N veces
            Nodo nodo = this.sacarNodoMaximo(); // O(log U)
            nodos.add(nodo);
            res.add(nodo.dato);
            n--;
        }

        for (Nodo nodo : nodos) { // N veces
            nodo.posicion = datos.size();
            datos.add(nodo);
            subir(datos.size() - 1); // O(log U)
        }
        return res;
        // Complejidad: O(Nlog U) + O(Nlog U) = O(Nlog U)
    }

    private Nodo sacarNodoMaximo() {
        // requiere: datos.tamanio() > 0

        Nodo maximo = datos.get(0);
        Nodo ultimo = datos.remove(datos.size() - 1);

        if (!datos.isEmpty()) {
            datos.set(0, ultimo);
            ultimo.posicion = 0;
            bajar(0);
        }
        maximo.posicion = -1;
        return maximo;
    }

    private void subir(int pos) {
        while (pos > 0) {
            int padre = (pos - 1) / 2;

            if (datos.get(pos).dato.compareTo(datos.get(padre).dato) <= 0) {
                break;
            }
            intercambiar(pos, padre);
            pos = padre;
        }
    }

    private void bajar(int pos) {
        int tamanio = datos.size();

        while (true) {
            int izq = 2 * pos + 1;
            int der = 2 * pos + 2;
            int mayor = pos;

            if (izq < tamanio && datos.get(izq).dato.compareTo(datos.get(mayor).dato) > 0) {
                mayor = izq;
            }

            if (der < tamanio && datos.get(der).dato.compareTo(datos.get(mayor).dato) > 0) {
                mayor = der;
            }

            if (mayor == pos) {
                break;
            }
            intercambiar(pos, mayor);
            pos = mayor;
        }
    }

    private void intercambiar(int i, int j) {
        Nodo aux = datos.get(i);
        datos.set(i, datos.get(j));
        datos.set(j, aux);

        datos.get(i).posicion = i;
        datos.get(j).posicion = j;
    }
}
