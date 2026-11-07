package aed.tp2;

public class Relacion implements Comparable<Relacion> {
    private final int idMenor;
    private final int idMayor;

/*
Si el handle lo ponemos para usuario no hace falta que relacion
tenga su propio handle
*/

    public Relacion(int id1, int id2) {
        if (id1 < id2) {
            idMenor = id1;
            idMayor = id2;
        } else {
            idMenor = id2;
            idMayor = id1;
        }

    }

    public int obtenerIdMenor() {
        return idMenor;
    }

    public int obtenerIdMayor() {
        return idMayor;
    }
    @Override
    public int compareTo(Relacion otra) {
        int comparacion = Integer.compare(idMenor, otra.idMenor);

        if (comparacion != 0) {
            return comparacion;
        }

        return Integer.compare(idMayor, otra.idMayor);
    }
}
