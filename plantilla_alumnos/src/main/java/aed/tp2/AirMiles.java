package aed.tp2;

public class AirMiles implements SistemaAirMiles {
    private ABB<Usuario> usuarios;
    private Usuario maxAcumulador;
    private Usuario maxCanjeador;
    // private MaxHeap heapMillas;
    private Trie promociones;
    
    private class Usuario implements Comparable<Usuario>{
        int id;
        int millasDisponibles;
        int millasTotales;
        int millasCanjeadas;
        int millasTransferidas;

        Usuario(int id){
            this.id = id;
            this.millasDisponibles = 0;
            this.millasTotales = 0;
            this.millasCanjeadas = 0;
            this.millasTransferidas = 0;

        }
        // hay que ver aca como hacemos para lo del heap
        @Override
        public int compareTo(Usuario otro) {
        return Integer.compare(this.id, otro.id);
    }
    }

    
    public AirMiles() {
        usuarios = new ABB<>();
        promociones = new Trie();
        // heapMillas = new MaxHeap();
    }

    public void registrarUsuario(int id) {
        usuarios.insertar(new Usuario(id));
        // insertar en heap
    }

    public void registrarUsuarioConReferidor(int id, int ref) {
        usuarios.insertar(new Usuario(id));
        // insertar en heap
    }
    private double consultarFactorCategoria(String categoria) {
        double res = 0.0;

        if (categoria.equals("Bronce")) {
            res = 1.0;
        } else if (categoria.equals("Plata")) {
            res = 1.5;
        } else if (categoria.equals("Oro")) {
            res = 2.0;
        }

        return res;
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

        return res;
    }

    public void acumularMillas(int id, double distancia, String nombrePromo) {
        Usuario u = buscarUsuario(id);
        String categoria = categoriaPorMillas(u.millasTotales);
        double factorCategoria = consultarFactorCategoria(categoria);
        double factor = promociones.obtenerFactor(nombrePromo);

        double millas = distancia * factorCategoria * factor ;
        u.millasTotales += millas;
        u.millasDisponibles += millas;
        
        // actualizar heap
        if (maxAcumulador == null ||u.millasTotales > maxAcumulador.millasTotales) {
            maxAcumulador = u;
        }

    }

    public void canjearMillas(int id, double cantidad) {
        Usuario u = buscarUsuario(id);
        u.millasDisponibles -= cantidad;
        u.millasCanjeadas += cantidad;
        if (maxCanjeador == null || (u.millasCanjeadas + u.millasTransferidas) > (maxCanjeador.millasCanjeadas + maxCanjeador.millasTransferidas)){
            maxCanjeador = u;
        }
        //actualizar heap
    }

    public void transferirMillas(int origen, int destino, double cantidad) {
        Usuario u_origen = buscarUsuario(origen);   
        Usuario u_destino = buscarUsuario(destino);
        
        // hay que chequear que pasa si se pasa del monton del total
        u_origen.millasDisponibles -= cantidad;
        u_destino.millasDisponibles += cantidad;
        u_origen.millasTransferidas += cantidad;

        if (maxCanjeador == null || (u_origen.millasCanjeadas + u_origen.millasTransferidas) > (maxCanjeador.millasCanjeadas + maxCanjeador.millasTransferidas)){
            maxCanjeador = u_origen;
        }
        //actualizar heap
    }

    private Usuario buscarUsuario(int id) { 
        return usuarios.obtener(new Usuario(id));
    }

    public double consultarSaldo(int id) {
        Usuario u = buscarUsuario(id);

        if (u == null) {
            throw new RuntimeException("Usuario inexistente");
        }

        return u.millasDisponibles;
        }

    public String consultarCategoria(int id) {
        Usuario u = buscarUsuario(id);

        if (u == null) {
            throw new RuntimeException("Usuario inexistente");
        }

        return categoriaPorMillas(u.millasTotales);

       
    }

    public int[] rankingMillas(int n) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void iniciarPromocion(String nombre, double factor) {
        promociones.insertar(nombre, factor);    
    }

    public void iniciarPromocionConTope(String nombre, double factor, double tope) {
        promociones.insertarConTope(nombre, factor, tope);    
    }

    public void finalizarPromocion(String nombre) {
        promociones.eliminar(nombre);    
    }

    public int elMasAcumulador() {
        return maxAcumulador.id;
    }

    public int elMasCanjeador() {
        return maxCanjeador.id;
    }

    //todavia hay que ver como lo implementamos 
    public int[][] paresTransferidoresExclusivos() {
        throw new UnsupportedOperationException("Sin implementar");
    }
}
