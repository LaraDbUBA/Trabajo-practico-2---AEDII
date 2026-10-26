package aed.tp2;

public class Relacion implements Comparable<Relacion> {
    private final int idMenor;
    private final int idMayor;

    private boolean menorAMayor;
    private boolean mayorAMenor;

    Handle<Relacion> handleExclusivo;

    public Relacion(int id1, int id2) {
        if (id1 < id2) {
            idMenor = id1;
            idMayor = id2;
        } else {
            idMenor = id2;
            idMayor = id1;
        }

        menorAMayor = false;
        mayorAMenor = false;
        handleExclusivo = null;
    }

    public int obtenerIdMenor() {
        return idMenor;
    }

    public int obtenerIdMayor() {
        return idMayor;
    }

    public boolean conecta(int id1, int id2) {
        return idMenor == Math.min(id1, id2) && idMayor == Math.max(id1, id2);
    }

    public void registrarTransferencia(int origen, int destino) {
        if (origen == idMenor && destino == idMayor) {
            menorAMayor = true;
        } else if (origen == idMayor && destino == idMenor) {
            mayorAMenor = true;
        }
    }

    public boolean huboEnAmbasDirecciones() {
        return menorAMayor && mayorAMenor;
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
