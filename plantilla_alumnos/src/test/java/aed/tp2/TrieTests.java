package aed.tp2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class TrieTests {
    private Trie trie;

    @BeforeEach
    void setUp() {
        trie = new Trie();
    }

    @Test
    void promocionQueEsPrefijoDeOtra() {
        trie.insertar("PromoVerano", 2.0);

        // El camino P-R-O-M-O existe, pero ahí no termina ninguna promoción
        assertTrue(trie.existe("PromoVerano"));
        assertFalse(trie.existe("Promo"));
        assertFalse(trie.existe("PromoVeranoLargo"));

        // Al insertar el prefijo como promoción, cada una conserva su factor
        trie.insertar("Promo", 3.0);
        assertTrue(trie.existe("Promo"));
        assertEquals(3.0, trie.obtenerFactor("Promo"));
        assertEquals(2.0, trie.obtenerFactor("PromoVerano"));
    }

    @Test
    void topeSeConsumeGlobalmente() {
        trie.insertarConTope("Flash", 3.0, 300);

        // Dos acumulaciones bajo la misma promoción comparten el tope: 150 + 150 = 300
        assertEquals(150.0, trie.calcularMillas("Flash", 50));
        assertEquals(150.0, trie.calcularMillas("Flash", 50));

        // Tope alcanzado: ya no se aplica el factor de la promoción
        assertEquals(10.0, trie.calcularMillas("Flash", 10));
    }

    @Test
    void reiniciarPromocionConElMismoNombre() {
        // Promoción sin tope: al finalizarla deja de aplicar su factor
        trie.insertar("PromoVerano", 2.0);
        trie.eliminar("PromoVerano");
        assertEquals(100.0, trie.calcularMillas("PromoVerano", 100));

        // Se vuelve a iniciar con el mismo nombre y otro factor
        trie.insertar("PromoVerano", 4.0);
        assertEquals(400.0, trie.calcularMillas("PromoVerano", 100));

        // Promoción con tope agotado: al reiniciarla el tope arranca de cero
        trie.insertarConTope("Flash", 3.0, 300);
        assertEquals(300.0, trie.calcularMillas("Flash", 100));
        trie.insertarConTope("Flash", 2.0, 200);
        assertEquals(200.0, trie.calcularMillas("Flash", 100));
    }

    @Test
    void promocionesConPrefijoComunSonIndependientes() {
        trie.insertar("NoPromo", 1.0);
        trie.insertar("PromoDoble", 2.0);
        trie.insertar("PromoTriple", 3.0);

        // Finalizar una promoción no afecta a otra que comparte el camino P-R-O-M-O
        trie.eliminar("PromoDoble");
        assertEquals(100.0, trie.calcularMillas("PromoDoble", 100));
        assertEquals(300.0, trie.calcularMillas("PromoTriple", 100));

        // Una promoción sin tope no se agota: se puede usar muchas veces
        assertEquals(300.0, trie.calcularMillas("PromoTriple", 100));
        assertEquals(50.0, trie.calcularMillas("NoPromo", 50));
    }
}