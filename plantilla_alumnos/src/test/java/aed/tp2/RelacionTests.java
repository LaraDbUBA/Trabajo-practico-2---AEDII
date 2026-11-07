package aed.tp2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class RelacionTests {
    private ListaEnlazada<Relacion> pares;

    @BeforeEach
    void setUp() {
        pares = new ListaEnlazada<>();
    }

    @Test
    void relacionNormalizaElOrdenDeLosIds() {
        // El par se guarda igual sin importar quién transfirió a quién
        Relacion r1 = new Relacion(7, 3);
        Relacion r2 = new Relacion(3, 7);

        assertEquals(3, r1.obtenerIdMenor());
        assertEquals(7, r1.obtenerIdMayor());
        assertEquals(3, r2.obtenerIdMenor());
        assertEquals(7, r2.obtenerIdMayor());
    }

    @Test
    void compareToOrdenaPorIdMenorYLuegoPorIdMayor() {
        // (3, 7) y (7, 3) representan el mismo par
        assertEquals(0, new Relacion(3, 7).compareTo(new Relacion(7, 3)));

        // Primero decide el id menor: (1, 9) < (2, 3)
        assertTrue(new Relacion(1, 9).compareTo(new Relacion(2, 3)) < 0);

        // Con el mismo id menor, decide el id mayor: (2, 3) < (2, 5)
        assertTrue(new Relacion(2, 3).compareTo(new Relacion(2, 5)) < 0);
        assertTrue(new Relacion(2, 5).compareTo(new Relacion(2, 3)) > 0);
    }

    @Test
    void eliminarParDelMedioPorHandle() {
        pares.agregarAtras(new Relacion(1, 2));
        Handle<Relacion> h34 = pares.agregarAtras(new Relacion(3, 4));
        pares.agregarAtras(new Relacion(5, 6));
        assertEquals(3, pares.longitud());

        // Se rompe la exclusividad de (3, 4): se elimina en O(1) con su handle
        h34.eliminar();

        // Los otros pares quedan, en el mismo orden
        assertEquals(2, pares.longitud());
        assertEquals(1, pares.obtener(0).obtenerIdMenor());
        assertEquals(5, pares.obtener(1).obtenerIdMenor());
    }

    @Test
    void eliminarPrimerYUltimoParPorHandle() {
        Handle<Relacion> h12 = pares.agregarAtras(new Relacion(1, 2));
        pares.agregarAtras(new Relacion(3, 4));
        Handle<Relacion> h56 = pares.agregarAtras(new Relacion(5, 6));

        // Eliminar los extremos de la lista
        h12.eliminar();
        h56.eliminar();
        assertEquals(1, pares.longitud());
        assertEquals(3, pares.obtenerPrimero().obtenerIdMenor());
        assertEquals(3, pares.obtenerUltimo().obtenerIdMenor());

        // Después de eliminar los extremos se puede seguir agregando pares
        pares.agregarAtras(new Relacion(7, 8));
        assertEquals(2, pares.longitud());
        assertEquals(7, pares.obtenerUltimo().obtenerIdMenor());
    }

    @Test
    void eliminarDosVecesElMismoHandleNoRompeLaLista() {
        // En AirMiles los dos usuarios del par comparten el mismo handle
        Handle<Relacion> h12 = pares.agregarAtras(new Relacion(1, 2));
        pares.agregarAtras(new Relacion(3, 4));

        h12.eliminar();
        h12.eliminar(); // segunda vez: no tiene que eliminar otro par

        assertEquals(1, pares.longitud());
        assertEquals(3, pares.obtenerPrimero().obtenerIdMenor());
    }
}