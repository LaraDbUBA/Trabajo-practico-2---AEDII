# Notas
## Estructuras elegidas:
- AVL : usuarios ordenados por Id
- maxHeap : usuarios ordenador por millasDisponibles 
- tries : promociones

## Justificaciones
maxHeap: Cada extracción cuesta \(O(\log U)\), por lo que obtener los \(N\) primeros cuesta \(O(N\log U)\), que coincide con la complejidad pedida. 
AVL : encontrar un elemento cuesta \(O(\log U)\)
tries : 

## Elecciones de atributos
- avl usuarios 
- usuario maxacumulador
- usuario maxcanjeador
- tries promos
- maxHeap usuarios2

---
## Preguntas
1. en registrar usuario con referidor se sumaban las millas totales al referidor? 
2. en el caso que consulte maximo acumulador cuando todavia no hay ningun usuario, es correcto entregar -1 o se puede entregar un error? yo podria asumir en el contrato que hay un requiere de al menos un usuario
3. se puede importar el ArrayList?
4. 
