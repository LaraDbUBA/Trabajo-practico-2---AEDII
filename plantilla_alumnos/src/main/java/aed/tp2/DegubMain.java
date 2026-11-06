package aed.tp2;

public class DegubMain {

    /**
     * Este modulo es para usar para debuggear rápidamente sin la neceisdad de tener que estar viendo los errores en los Tests
     * <p>
     * Recomiendo aunque sea tener algunos methodos básicos completos 
     */

    

    public static void main(String[] args) {
        AirMiles am = new AirMiles();
        

        /**
         * Los siguientes 5 van a ganar millas pero a base de referir a otras personas
         */
        am.registrarUsuario(1);
        am.registrarUsuario(2);
        am.registrarUsuario(3);
        am.registrarUsuario(4);
        am.registrarUsuario(5);
        
        /* Personas referidas */

        am.registrarUsuarioConReferidor(6, 1);
        am.registrarUsuarioConReferidor(7, 2);
        am.registrarUsuarioConReferidor(8, 3);
        am.registrarUsuarioConReferidor(9, 4);
        am.registrarUsuarioConReferidor(10, 5);
        
        // Ahora probamos los casos de las transferencias. 

        am.transferirMillas(1, 6, 100); // 1 -> 6, 100 millas -> 400 | 600
        System.out.println(am.consultarSaldo(1));
        System.out.println(am.consultarSaldo(6));
       // am.verificarRecibioDe(1, 6);
        //am.verificarMandoA(1, 6);

        System.err.println();
    }
}