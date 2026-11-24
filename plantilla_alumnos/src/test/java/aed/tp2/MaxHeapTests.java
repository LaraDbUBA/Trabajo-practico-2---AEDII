package aed.tp2;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MaxHeapTests {

    // Probamos el heap con usuarios, como en AirMiles (Usuario es Comparable por
    // millas disponibles)
    private MaxHeap<Usuario> heap;

    @BeforeEach
    void setUp() {
        heap = new MaxHeap<>();
    }

    // Crea un usuario con cierta cantidad de millas disponibles
    private Usuario usuario(int id, double millas) {
        Usuario u = new Usuario(id);
        u.acumular(millas);
        return u;
    }

    // Devuelve las millas disponibles de cada usuario de la lista, en orden
    private double[] millas(ArrayList<Usuario> usuarios) {
        double[] res = new double[usuarios.size()];
        for (int i = 0; i < usuarios.size(); i++) {
            res[i] = usuarios.get(i).disponibles();
        }
        return res;
    }

    @Test
    void primerosDevuelveEnOrdenDecreciente() {
        // Millas desordenadas y con un repetido
        heap.insertar(usuario(1, 5));
        heap.insertar(usuario(2, 7));
        heap.insertar(usuario(3, 9));
        heap.insertar(usuario(4, 3));
        heap.insertar(usuario(5, 7));
        assertEquals(5, heap.tamanio());

        // primeros devuelve de mayor a menor
        assertArrayEquals(new double[] { 9, 7, 7, 5, 3 }, millas(heap.primeros(5)));

        // y no saca a nadie: el heap sigue con los 5 y da lo mismo si se vuelve a
        // consultar
        assertEquals(5, heap.tamanio());
        assertArrayEquals(new double[] { 9, 7, 7, 5, 3 }, millas(heap.primeros(5)));
        assertArrayEquals(new double[] { 9, 7 }, millas(heap.primeros(2)));
    }

    @Test
    void handlesSiguenValidosDespuesDePrimeros() {
        Usuario a = usuario(1, 5);
        Usuario b = usuario(2, 9);
        Usuario c = usuario(3, 3);
        Usuario d = usuario(4, 7);
        Handle<Usuario> ha = heap.insertar(a);
        Handle<Usuario> hb = heap.insertar(b);
        heap.insertar(c);
        heap.insertar(d);

        // primeros saca y vuelve a meter a b (id 2) y d (id 4)
        ArrayList<Usuario> top = heap.primeros(2);
        assertEquals(2, top.get(0).id());
        assertEquals(4, top.get(1).id());

        // los handles viejos siguen sirviendo (si primeros creara nodos nuevos, esto se
        // rompería)
        assertEquals(2, hb.valor().id());
        a.acumular(10); // a pasa a tener 15
        ha.modificar(a);
        assertEquals(1, heap.primeros(1).get(0).id());
        assertArrayEquals(new double[] { 15, 9, 7, 3 }, millas(heap.primeros(4)));
    }

    @Test
    void modificarReubicaAlCambiarLasMillas() {
        Usuario a = usuario(1, 10);
        Usuario b = usuario(2, 20);
        Usuario c = usuario(3, 30);
        Usuario d = usuario(4, 40);
        Handle<Usuario> ha = heap.insertar(a);
        heap.insertar(b);
        heap.insertar(c);
        heap.insertar(d);

        // a gana millas: sube hasta ser el máximo
        a.acumular(40); // 50
        ha.modificar(a);
        assertEquals(1, heap.primeros(1).get(0).id());

        // a canjea todo: baja y d vuelve a ser el máximo
        a.canjear(50); // 0
        ha.modificar(a);
        assertEquals(4, heap.primeros(1).get(0).id());

        // se mantiene el orden
        assertArrayEquals(new double[] { 40, 30, 20, 0 }, millas(heap.primeros(4)));
    }

    @Test
    void heapConUnSoloElemento() {
        Usuario a = usuario(1, 1);
        Handle<Usuario> ha = heap.insertar(a);
        assertEquals(1, heap.tamanio());
        assertEquals(1, heap.primeros(1).get(0).id());
        assertEquals(1, heap.tamanio());

        // eliminar el único elemento deja el heap vacío
        ha.eliminar();
        assertEquals(0, heap.tamanio());
        assertEquals(0, heap.primeros(0).size());
    }
}