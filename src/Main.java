public class Main {

    public static void main(String[] args) {

        ArbolB arbol = new ArbolB();

        int[] valores = {20, 40, 10, 30, 50, 60, 70, 5, 15, 25, 35, 45};

        for (int x : valores) {
            arbol.insertar(x);
        }

        System.out.println("Árbol después de insertar:");
        arbol.imprimirPorNiveles();

        arbol.insertar(35);
        System.out.println("Después de insertar 35 otra vez:");
        arbol.imprimirPorNiveles();

        System.out.println(arbol.buscar(35) ? "35 FOUND" : "35 NOT_FOUND");
        System.out.println(arbol.buscar(99) ? "99 FOUND" : "99 NOT_FOUND");

        arbol.eliminar(25);
        arbol.eliminar(10);
        arbol.eliminar(70);

        System.out.println("Después de eliminar 25, 10 y 70:");
        arbol.imprimirPorNiveles();

        System.out.println(arbol.buscar(25) ? "25 FOUND" : "25 NOT_FOUND");
        System.out.println(arbol.buscar(35) ? "35 FOUND" : "35 NOT_FOUND");

        arbol.eliminar(5);

        System.out.println("Después de eliminar 5:");
        arbol.imprimirPorNiveles();
    }
}