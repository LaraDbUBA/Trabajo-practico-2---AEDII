package aed.tp2;

public class Promocion {
    private final double factor;
    private double cupo;

    private static final double SIN_TOPE = -1.0;

    Promocion(double factor) {
        //requiere: factor >= 1.0
        this.factor = factor;
        this.cupo = SIN_TOPE;
    }

    Promocion(double factor, double tope) {
        //requiere: tope > 0 and factor >= 1.0
        this.factor = factor;
        this.cupo = tope;
    }

    public double factor() {
        return this.factor;
    }
    
    public double cupo() {
        //requiere: this.tieneTope()
        return this.cupo;
    }

    public boolean tieneTope() {
        return this.cupo >= 0;
    }

    public void restarCupo(double millas) {
        //requiere: this.tieneTope() and millas <= this.cupo()
        this.cupo -= millas;
    }


}
