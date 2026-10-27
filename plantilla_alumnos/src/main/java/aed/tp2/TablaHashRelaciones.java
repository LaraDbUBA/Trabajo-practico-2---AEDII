package aed.tp2;

public class TablaHashRelaciones {
    private class Nodo {
        Relacion relacion;
        Nodo siguiente;

        Nodo(Relacion relacion, Nodo siguiente) {
            this.relacion = relacion;
            this.siguiente = siguiente;
        }
    }

    private Nodo[] tabla;
    private int cantidad;

    public TablaHashRelaciones() {
        tabla = new Nodo[16];
        cantidad = 0;
    }

    private int indice(int id1, int id2) {
        int menor = Math.min(id1, id2);
        int mayor = Math.max(id1, id2);

        long valor = 31L * menor + mayor;
        return (int) (valor % tabla.length);
    }

    public Relacion obtener(int id1, int id2) {
        int pos = indice(id1, id2);
        Nodo actual = tabla[pos];

        while (actual != null) {
            if (actual.relacion.conecta(id1, id2)) {
                return actual.relacion;
            }
            actual = actual.siguiente;
        }

        return null;
    }

    public void insertar(Relacion relacion) {
        if (cantidad + 1 > tabla.length * 0.75) {
            redimensionar();
        }

        int pos = indice(
            relacion.obtenerIdMenor(),
            relacion.obtenerIdMayor()
        );

        tabla[pos] = new Nodo(relacion, tabla[pos]);
        cantidad++;
    }

    private void redimensionar() {
        Nodo[] anterior = tabla;
        tabla = new Nodo[anterior.length * 2];
        cantidad = 0;

        for (int i = 0; i < anterior.length; i++) {
            Nodo actual = anterior[i];

            while (actual != null) {
                insertarSinRedimensionar(actual.relacion);
                actual = actual.siguiente;
            }
        }
    }

    private void insertarSinRedimensionar(Relacion relacion) {
        int pos = indice(
            relacion.obtenerIdMenor(),
            relacion.obtenerIdMayor()
        );

        tabla[pos] = new Nodo(relacion, tabla[pos]);
        cantidad++;
    }

}
