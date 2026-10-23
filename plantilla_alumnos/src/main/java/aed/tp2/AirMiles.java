package aed.tp2;

public class AirMiles implements SistemaAirMiles {
    private ABB<Integer, Usuario> usuarios;
    private Usuario maxAcumulador;
    private Usuario maxCanjeador;
    
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
        throw new UnsupportedOperationException("Sin implementar");
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
