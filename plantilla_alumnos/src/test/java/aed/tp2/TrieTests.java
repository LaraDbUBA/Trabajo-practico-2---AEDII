package aed.tp2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class TrieTests {
    // Probamos el Trie genérico con promociones, como lo usa AirMiles
    private Trie<Promocion> trie;

    @BeforeEach
    void setUp() {
        trie = new Trie<>();
    }

    @Test
    void claveQueEsPrefijoDeOtra() {
        trie.definir("PromoVerano", new Promocion(2.0));

        // El camino P-R-O-M-O existe, pero ahí no termina ninguna clave
        assertTrue(trie.esta("PromoVerano"));
        assertFalse(trie.esta("Promo"));
        assertFalse(trie.esta("PromoVeranoLargo"));
        assertFalse(trie.esta("Otra"));

        // Al definir el prefijo como clave, cada una conserva su valor
        trie.definir("Promo", new Promocion(3.0));
        assertTrue(trie.esta("Promo"));
        assertEquals(3.0, trie.obtener("Promo").factor());
        assertEquals(2.0, trie.obtener("PromoVerano").factor());
    }

    @Test
    void definirUnaClaveExistentePisaElValor() {
        trie.definir("PromoVerano", new Promocion(2.0));
        trie.definir("PromoVerano", new Promocion(4.0));
        assertEquals(4.0, trie.obtener("PromoVerano").factor());

        // Una promoción con tope reiniciada arranca con el tope nuevo
        trie.definir("Flash", new Promocion(3.0, 300));
        trie.obtener("Flash").restarCupo(300);
        trie.definir("Flash", new Promocion(2.0, 200));
        assertEquals(200.0, trie.obtener("Flash").cupo());
    }

    @Test
    void borrarYVolverADefinir() {
        trie.definir("PromoVerano", new Promocion(2.0));
        trie.borrar("PromoVerano");
        assertFalse(trie.esta("PromoVerano"));

        // Se vuelve a definir con el mismo nombre y otro valor
        trie.definir("PromoVerano", new Promocion(4.0));
        assertTrue(trie.esta("PromoVerano"));
        assertEquals(4.0, trie.obtener("PromoVerano").factor());
    }

    @Test
    void clavesConPrefijoComunSonIndependientes() {
        trie.definir("PromoDoble", new Promocion(2.0));
        trie.definir("PromoTriple", new Promocion(3.0));

        // Borrar una clave no afecta a otra que comparte el camino P-R-O-M-O
        trie.borrar("PromoDoble");
        assertFalse(trie.esta("PromoDoble"));
        assertTrue(trie.esta("PromoTriple"));
        assertEquals(3.0, trie.obtener("PromoTriple").factor());
    }

    @Test
    void borrarElPrefijoNoBorraLaClaveLargaYAlReves() {
        trie.definir("Promo", new Promocion(2.0));
        trie.definir("PromoVerano", new Promocion(3.0));

        // Borrar la corta: la poda no puede llevarse el camino de la larga
        trie.borrar("Promo");
        assertFalse(trie.esta("Promo"));
        assertTrue(trie.esta("PromoVerano"));

        // Borrar la larga: la poda tiene que frenar en la corta
        trie.definir("Promo", new Promocion(2.0));
        trie.borrar("PromoVerano");
        assertFalse(trie.esta("PromoVerano"));
        assertTrue(trie.esta("Promo"));
        assertEquals(2.0, trie.obtener("Promo").factor());
    }

    @Test
    void aceptaTodosLosCaracteresDelAlfabeto() {
        // Espacios, ñ, tildes, números y signos (antes solo se aceptaban letras A-Z)
        trie.definir("Promo Año Nuevo 2027!", new Promocion(2.0));
        trie.definir("Canción", new Promocion(3.0));
        assertTrue(trie.esta("Promo Año Nuevo 2027!"));
        assertEquals(3.0, trie.obtener("Canción").factor());
        assertFalse(trie.esta("Cancion"));

        // Mayúsculas y minúsculas son claves distintas
        trie.definir("Flash", new Promocion(4.0));
        assertTrue(trie.esta("Flash"));
        assertFalse(trie.esta("flash"));
    }
}