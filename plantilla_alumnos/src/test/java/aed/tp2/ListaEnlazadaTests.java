package aed.tp2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

// Tests de la ListaEnlazada y sus handles, con enteros
public class ListaEnlazadaTests {
    private ListaEnlazada<Integer> lista;

    @BeforeEach
    void setUp() {
        lista = new ListaEnlazada<>();
    }

    // Recorre la lista con su iterador (como hace AirMiles) y devuelve los elementos en orden
    private int[] elementos() {
        int[] res = new int[lista.longitud()];
        Iterador<Integer> it = lista.iterador();
        int i = 0;
        while (it.haySiguiente()) {
            res[i] = it.siguiente();
            i++;
        }
        return res;
    }

    @Test
    void agregarAtrasMantieneElOrden() {
        lista.agregarAtras(1);
        lista.agregarAtras(2);
        lista.agregarAtras(3);
        assertEquals(3, lista.longitud());
        assertArrayEquals(new int[]{1, 2, 3}, elementos());
    }

    @Test
    void handleDevuelveSuElemento() {
        lista.agregarAtras(1);
        Handle<Integer> h = lista.agregarAtras(2);
        assertEquals(2, (int) h.valor());
    }

    @Test
    void eliminarDelMedioPorHandle() {
        lista.agregarAtras(1);
        Handle<Integer> h2 = lista.agregarAtras(2);
        lista.agregarAtras(3);

        // Se elimina en O(1) con su handle, sin recorrer la lista
        h2.eliminar();

        // Los otros quedan, en el mismo orden
        assertEquals(2, lista.longitud());
        assertArrayEquals(new int[]{1, 3}, elementos());
    }

    @Test
    void eliminarPrimeroYUltimoPorHandle() {
        Handle<Integer> h1 = lista.agregarAtras(1);
        lista.agregarAtras(2);
        Handle<Integer> h3 = lista.agregarAtras(3);

        // Eliminar los extremos de la lista
        h1.eliminar();
        h3.eliminar();
        assertArrayEquals(new int[]{2}, elementos());

        // Después de eliminar los extremos se puede seguir agregando
        lista.agregarAtras(4);
        assertArrayEquals(new int[]{2, 4}, elementos());
    }

    @Test
    void eliminarElUnicoElementoDejaLaListaVacia() {
        Handle<Integer> h = lista.agregarAtras(1);
        h.eliminar();
        assertEquals(0, lista.longitud());
        assertFalse(lista.iterador().haySiguiente());

        // y se puede volver a usar
        lista.agregarAtras(5);
        assertArrayEquals(new int[]{5}, elementos());
    }

    @Test
    void eliminarDosVecesElMismoHandleNoRompeLaLista() {
        Handle<Integer> h1 = lista.agregarAtras(1);
        lista.agregarAtras(2);

        h1.eliminar();
        h1.eliminar(); // segunda vez: no tiene que eliminar otro elemento

        assertEquals(1, lista.longitud());
        assertArrayEquals(new int[]{2}, elementos());
    }
}