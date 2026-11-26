package aed.tp2;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTests {
    private Usuario u;

    @Test
    void rompeExclusividadLosCuatroCasos() {
        
        u = new Usuario(1);

        // Nunca transfirió: pasa a ser exclusivo con 2, no rompe nada
        assertFalse(u.rompeExclusividad(2));
        assertTrue(u.esExclusivoCon(2));

        // Exclusivo con 2 y vuelve a transferir con 2: sigue igual
        assertFalse(u.rompeExclusividad(2));
        assertTrue(u.esExclusivoCon(2));

        // Exclusivo con 2 y transfiere con 3: pierde la exclusividad
        assertTrue(u.rompeExclusividad(3));
        assertFalse(u.esExclusivoCon(2));
        assertFalse(u.esExclusivoCon(3));

        // Ya perdió la exclusividad: no vuelve a romper nada
        assertFalse(u.rompeExclusividad(4));
        assertFalse(u.esExclusivoCon(4));
    }
}