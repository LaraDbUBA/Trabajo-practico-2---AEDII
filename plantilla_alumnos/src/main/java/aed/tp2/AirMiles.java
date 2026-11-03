package aed.tp2;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
public class AirMiles implements SistemaAirMiles {
    private ABB<Usuario> usuarios;
    private Usuario maxAcumulador;
    private Usuario maxCanjeador;
    private MaxHeap<Usuario> heapMillas;
    private Trie promociones;
    // private ListaEnlazada<Relacion> paresExclusivos; <- Idea
    
    private class Usuario implements Comparable<Usuario>{
        int id;
        int millasDisponibles; // CAMBIAR A DOUBLE
        int millasTotales; // CAMBIAR A DOUBLE
        int millasCanjeadas; // CAMBIAR A DOUBLE
        int millasTransferidas; // CAMBIAR A DOUBLE
        int posicionHeap;

        /*Agregamos Esto de idea */
        Usuario recibioDe;
        Usuario mandoA;
        boolean esExclusivo;
        Usuario esExclusivoCon;

        Usuario(int id){
            this.id = id;
            this.millasDisponibles = 0;
            this.millasTotales = 0;
            this.millasCanjeadas = 0;
            this.millasTransferidas = 0;
            this.posicionHeap = -1;


            /* Agregamos esto como Idea*/
            this.esExclusivo = true;
            this.esExclusivoCon = null;
            this.mandoA = null;
            this.recibioDe = null;

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
        

        if (u.millasTotales > maxAcumulador.millasTotales){maxAcumulador = u;}
        if (referidor.millasTotales > maxAcumulador.millasTotales){maxAcumulador = referidor;}

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
        heapMillas.actualizar(u_origen); //O(logU)
        heapMillas.actualizar(u_destino); //O(logU)
        // O(logU)


        /** Tenemos que verificar si el que recibio y el que mando ya tenían a alguien o si no había nadie.
         * <p>
         * Además fijate si el tema es solo cuando se manda y recibe o hay alguno más que no estas viendo
         * <p>
         * la trasferencia ocurre una vez por llamada asi que puede quedar casos que 1 -> 2 y que uno(1) tenga recibido:null y que dos(2) tenga mandeA: null. Hay que contemplar esos casos
         */

        verificarRecibioDe(origen, destino);
        verificarMandoA(origen, destino);
        
        // Chequeamos cada uno por si al final quedaron con distintos valores sus atributos de mandeA y recibeDe distintos
        sigueSiendoExclusivo(origen);
        sigueSiendoExclusivo(destino);
        
    
        /**
         * Se verifica que si despues de todo esto siguen los dos siendo excluviso entonces los los dos son pares
         * Para este punto si origen mando a otro destino o si destino recibio de otro origen que no sea el primero entonces esExclusivo pasaba a false
         * Entonces si hasta este punto siguen siendo los dos exclusivos es que ambos son exclusivosEntreSi
         */
        verificarSiSonPares(origen, destino);



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
        throw new UnsupportedOperationException("Sin implementar");
        /*
        
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
Sea P la longitud de pares exclusivos
El recorrido es O(P), y como cada pareja involucra dos 
usuarios distintos, P < U/2. Por eso, la complejidad 
final es O(U).
        */
    }

    void verificarRecibioDe(int mandoMiilas, int recibioMillas){
        
        /**
         * 1 Caso: Verificamos si no había recibido de nadie entonces se pone de el quien le mando
         * 2 Caso: Recibio antes de alguien pero tenemos que verificar de quien, si es la misma persona u otra.
         * Primero verificamos si recibio algo de alguien antes o no? Mejor chequear primero si ya es false entonces lo otro lo podemos convertir en null
         * Si verificamos primero si es false entonces todas tienen que ser false desde el inicio?
         * 
         */

        Usuario origen =  buscarUsuario(mandoMiilas);
        Usuario destino = buscarUsuario(recibioMillas);

        
        /**Todavía no entiendo el tema de null asi que no se como funciona el == con el tema de null, si es con números o como
         * <p>
         * Todos los estados van a empezar en esExclusivoCon = false;
         * <p>
         * Modificamos y movimos el caso de null primero asi no nos agarra un Null.pointerException
        */
        if (destino.recibioDe == null) {
            destino.recibioDe = origen;
        }


        else if (destino.recibioDe.id != origen.id) { 
            destino.esExclusivo = false;
            origen.esExclusivo = false;   
        }
        
        else if (destino.recibioDe.id == origen.id){
            return;
        }

        else {
            throw new UnknownError("Falto un caso que tenes que Chequear");
        }

    }

    void verificarMandoA(int mandoMillas, int recibioMillas) {
        Usuario elQueMando = buscarUsuario(mandoMillas);
        Usuario elQueRecibio = buscarUsuario(recibioMillas);

        /**
         * Se repiten los tres(3) casos que usamos para verificarRecibioDe
         * <p>
         * Caso 1: Si mande a alguien y ya había mandado a otra persona
         * Caso 2: No mande nunca y se guarda la primera persona a quien mande para despues comparar siempre
         * <p>
         * Modificamos y movimos el caso de null primero asi no nos agarra un Null.pointerException
         */

        if (elQueMando.mandoA == null) {
            elQueMando.mandoA = elQueRecibio;
        }

        else if (elQueMando.mandoA.id != elQueRecibio.id) {
            elQueMando.esExclusivo = false;
            elQueRecibio.esExclusivo = false;
        }

        else if (elQueMando.mandoA.id == elQueRecibio.id) {
            return;
        }
        
        else {
            throw new UnknownError("Falta chequear un caso que no estas viendo");
        }
    }


    void sigueSiendoExclusivo(int id) {
        Usuario usuario = buscarUsuario(id);
        //Tenemos que chequear el caso Null primero 
        if (usuario.recibioDe != null && usuario.mandoA != null && usuario.recibioDe.id != usuario.mandoA.id){usuario.esExclusivo = false;}
    }


    void verificarSiSonPares(int origen, int destino) {
        Usuario QuienMando = buscarUsuario(origen);
        Usuario QuienRecibio = buscarUsuario(destino);
        
        if(QuienMando.esExclusivo && QuienRecibio.esExclusivo) {
        QuienRecibio.esExclusivoCon = QuienMando;
        QuienMando.esExclusivoCon = QuienRecibio;
        }

        // Lo mas logico me parece al final de todo verificar aca dentro que si hay alguno que no es par entonces elimiinarlo

        //Eliminar el exclusivoCon y convertimos en false por si acaso
        else if (!QuienMando.esExclusivo || !QuienRecibio.esExclusivo) {
        
            QuienMando.esExclusivo = false;
            QuienRecibio.esExclusivo = false;
        
            if (QuienRecibio.esExclusivoCon != null) {
                Usuario exclusivoQuienRecibio = QuienRecibio.esExclusivoCon;
                exclusivoQuienRecibio.esExclusivo = false;
                exclusivoQuienRecibio.esExclusivoCon = null;
                QuienRecibio.esExclusivoCon = null;
            }


            if (QuienMando.esExclusivoCon != null){
                Usuario exclusivoQuienMando = QuienMando.esExclusivoCon;
                exclusivoQuienMando.esExclusivoCon = null;
                exclusivoQuienMando.esExclusivo = false;
                QuienMando.esExclusivoCon = null;
            }

     

        }
    }


    //ME FALTA EL TEMA DEL HANDLE Y CONECTARLO CON LA LISTA ENLAZADA PERO PRIEMRO QUIERO PREGUNTAR BIEN CUAL ERA EL TEMA DEL HANDLE QUE EL CONSDIERABA QUE PODÍA ESTAR ENCAPSULADO
    

}
