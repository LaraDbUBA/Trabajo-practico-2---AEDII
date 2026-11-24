package aed.tp2;

import java.rmi.UnexpectedException;
import java.util.ArrayList;

public class AirMiles implements SistemaAirMiles {
    private ABB<Integer, Handle<Usuario>> usuarios;
    private Usuario maxAcumulador;
    private Usuario maxCanjeador;
    private MaxHeap<Usuario> heapMillas;
    private Trie promociones;
    private ListaEnlazada<Relacion> paresExclusivos;

    public AirMiles() {
        usuarios = new ABB<>(); // O(1)
        promociones = new Trie(); // O(1)
        promociones.insertar("NoPromo", 1.0);
        paresExclusivos = new ListaEnlazada<>();
        heapMillas = new MaxHeap<>();
    }

    public AirMiles crearSistema() {
        return new AirMiles(); // O(1)
    }

    public void registrarUsuario(int id) {
        Usuario u = new Usuario(id); // O(1)

        Handle<Usuario> handle = heapMillas.insertar(u); // O(logU)

        usuarios.definir(id, handle); // O(logU) promedio y O(U) en peor caso

        if (maxAcumulador == null) {
            maxAcumulador = u;
        }

        if (maxCanjeador == null) {
            maxCanjeador = u;
        }
        // Complejidad final: O(logU)
    }

    public void registrarUsuarioConReferidor(int id, int ref) {
        Usuario u = new Usuario(id); // O(1)

        // Antes de agregar aumentamos la cantidad de millas
        u.acumular(500.0);

        Handle<Usuario> handle = heapMillas.insertar(u); // O(logU)

        usuarios.definir(id, handle); // O(logU) promedio y O(U) en peor caso

        Handle<Usuario> h_referidor = buscarHandleUsuario(ref); // O(logU) promedio y O(U) en peor caso
        Usuario referidor = h_referidor.valor();
        referidor.acumular(500.0);
        h_referidor.modificar(referidor); // O(logU)

        if (u.totales() > maxAcumulador.totales()) {
            maxAcumulador = u;
        }
        if (referidor.totales() > maxAcumulador.totales()) {
            maxAcumulador = referidor;
        }
    }

    public void acumularMillas(int id, double distancia, String nombrePromo) {
        // requiere: el usuario existe en el sistema, distancia > 0,
        Handle<Usuario> h_usuario = buscarHandleUsuario(id); // O(logU) promedio y O(U) en peor caso
        Usuario u = h_usuario.valor(); // O(1)

        double factorCategoria = u.factorCategoria(); // O(1)

        double millasBase = distancia * factorCategoria; // O(1)

        // Busca la promoción una sola vez y controla su tope.
        double millas = promociones.calcularMillas(nombrePromo, millasBase); // O(|L|)

        u.acumular(millas); // O(1)

        h_usuario.modificar(u); // O(log U)

        if (u.totales() > maxAcumulador.totales()) {
            maxAcumulador = u; // O(1)
        }

        // Complejidad: O(log U + |L|)
    }

    public void canjearMillas(int id, double cantidad) {
        // requiere: el usuario existe en el sistema y 0 < cantidad <= disponibles
        Handle<Usuario> h_usuario = buscarHandleUsuario(id); // O(logU) promedio y O(U) en peor caso
        Usuario u = h_usuario.valor(); // O(1)

        u.canjear(cantidad); // O(1)

        if (u.perdidas() > maxCanjeador.perdidas()) {
            maxCanjeador = u;
        }

        h_usuario.modificar(u); // O(logU)
        // O(logU)
    }

    private void eliminarPareja(Usuario usuario) {
        if (usuario.tieneHandlePar()) {
            int id = usuario.id();
            Handle<Relacion> handle = usuario.obtenerHandlePar();
            int id_otro = handle.valor().otro(id);
            Usuario otro = buscarHandleUsuario(id_otro).valor(); // O(logU) promedio y O(U) en peor caso
            handle.eliminar();
            usuario.tiraHandlePar();
            otro.tiraHandlePar();

        }
    }

    private void actualizarRelacion(Usuario origen, Usuario destino) {
        int id_origen = origen.id();
        int id_destino = destino.id();

        if (origen.rompeExclusividad(id_destino)) {
            eliminarPareja(origen);
        }
        if (destino.rompeExclusividad(id_origen)) {
            eliminarPareja(destino);
        }

        if (origen.esExclusivoCon(id_destino) && destino.esExclusivoCon(id_origen) && !origen.tieneHandlePar()
                && !destino.tieneHandlePar()) {
            Relacion relacion = new Relacion(id_origen, id_destino);
            Handle<Relacion> handle = paresExclusivos.agregarAtras(relacion);
            origen.guardarHandlePar(handle);
            destino.guardarHandlePar(handle);
        }
    }

    public void transferirMillas(int origen, int destino, double cantidad) {
        // requiere: origen y destino existen, origen ≠ destino,
        // cantidad > 0 y disponibles(origen) ≥ cantidad
        Handle<Usuario> h_origen = buscarHandleUsuario(origen); // O(logU) promedio y O(U) en peor caso
        Usuario u_origen = h_origen.valor();
        Handle<Usuario> h_destino = buscarHandleUsuario(destino); // O(logU) promedio y O(U) en peor caso
        Usuario u_destino = h_destino.valor();

        u_origen.transferir(cantidad);
        u_destino.recibir(cantidad); // solo suma disponibles y no cumple disponibles <= totales siempre, pero pasa
                                     // test transferenciaRecibidaNoSumaTotales

        if (u_origen.perdidas() > maxCanjeador.perdidas()) {
            maxCanjeador = u_origen;
        }

        h_origen.modificar(u_origen); // O(logU)
        h_destino.modificar(u_destino); // O(logU)
        // O(logU)
        actualizarRelacion(u_origen, u_destino); // O(logU)

    }

    private Handle<Usuario> buscarHandleUsuario(int id) {
        return usuarios.obtener(id);
    }

    public double consultarSaldo(int id) {
        // requiere: id asociado a un usuario perteneciente al sistema
        Usuario u = buscarHandleUsuario(id).valor(); // O(logU) promedio y O(U) en peor caso
        return u.disponibles();
    }

    public String consultarCategoria(int id) {
        // requiere: id asociado a un usuario perteneciente al sistema
        Usuario u = buscarHandleUsuario(id).valor(); // O(logU) promedio y O(U) en peor caso
        return u.categoria();
    }

    public int[] rankingMillas(int n) {
        // requiere: n>=1
        int cantidad = Math.min(n, heapMillas.tamanio());
        ArrayList<Usuario> aux = heapMillas.primeros(cantidad);
        int[] ranking = new int[cantidad];
        for(int i = 0;i < cantidad;i++) {
            int id = aux.get(i).id();
            ranking[i] = id;
        }
        return ranking;

        // O(NlogU)
    }

    public void iniciarPromocion(String nombre, double factor) {
        promociones.insertar(nombre, factor); // O(|L|)
    }

    public void iniciarPromocionConTope(String nombre, double factor, double tope) {
        promociones.insertarConTope(nombre, factor, tope); // O(|L|)
    }

    public void finalizarPromocion(String nombre) {
        promociones.eliminar(nombre); // O(|L|)
    }

    public int elMasAcumulador() {
        if (maxAcumulador == null) {
            return -1;
        } // O(1)
        return maxAcumulador.id(); // O(1)
    }

    public int elMasCanjeador() {
        if (maxCanjeador == null) {
            return -1;
        } // O(1)
        return maxCanjeador.id(); // O(1)
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
/*
 * Sea P la longitud de pares exclusivos
 * El recorrido es O(P), y como cada pareja involucra dos
 * usuarios distintos, P < U/2. Por eso, la complejidad
 * final es O(U).
 */
