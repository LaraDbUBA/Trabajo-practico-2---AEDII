package aed.tp2;

interface Diccionario<K,V> {
    /**
     * Define una nueva clave y su valor o reescribe si ya existe.
     * 
    */
   public void definir(K k, V v);
   
   /**
     * Devuelve si la clave pertenece al diccionario o no.
   */
  public boolean esta(K k);
  
  /**
   * Devuelve el valor asociado a la clave pasada por parámetro.
   * Requiere que la clave pertenezca al diccionario. 
   * 
  */
   public V obtener(K k);
   
   /**
     * Elimina la clave pasada por parámetro y su valor.
     * Requiere que la clave pertenezca al diccionario. 
     * 
     */
    public void borrar(K k);

}