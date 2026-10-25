package aed.tp2;

public class DegubMain {

    /**
     * Este modulo es para usar para debuggear rápidamente sin la neceisdad de tener que estar viendo los errores en los Tests
     * <p>
     * Recomiendo aunque sea tener algunos methodos básicos completos 
     */

    public static void main(String[] args) {
        AirMiles am = new AirMiles();
        
        am.registrarUsuario(1);
        am.registrarUsuarioConReferidor(2, 1);
        // Vemos que pasa con el tema de registrador usuarios y vamos a ver los saldos

        double res = am.consultarSaldo(2);
        double resRef = am.consultarSaldo(1);


    

        System.out.println(res); //Debería devolver 500
        System.out.println(resRef);

    }
}
