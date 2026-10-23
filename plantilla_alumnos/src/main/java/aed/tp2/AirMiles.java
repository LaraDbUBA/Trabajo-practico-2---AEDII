package aed.tp2;
// importar el ABB
public class AirMiles implements SistemaAirMiles {
    private ABB<Integer, Usuario> usuarios;
    private Usuario maxAcumulador;
    private Usuario maxCanjeador;
    // falta el maxheap 
    // falta el trie 
    
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

        @Override
        public int compareTo(Usuario otro) {
        return Integer.compare(this.id, otro.id);
    }
    }

    
    public AirMiles() {
        usuarios = new ABB<>();
    }

    public void registrarUsuario(int id) {
        usuarios.insertar(new Usuario(id));
    }

    public void registrarUsuarioConReferidor(int id, int ref) {
        usuarios.insertar(new Usuario(id));
    }

    public void acumularMillas(int id, double distancia, String nombrePromo) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void canjearMillas(int id, double cantidad) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void transferirMillas(int origen, int destino, double cantidad) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public double consultarSaldo(int id) {
        return obtener(id).millasDisponibles;
    }

    public String consultarCategoria(int id) {
        int millas = obtener(id).millasTotales;
        // o poner una variable res e ir cambiando, en realidad no me gustan tantos return
        if (millas < 10000){
            return "Bronce";
        } else if (millas < 50000){
            return "Platino";
        } else if (millas < 1000000){
            return "Oro";
        } else return "Platino";
       
    }

    public int[] rankingMillas(int n) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void iniciarPromocion(String nombre, double factor) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void iniciarPromocionConTope(String nombre, double factor, double tope) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void finalizarPromocion(String nombre) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public int elMasAcumulador() {
        return maxAcumulador.id;
    }

    public int elMasCanjeador() {
        return maxCanjeador.id;
    }

    public int[][] paresTransferidoresExclusivos() {
        throw new UnsupportedOperationException("Sin implementar");
    }
}
