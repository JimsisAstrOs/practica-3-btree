import implementacion.ArbolB;
public class PruebasArbolB{
    public static void main(String[] args){

        System.out.println("****************************************");
        System.out.println("PRUEBAS DEL ÁRBOL B");
        System.out.println("****************************************");

        // PRUEBA 1: ÁRBOL VACÍO

        System.out.println("\nPRUEBA 1: árbol vacío");
        ArbolB arbol = new ArbolB();
        boolean resultado = arbol.buscar(10);
        boolean esperado = false;

        comprobar("buscar(10) -> NOT_FOUND", resultado, esperado);

        // PRUEBA 2: INSERCIÓN SIN SPLIT

        System.out.println("\nPRUEBA 2: inserción sin split");
        arbol = new ArbolB();
        arbol.insertar(20);
        arbol.insertar(40);
        arbol.insertar(10);
        System.out.println("Esperado:");
        System.out.println("[10 | 20 | 40]");
        System.out.println("Obtenido:");
        arbol.imprimirPorNiveles();
        // Falta ver si es correcta o no la impresión.

        // PRUEBA 3: PRIMER SPLIT DE LA RAÍZ

        System.out.println("\nPRUEBA 3: primer split de la raíz");
        arbol.insertar(30);
        System.out.println("Esperado:");
        System.out.println("[30]");
        System.out.println("[10 | 20] [40]");
        System.out.println("Obtenido:");
        arbol.imprimirPorNiveles();
        // Falta ver si es correcta o no la impresión.

        // PRUEBA 4: BÚSQUEDA

        System.out.println("\nPRUEBA 4: búsqueda");
        comprobar("buscar(20) -> FOUND",arbol.buscar(20),true);
        comprobar("buscar(30) -> FOUND",arbol.buscar(30),true);
        comprobar("buscar(99) -> NOT_FOUND",arbol.buscar(99),false);

        // PRUEBA 5: DIVISIÓN Y PROPAGACIÓN

        arbol = new ArbolB();
        System.out.println("\nPRUEBA 5: división y propagación");
        int[] valores = {20, 40, 10, 30, 50, 60, 70, 5, 15, 25, 35, 45};

        for (int x : valores) {
            arbol.insertar(x);
        }

        System.out.println("Esperado:");
        System.out.println("[45]");
        System.out.println("[15 | 30] [60]");
        System.out.println("[5 | 10] [20 | 25] [35 | 40] [50] [70]");
        System.out.println("\nObtenido:");
        arbol.imprimirPorNiveles();
        // Falta ver si es correcta o no la impresión.

        // PRUEBA 6: NO DUPLICADOS

        System.out.println("\nPRUEBA 6: inserción de duplicado");
        arbol.insertar(35);
        arbol.imprimirPorNiveles();
        // Falta ver si es correcta o no la impresión.
        comprobar("buscar(35) después de duplicado", arbol.buscar(35),true);

        // PRUEBA 7: ELIMINAR HOJA SIN UNDERFLOW

        System.out.println("\nPRUEBA 7: eliminar hoja sin underflow");
        arbol.eliminar(25);
        arbol.imprimirPorNiveles();
        // Falta ver si es correcta o no la impresión.
        comprobar("buscar(25) después de eliminar", arbol.buscar(25),false);

        // PRUEBA 8: REPARACIÓN UNDERFLOW

        System.out.println("\nPRUEBA 8: reparación de underflow");
        arbol.eliminar(10);
        arbol.eliminar(70);
        arbol.imprimirPorNiveles();
        // Falta ver si es correcta o no la impresión.
        comprobar("buscar(10) después de eliminar", arbol.buscar(10), false);
        comprobar("buscar(70) después de eliminar", arbol.buscar(70), false);

        // PRUEBA 9: REDUCCIÓN ALTURA
        System.out.println("\nPRUEBA 9: reducción de altura");
        arbol.eliminar(5);
        arbol.imprimirPorNiveles();
        // Falta ver si es correcta o no la impresión.
        comprobar("buscar(5) después de eliminar", arbol.buscar(5), false);
    }

    private static void comprobar(String descripcion, boolean obtenido, boolean esperado) {
        if (obtenido == esperado) {
            System.out.println("[OK] " + descripcion);
        } else {
            System.out.println("[ERROR] " + descripcion);
            System.out.println("       Esperado: " + esperado);
            System.out.println("       Obtenido: " + obtenido);
        }
    }

}