package aed.tp2;

public class Relacion implements Comparable<Relacion> {
    private final int idMenor;
    private final int idMayor;

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

    public int otro(int id) {
        //requiere: id == idMayor || id == idMenor
        int id_otro;
        if(idMayor == id) {
            id_otro = idMenor;
        } else {
            id_otro = idMayor;
        }
        return id_otro;
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
