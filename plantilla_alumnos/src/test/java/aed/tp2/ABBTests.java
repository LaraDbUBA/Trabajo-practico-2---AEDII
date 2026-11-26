package aed.tp2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

// Tests del ABB como diccionario (clave -> valor)
public class ABBTests {
    private ABB<Integer, Integer> abb;

    @BeforeEach
    void setUp() {
        abb = new ABB<>();
    }

    @Test
    void definirYObtenerVariasClaves() {
        abb.definir(5, 50);
        abb.definir(2, 20);
        abb.definir(8, 80);

        // Cada clave devuelve su propio valor
        assertEquals(50, (int) abb.obtener(5));
        assertEquals(20, (int) abb.obtener(2));
        assertEquals(80, (int) abb.obtener(8));
    }

    @Test
    void definirUnaClaveExistentePisaElValor() {
        abb.definir(5, 50);
        abb.definir(5, 99);

        // No se duplica la clave: se reemplaza el valor
        assertEquals(99, (int) abb.obtener(5));
    }

    @Test
    void clavesInsertadasEnOrdenPeorCaso() {
        // Con claves en orden creciente el árbol queda como una lista (peor caso, altura U)
        for (int i = 1; i <= 100; i++) {
            abb.definir(i, i * 10);
        }

        // Igual se encuentran todas, de la primera a la última
        assertEquals(10, (int) abb.obtener(1));
        assertEquals(500, (int) abb.obtener(50));
        assertEquals(1000, (int) abb.obtener(100));
    }

    @Test
    void clavesDesordenadasBajanPorAmbosLados() {
        // 50 queda en la raíz; las menores van a la izquierda y las mayores a la derecha
        int[] claves = {50, 30, 70, 20, 40, 60, 80, 35, 65};
        for (int c : claves) {
            abb.definir(c, c * 10);
        }

        for (int c : claves) {
            assertEquals(c * 10, (int) abb.obtener(c));
        }

        // Pisar un valor en el medio del árbol no afecta a los demás
        abb.definir(40, 999);
        assertEquals(999, (int) abb.obtener(40));
        assertEquals(350, (int) abb.obtener(35));
        assertEquals(600, (int) abb.obtener(60));
    }
}