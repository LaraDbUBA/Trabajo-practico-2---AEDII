/*
 * Cuentas Importantes:
 * Buscar el indice Padre es: n -1 / 2
 * Hijo Izquierdo de I es: 2*i + 1
 * Hijo Derecho de I es: 2* i + 2
 * 
*/

package aed.tp2;

import java.util.ArrayList;

public class MaxHeap<T> {

    public interface Operaciones<T> {
        int comparar(T a, T b);
        int obtenerPosicion(T elem);
        void actualizarPosicion(T elem, int posicion);
    }

    private ArrayList<T> datos;
    private Operaciones<T> operaciones;

    public MaxHeap(Operaciones<T> operaciones) {
        this.datos = new ArrayList<>();
        this.operaciones = operaciones;
    }

    public boolean estaVacio() {
        return datos.isEmpty();
    }

    public int tamanio() {
        return datos.size();
    }

    public T verMaximo() {
        if (estaVacio()) {
            throw new IllegalStateException("El heap esta vacio");
        }

        return datos.get(0);
    }

    public void insertar(T elem) {
        if (operaciones.obtenerPosicion(elem) != -1) {
            throw new IllegalArgumentException("El elemento ya pertenece al heap");
        }

        datos.add(elem);

        int pos = datos.size() - 1;
        operaciones.actualizarPosicion(elem, pos);

        subir(pos);
    }

    public T extraerMaximo() {
        if (estaVacio()) {
            throw new IllegalStateException("El heap esta vacio");
        }

        T maximo = datos.get(0);
        T ultimo = datos.remove(datos.size() - 1);

        if (!datos.isEmpty()) {
            datos.set(0, ultimo);
            operaciones.actualizarPosicion(ultimo, 0);
            bajar(0);
        }

        operaciones.actualizarPosicion(maximo, -1);

        return maximo;
    }

    public void actualizar(T elem) {
        int pos = operaciones.obtenerPosicion(elem);

        if (pos < 0 || pos >= datos.size() || datos.get(pos) != elem) {
            throw new IllegalArgumentException("El elemento no pertenece al heap");
        }

        subir(pos);
        bajar(operaciones.obtenerPosicion(elem));
    }

    private void subir(int pos) {
        while (pos > 0) {
            int padre = (pos - 1) / 2;

            if (operaciones.comparar(datos.get(pos), datos.get(padre)) <= 0) {
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

            if (izq < tamanio && operaciones.comparar(
                    datos.get(izq), datos.get(mayor)) > 0) {
                mayor = izq;
            }

            if (der < tamanio && operaciones.comparar(
                    datos.get(der), datos.get(mayor)) > 0) {
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
        T aux = datos.get(i);
        datos.set(i, datos.get(j));
        datos.set(j, aux);

        operaciones.actualizarPosicion(datos.get(i), i);
        operaciones.actualizarPosicion(datos.get(j), j);
    }
}


