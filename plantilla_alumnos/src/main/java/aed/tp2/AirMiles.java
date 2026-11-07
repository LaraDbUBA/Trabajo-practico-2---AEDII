package aed.tp2;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
public class AirMiles implements SistemaAirMiles {
    private ABB<Usuario> usuarios;
    private Usuario maxAcumulador;
    private Usuario maxCanjeador;
    private MaxHeap<Usuario> heapMillas;
    private Trie promociones;
    private ListaEnlazada<Relacion> paresExclusivos; 
    
    private class Usuario implements Comparable<Usuario>{
        int id;
        double millasDisponibles; 
        double millasTotales; 
        double millasCanjeadas; 
        double millasTransferidas; 
        int posicionHeap;
        int esExclusivoCon; // tiene que ser el id

        Handle<Relacion> handlePar; 

        Usuario(int id){
            this.id = id;
            this.millasDisponibles = 0;
            this.millasTotales = 0;
            this.millasCanjeadas = 0;
            this.millasTransferidas = 0;
            this.posicionHeap = -1;
            this.esExclusivoCon = -2;
            this.handlePar = null;
            

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
        paresExclusivos = new ListaEnlazada<>();
        heapMillas = new MaxHeap<Usuario>(new MaxHeap.Operaciones<Usuario>() {
            @Override
            public int comparar(Usuario a, Usuario b) {
                return Double.compare(
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
        

        if (u.millasTotales > maxAcumulador.millasTotales){maxAcumulador = u;}
        if (referidor.millasTotales > maxAcumulador.millasTotales){maxAcumulador = referidor;}
        
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
    private String categoriaPorMillas(double millas) {
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

        // Complejidad: O(log U + |L|)
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
    private void eliminarPareja(Usuario usuario){
        if (usuario.handlePar != null){
            int id_otro = usuario.esExclusivoCon;
            Usuario otro = buscarUsuario(id_otro); //O(logU), no me cambia nada en mi implemetacion de transferirMillas
            usuario.handlePar.eliminar();
            usuario.handlePar = null;
            otro.handlePar = null;
            otro.esExclusivoCon = -1;
            

        }
    }

    private void actualizarExclusividad(Usuario usuario1, Usuario usuario2){
        if (usuario1.esExclusivoCon == -1){
            return;
        }
        
        // un entero no puede ser null, asignamos un valor que indique que nunca se paso con nadie
        // por ejemplo -2
        if (usuario1.esExclusivoCon == -2){
            usuario1.esExclusivoCon = usuario2.id;
        }

        if (usuario1.esExclusivoCon != usuario2.id){
            eliminarPareja(usuario1);
            eliminarPareja(usuario2);
            usuario1.esExclusivoCon = -1;
            usuario2.esExclusivoCon = -1;
        }
    }
    
    private void actualizarRelacion(Usuario origen, Usuario destino){
        actualizarExclusividad(origen, destino);
        actualizarExclusividad(destino, origen);

        if (origen.esExclusivoCon == destino.id && 
            destino.esExclusivoCon == origen.id &&
            origen.handlePar == null &&
            destino.handlePar == null){
            Relacion relacion = new Relacion(origen.id, destino.id);
            Handle<Relacion> handle =  paresExclusivos.agregarAtras(relacion);
            origen.handlePar = handle;
            destino.handlePar = handle;  
        }
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
        heapMillas.actualizar(u_origen); //O(logU)
        heapMillas.actualizar(u_destino); //O(logU)
        // O(logU)
        actualizarRelacion(u_origen, u_destino); //O(logU)

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

    
    public int[][] paresTransferidoresExclusivos() {
        
        int cantidad = paresExclusivos.longitud();
        int[][] resultado = new int[cantidad][2];

        Iterador<Relacion> iter = paresExclusivos.iterador();
        int i = 0;

        while (iter.haySiguiente()) {
            Relacion relacion = iter.siguiente();

            resultado[i][0] = relacion.obtenerIdMenor();
            resultado[i][1] = relacion.obtenerIdMayor();

            i++;
        }

        return resultado;
    }
} 
            /*Sea P la longitud de pares exclusivos
    El recorrido es O(P), y como cada pareja involucra dos 
    usuarios distintos, P < U/2. Por eso, la complejidad 
    final es O(U). */
    




