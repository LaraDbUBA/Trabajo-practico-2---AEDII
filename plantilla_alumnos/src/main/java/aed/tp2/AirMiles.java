package aed.tp2;
import java.util.ArrayList;
public class AirMiles implements SistemaAirMiles {
    private ABB<Usuario> usuarios;
    private Usuario maxAcumulador;
    private Usuario maxCanjeador;
    private MaxHeap<Usuario> heapMillas;
    private Trie promociones;
    // TablaHashRelaciones relaciones;
    // private ListaEnlazada<Relacion> paresExclusivos; <- Idea
    
    private class Usuario implements Comparable<Usuario>{
        int id;
        int millasDisponibles;
        int millasTotales;
        int millasCanjeadas;
        int millasTransferidas;
        int posicionHeap;
      /* IDEA
        int cantidadContrapartes;
        Usuario parejaExclusiva ;
        Handle<Relacion> handleParejaExclusiva;
      */

        Usuario(int id){
            this.id = id;
            this.millasDisponibles = 0;
            this.millasTotales = 0;
            this.millasCanjeadas = 0;
            this.millasTransferidas = 0;
            this.posicionHeap = -1;
            /* IDEA
                this.cantidadContrapartes = 0;
                this.parejaExclusiva = null;
                this.handleParejaExclusiva = null;
            */

        }
        
        @Override
        public int compareTo(Usuario otro) {
        return Integer.compare(this.id, otro.id); //O(1)
    }
    }

    
    public AirMiles() {
        usuarios = new ABB<>(); //O(1)
        promociones = new Trie(); // O(1)
        promociones.insertar("NoPromo", 1.0);
       // paresExclusivos = new ListaEnlazada<>();
       // relaciones = new TablaHashRelaciones();
        heapMillas = new MaxHeap<Usuario>(new MaxHeap.Operaciones<Usuario>() {
            @Override
            public int comparar(Usuario a, Usuario b) {
                return Integer.compare(
                    a.millasDisponibles,
                    b.millasDisponibles
                );
            }

            @Override
            public int obtenerPosicion(Usuario u) {
                return u.posicionHeap;
            }

            @Override
            public void actualizarPosicion(Usuario u, int posicion) {
                u.posicionHeap = posicion;
            }
            }
        );

       
    }
    public AirMiles crearSistema(){
        return new AirMiles(); //O(1)
    }

    public void registrarUsuario(int id) {
        Usuario u = new Usuario(id); // O(1)

        usuarios.insertar(u); // O(logU)
        if (maxAcumulador == null) {
            maxAcumulador = u;
        }

        if (maxCanjeador == null) {
            maxCanjeador = u;
        }
        heapMillas.insertar(u); // O(logU)
        // Complejidad final: O(logU)
    }


    public void registrarUsuarioConReferidor(int id, int ref) {
        Usuario u = new Usuario(id); // O(1)

        // Antes de agregar aumentamos la cantidad de millas
        u.millasDisponibles +=500;
        u.millasTotales += 500;

        usuarios.insertar(u); // O(logU)
        heapMillas.insertar(u); // O(logU)

        /**
         * Tenemos que sumarle 500 millas al referidor
         * Despues en base a la modificacion volvemos a verificar si es necesario cambiar las cosas
         */

        Usuario referidor = buscarUsuario(ref); // O(logU)

        referidor.millasDisponibles +=500;
        referidor.millasTotales += 500; // se le agregan las totales tambien?

        // si de verdad hay que sumar las millas Totales hay que chequear el maximo acumulador
        heapMillas.actualizar(referidor); // O(logU)
        

        //Falta agregar el caso Null?
        
    }


    private double consultarFactorCategoria(String categoria) {
        double res = 0.0; //O(1)

        if (categoria.equals("Bronce")) {
            res = 1.0;
        } else if (categoria.equals("Plata")) {
            res = 1.5;
        } else if (categoria.equals("Oro")) {
            res = 2.0;
        }

        return res; // O(1)
    }
    private String categoriaPorMillas(int millas) {
        String res;

        if (millas < 10000) {
            res = "Bronce";
        } else if (millas < 50000) {
            res = "Plata";
        } else if (millas < 1000000) {
            res = "Oro";
        } else {
            res = "Platino";
        }

        return res; // O(1)
    }

    
    public void acumularMillas(int id, double distancia, String nombrePromo) {
        Usuario u = buscarUsuario(id); // O(log U)

        String categoria = categoriaPorMillas(u.millasTotales); // O(1)
        double factorCategoria = consultarFactorCategoria(categoria); // O(1)

        double millasBase = distancia * factorCategoria; // O(1)

        // Busca la promoción una sola vez y controla su tope.
        double millas = promociones.calcularMillas(nombrePromo, millasBase); // O(|L|)

        u.millasTotales += millas; // O(1)
        u.millasDisponibles += millas; // O(1)

        heapMillas.actualizar(u); // O(log U)

        if (maxAcumulador == null || u.millasTotales > maxAcumulador.millasTotales) {
            maxAcumulador = u; // O(1)
        }

        // Complejidad: O(log U + |L|), suponiendo que el ABB está balanceado.
    }


    public void canjearMillas(int id, double cantidad) {
        Usuario u = buscarUsuario(id); // O(logU)
        u.millasDisponibles -= cantidad;
        u.millasCanjeadas += cantidad;
        if (maxCanjeador == null || (u.millasCanjeadas + u.millasTransferidas) > (maxCanjeador.millasCanjeadas + maxCanjeador.millasTransferidas)){
            maxCanjeador = u;
        }
        heapMillas.actualizar(u); // O(logU)
        //O(logU)
    }

    public void transferirMillas(int origen, int destino, double cantidad) {
        Usuario u_origen = buscarUsuario(origen);   // O(LogU)
        Usuario u_destino = buscarUsuario(destino);// O(logU)
        
        // hay que chequear que pasa si se pasa del monton del total
        u_origen.millasDisponibles -= cantidad;
        u_destino.millasDisponibles += cantidad;
        u_origen.millasTransferidas += cantidad;

        if (maxCanjeador == null || (u_origen.millasCanjeadas + u_origen.millasTransferidas) > (maxCanjeador.millasCanjeadas + maxCanjeador.millasTransferidas)){
            maxCanjeador = u_origen;
        }
       // actualizarRelacion(u_origen, u_destino);
        heapMillas.actualizar(u_origen); //O(logU)
        heapMillas.actualizar(u_destino); //O(logU)
        // O(logU)
    }

    private Usuario buscarUsuario(int id) { 
        return usuarios.obtener(new Usuario(id)); // O(logU)
    }

    public double consultarSaldo(int id) {
        Usuario u = buscarUsuario(id); //O(logU)

        if (u == null) {
            throw new RuntimeException("Usuario inexistente");
        }

        return u.millasDisponibles;
        }
/*
private void actualizarRelacion(Usuario origen, Usuario destino) {
    Relacion relacion = relaciones.obtener(origen.id, destino.id);

    // Si es la primera transferencia entre estos usuarios
    if (relacion == null) {

        // Si alguno tenía una pareja exclusiva, deja de serlo
        if (origen.parejaExclusiva != null) {
            invalidarPareja(origen);
        }

        if (destino.parejaExclusiva != null) {
            invalidarPareja(destino);
        }

        // Creamos la relación y la guardamos en la tabla hash
        relacion = new Relacion(origen.id, destino.id);
        relaciones.insertar(relacion);

        // Ahora cada uno tiene una contraparte distinta más
        origen.cantidadContrapartes++;
        destino.cantidadContrapartes++;
    }

    // Registramos el sentido de esta transferencia
    relacion.registrarTransferencia(origen.id, destino.id);

    // Si se transfirieron en ambos sentidos y no tienen otras contrapartes
    if (relacion.huboEnAmbasDirecciones()
            && origen.cantidadContrapartes == 1
            && destino.cantidadContrapartes == 1
            && origen.parejaExclusiva == null
            && destino.parejaExclusiva == null) {

        // Agregamos la relación a la lista de parejas exclusivas
        relacion.handleExclusivo = paresExclusivos.agregarAtras(relacion);

        origen.parejaExclusiva = relacion;
        destino.parejaExclusiva = relacion;
    }
}

private void invalidarPareja(Usuario usuario) {
    Relacion relacion = usuario.parejaExclusiva;

    if (relacion != null) {
        // Sacamos la relación de la lista en O(1)
        relacion.handleExclusivo.eliminar();
        relacion.handleExclusivo = null;

        // Buscamos al otro usuario de la pareja
        int otroId;

        if (relacion.obtenerIdMenor() == usuario.id) {
            otroId = relacion.obtenerIdMayor();
        } else {
            otroId = relacion.obtenerIdMenor();
        }

        Usuario otro = buscarUsuario(otroId);

        // Ninguno sigue teniendo pareja exclusiva
        usuario.parejaExclusiva = null;
        otro.parejaExclusiva = null;
    }
}
*/
    public String consultarCategoria(int id) {
        Usuario u = buscarUsuario(id); //O(logU)

        if (u == null) {
            throw new RuntimeException("Usuario inexistente");
        }

        return categoriaPorMillas(u.millasTotales);

       
    }

    public int[] rankingMillas(int n) {
        
        int cantidad = Math.min(n, heapMillas.tamanio());

        if (cantidad <= 0) {
            return new int[0];
        }

        int[] ranking = new int[cantidad];
        ArrayList<Usuario> auxiliares = new ArrayList<>();
        //se repite N veces
        for (int i = 0; i < cantidad; i++) {
            Usuario u = heapMillas.extraerMaximo(); //O(logU)

            ranking[i] = u.id;
            auxiliares.add(u);
        }

        for (Usuario u : auxiliares) {
            heapMillas.insertar(u); // O(logU)
        }

        return ranking;
        
       //O(NlogU)
    }

    public void iniciarPromocion(String nombre, double factor) {
        promociones.insertar(nombre, factor);  // O(|L|)  
    }

    public void iniciarPromocionConTope(String nombre, double factor, double tope) {
        promociones.insertarConTope(nombre, factor, tope);   //O(|L|)  
    }

    public void finalizarPromocion(String nombre) {
        promociones.eliminar(nombre);   // O(|L|) 
    }

    public int elMasAcumulador() {
        if (maxAcumulador == null) {return -1;} // O(1)
        return maxAcumulador.id; // O(1)
    }

    public int elMasCanjeador() {
        if(maxCanjeador == null) {return -1;} // O(1)
        return maxCanjeador.id; // O(1)
    }

    //todavia hay que ver como lo implementamos 
    public int[][] paresTransferidoresExclusivos() {
        throw new UnsupportedOperationException("Sin implementar");
    }
}
