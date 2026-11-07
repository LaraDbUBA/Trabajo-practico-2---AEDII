package aed.tp2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class MaxHeapTests {

    // Elemento de prueba: una prioridad y su handle (posición en el heap)
    private static class Elem {
        int valor;
        int pos = -1;
        Elem(int valor) { this.valor = valor; }
    }

    private MaxHeap<Elem> heap;

    @BeforeEach
    void setUp() {
        heap = new MaxHeap<>(new MaxHeap.Operaciones<Elem>() {
            public int comparar(Elem a, Elem b) { return Integer.compare(a.valor, b.valor); }
            public int obtenerPosicion(Elem e) { return e.pos; }
            public void actualizarPosicion(Elem e, int posicion) { e.pos = posicion; }
        });
    }

        @Test
    void extraerMaximoDevuelveEnOrdenDecreciente() {
        // Prioridades desordenadas y con un repetido
        heap.insertar(new Elem(5));
        heap.insertar(new Elem(7));
        heap.insertar(new Elem(9));
        heap.insertar(new Elem(3));
        heap.insertar(new Elem(7));
        assertEquals(5, heap.tamanio());

        // Extraer el máximo repetidamente devuelve las prioridades de mayor a menor
        assertEquals(9, heap.extraerMaximo().valor);
        assertEquals(7, heap.extraerMaximo().valor);
        assertEquals(7, heap.extraerMaximo().valor);
        assertEquals(5, heap.extraerMaximo().valor);
        assertEquals(3, heap.extraerMaximo().valor);
        assertTrue(heap.estaVacio());
    }

    @Test
    void handlesSeActualizanAlInsertarYExtraer() {
        Elem a = new Elem(5);
        Elem b = new Elem(9);
        Elem c = new Elem(3);
        Elem d = new Elem(7);
        heap.insertar(a);
        heap.insertar(b);
        heap.insertar(c);
        heap.insertar(d);

        // El máximo está en la raíz (posición 0)
        assertEquals(0, b.pos);

        // Al extraerlo, su handle queda invalidado y el nuevo máximo pasa a la raíz
        heap.extraerMaximo();
        assertEquals(-1, b.pos);
        assertEquals(0, d.pos);

        // Un elemento extraído se puede volver a insertar
        heap.insertar(b);
        assertEquals(0, b.pos);
        assertEquals(b, heap.verMaximo());
    }

    @Test
    void actualizarReubicaAlCambiarLaPrioridad() {
        Elem a = new Elem(10);
        Elem b = new Elem(20);
        Elem c = new Elem(30);
        Elem d = new Elem(40);
        heap.insertar(a);
        heap.insertar(b);
        heap.insertar(c);
        heap.insertar(d);

        // Aumenta la prioridad de a: sube hasta la raíz
        a.valor = 50;
        heap.actualizar(a);
        assertEquals(a, heap.verMaximo());
        assertEquals(0, a.pos);

        // Disminuye la prioridad de a: baja y d vuelve a ser el máximo
        a.valor = 0;
        heap.actualizar(a);
        assertEquals(d, heap.verMaximo());

        // Se mantiene el invariante de orden
        assertEquals(40, heap.extraerMaximo().valor);
        assertEquals(30, heap.extraerMaximo().valor);
        assertEquals(20, heap.extraerMaximo().valor);
        assertEquals(0, heap.extraerMaximo().valor);
    }

    @Test
    void heapConUnSoloElemento() {
        Elem a = new Elem(1);
        heap.insertar(a);
        assertEquals(a, heap.verMaximo());
        assertEquals(0, a.pos);

        // Extraer el único elemento deja el heap vacío
        assertEquals(a, heap.extraerMaximo());
        assertEquals(-1, a.pos);
        assertTrue(heap.estaVacio());

    }
}