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
 */


public class MaxHeap <T extends Comparable<T>>{
    /**
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
    private int _longitud;

    public MaxHeap() {
        this._longitud = 0;
        
    }

    public T sacarMaximo(){
        /**
         * Sacamos el elemento al tope del Arbol
         * Ponemos en su lugar el último
         * Reordenamos todo según sea necesario
         */
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
         */
        throw new UnsupportedOperationException("Sin implementar");
    }

    public ArrayList<T> ranking(int n){
        /**
         * Vamos a usar sacarMaximo la cantidad de veces que sean n
         * Por cada vez que se haga sacarMaximo se va a reordenar de nuevo todo, poniendo un nuevo maximo
         * Se hace esto hasta conseguir todo
         * <p>
         * Dependiendo de si necesitamos esos que retirarmos o no nuevamente, se va a tener que pensar una copia o no
         */
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void sortMaxHeap(){
        /**
         * Encargada de reordenadar el array tanto si se elimina como si se agrega un elemento
         */
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
     */
    


}
