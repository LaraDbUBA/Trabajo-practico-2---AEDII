package aed.tp2;


public class Usuario implements Comparable<Usuario> {
    private final int id;
    private double millasDisponibles; 
    private double millasTotales; 
    private double millasCanjeadas; 
    private double millasTransferidas; 

    private static final int PERDIO_EXCLUSIVIDAD = -1;
    private static final int NADIE_AUN = -2;

    // Guarda el estado de exclusividad del usuario: el id del único usuario con quien
    // transfirió (envió o recibió), NADIE_AUN (0 transferencias) o PERDIO_EXCLUSIVIDAD
    // (transfirió o recibió de más de un usuario)
    private int esExclusivoCon;
    
    private Handle<Relacion> handlePar;
    
    Usuario(int id){
        this.id = id;
        this.millasDisponibles = 0;
        this.millasTotales = 0;
        this.millasCanjeadas = 0;
        this.millasTransferidas = 0;
        this.esExclusivoCon = NADIE_AUN;
        this.handlePar = null;
        }
    
    public int id() {
        return this.id;
    }

    public double disponibles() {
        return this.millasDisponibles;
    }

    public double totales() {
        return this.millasTotales;
    }

    public String categoria() {
        //
        String res;
        if (this.millasTotales < 10000) {
            res = "Bronce";
        } else if (this.millasTotales < 50000) {
            res = "Plata";
        } else if (this.millasTotales < 1000000) {
            res = "Oro";
        } else {
            res = "Platino";
        }
        return res;
    }
    
    public double factorCategoria() {
        double res;
        String categoria = this.categoria();
        
        if (categoria.equals("Bronce")) {
            res = 1.0;
        } else if (categoria.equals("Plata")) {
            res = 1.5;
        } else if (categoria.equals("Oro")) {
            res = 2.0;
        } else { //Platino
            res = 0.0;
        }
        return res;
    }
    
    public void acumular(double millas) {
        // requiere: 0<= millas
        this.millasDisponibles += millas;
        this.millasTotales += millas;
    }
    
    public void canjear(double millas) {
        // requiere: 0 < millas ≤ this.disponibles()
        this.millasCanjeadas += millas;
        this.millasDisponibles -= millas;
    }
    
    public void transferir(double millas) {
        // requiere: 0 < millas ≤ this.disponibles()
        this.millasDisponibles -= millas;
        this.millasTransferidas += millas;
    }

    public void recibir(double millas) {
        this.millasDisponibles += millas;
    }
    
    public double perdidas() {
        return this.millasCanjeadas + this.millasTransferidas;
    }

    public boolean esExclusivoCon(int id_otro) {
        // NADIE_AUN y PERDIO_EXCLUSIVIDAD son negativos y los ids son positivos, así que da false
        return this.esExclusivoCon == id_otro;
    }
    
    public boolean rompeExclusividad(int id_otro) {
        boolean res;

        if(this.esExclusivoCon == NADIE_AUN) {
            this.esExclusivoCon = id_otro;
            res = false;
        } else if(this.esExclusivoCon == PERDIO_EXCLUSIVIDAD) {
            res = false;
        } else if(this.esExclusivoCon == id_otro) {
            res = false;
        } else { // Es exclusivo con otro usuario queda true
            this.esExclusivoCon = PERDIO_EXCLUSIVIDAD;
            res = true;
        }
        return res;
    }

    public void guardarHandlePar(Handle<Relacion> h) {
        if(this.handlePar == null) {
            this.handlePar = h;
        }
    }
    
    public void tiraHandlePar() {
        this.handlePar = null;
    }

    public Handle<Relacion> obtenerHandlePar() {
        return this.handlePar;
    }
    
    public boolean tieneHandlePar() {
        return this.handlePar != null;
    }
    
    @Override
    public int compareTo(Usuario otro) {
        return Double.compare(this.disponibles(), otro.disponibles());
    }
}

