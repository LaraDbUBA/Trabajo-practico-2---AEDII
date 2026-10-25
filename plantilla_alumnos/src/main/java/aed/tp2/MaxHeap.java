/*
 
package aed.tp2;

import java.util.ArrayList;

/**
 * Cuentas Importantes:
 * Buscar el indice Padre es: n -1 / 2
 * Hijo Izquierdo de I es: 2*i + 1
 * Hijo Derecho de I es: 2* i + 2
 * 
 * Desiciones Importantes:
 * Se va a implementar o no Handle; Si vale la pena o no por el costo de complejidad
 * <p>
 * Permitiria acceder más rapido ya que podríamos guardar el indice, pero tiene que estar bien implementado tanto cuando sube como cuando baja
 


public class MaxHeap <T extends Comparable<T>>{
    /** Comentario de Santi
     * Implementación: Maxheap es un solo array pero pensalo commo un arbol por lo menos en el dibujo
     * No hay nodos ni punteros.
     * 
     * Índice:   0  1  2  3  4  5  6
     *  Array:  [9, 6, 8, 4, 3, 2, 5]
     * 
     * Y lo imaginás así:


                9 (índice 0)
              /    \
         6 (1)      8 (2)
         /  \       /  \
      4 (3) 3 (4) 2 (5) 5 (6)

    * El orden es en base al indice, como se colocoan y se encuentran se hacenn con la sección arriba de <Cuentas Importantes>
    * <p>
    * Nodos del mismo nivel no importa su orden.
    * 
    * Entendiendo esto la implementación sería mas o menos  la siguiente:
    * Guardaríamos los objetos en cada uno. Pero la forma en que estarían ordenados serían según la cantidad de millasDisponibles.
    * <p>
    * Despues de ser necesario al devolver el objeto se puede acceder a sus datos, tanto millasDisponibles, Id, millasTotales.. etc.
    * <p>
    * 
    *   
    */

    /* Comentario de Lara
    Mi idea es la siguiente, hacemos una interfaz anidada en este archivo
    seria asi:

    public interface Operaciones<T> {
        int comparar(T a, T b);
        int obtenerPosicion(T elem);
        void actualizarPosicion(T elem, int posicion);
    }

    (mira el archivo de AirMiles para ver como la usaria)
    Entonces el constructor de MaxHeap quedaria:
    private ArrayList<T> datos; <-- Habria que chequear si se puede importar ArrayList, supongo que si
    private Operaciones<T> operaciones;

    public MaxHeap(Operaciones<T> operaciones) {
        this.datos = new ArrayList<>();
        this.operaciones = operaciones;
    }

    VOY A DEJAR AL FINAL DEL ARCHIVO COMO LO HARIA, LO DEJO COMENTADO
    es para no olvidarme

     


    private int _longitud;
    

    public MaxHeap() {
        this._longitud = 0;
        
    }

    public T sacarMaximo(){
        /**
         * Sacamos el elemento al tope del Arbol
         * Ponemos en su lugar el último
         * Reordenamos todo según sea necesario
        
        throw new UnsupportedOperationException("Sin implementar");
    }

    public int cardinal() {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void insertar() {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public T obtener() {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void eliminar(){
        throw new UnsupportedOperationException("Sin implementar");
    }

    public T mostrarMaximo(){
        /**
         * Muestra el elemento al tope del Arbol o el primer elemento del Array.
         * <p>
         * Costo O(1)
         
        throw new UnsupportedOperationException("Sin implementar");
    }

    public ArrayList<T> ranking(int n){
        /**
         * Vamos a usar sacarMaximo la cantidad de veces que sean n
         * Por cada vez que se haga sacarMaximo se va a reordenar de nuevo todo, poniendo un nuevo maximo
         * Se hace esto hasta conseguir todo
         * <p>
         * Dependiendo de si necesitamos esos que retirarmos o no nuevamente, se va a tener que pensar una copia o no
         
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void sortMaxHeap(){
        /**
         * Encargada de reordenadar el array tanto si se elimina como si se agrega un elemento
         
        throw new UnsupportedOperationException("Sin implementar");
    }






    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }


    /**
     * Override: Sobre las comparaciones para que permita ordenarlo como nosotros quereamos
     * CompareTo va a ser sobre las millasDisponibles
     * <p>
     * Vamos a guardarlos como objetos pero van a estar ordenados según las millas
     * <p>
     * 
     
    


}
*/
// Borrador:

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


