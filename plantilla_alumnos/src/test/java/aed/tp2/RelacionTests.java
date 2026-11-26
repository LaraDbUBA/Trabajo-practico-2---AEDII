package aed.tp2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Tests solo de Relacion. 
public class RelacionTests {

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
    void otroDevuelveLaOtraPuntaDelPar() {
        // AirMiles lo usa para encontrar a la ex-pareja cuando se rompe un par
        Relacion r = new Relacion(7, 3);
        assertEquals(3, r.otro(7));
        assertEquals(7, r.otro(3));
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
}