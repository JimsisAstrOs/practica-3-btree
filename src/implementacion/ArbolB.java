package implementacion;
public class ArbolB {

    private Nodo raiz;

    public ArbolB() {
        raiz = null;
    }

    public boolean buscar(int x) {
        Nodo actual = raiz;

        while (actual != null) {
            int i = 0;

            while (i < actual.numClaves && x > actual.claves[i]) {
                i++;
            }

            if (i < actual.numClaves && x == actual.claves[i]) {
                return true;
            }

            if (actual.esHoja()) {
                return false;
            }

            actual = actual.hijos[i];
        }

        return false;
    }

    public void insertar (int x) {
        if (buscar(x)) {
            return;
        }

        if (raiz == null) {
            raiz = new Nodo();
            raiz.claves[0] = x;
            raiz.numClaves = 1;
            return;
        }

        insertarRecursivo(raiz, x);

        if (raiz.numClaves == 4) {
            dividirRaiz();
        }
    }

    private void insertarRecursivo(Nodo nodo, int x) {
        if (nodo.esHoja()) {
            insertarOrdenado(nodo, x);
            return;
        }
        int i = 0;
        while (i < nodo.numClaves && x > nodo.claves[i]) {
            i++;
        }
        insertarRecursivo(nodo.hijos[i], x);
        if (nodo.hijos[i].numClaves == 4) {
            dividirHijo(nodo, i);
        }
    }

    private void dividirHijo(Nodo padre, int posicion) {
        Nodo hijo = padre.hijos[posicion];
        Nodo derecho = new Nodo();

        int promovida = hijo.claves[2];

        derecho.claves[0] = hijo.claves[3];
        derecho.numClaves = 1;

        hijo.numClaves = 2;

        for (int i = padre.numClaves; i > posicion; i--) {
            padre.hijos[i +1] = padre.hijos[i];
        }

        padre.hijos[posicion + 1] = derecho;
        for (int i = padre.numClaves - 1; i >= posicion; i--) {
            padre.claves[i + 1] = padre.claves[i];
        }
        padre.claves[posicion] = promovida;
        padre.numClaves++;
    }

    private void dividirRaiz() {
        Nodo nuevaRaiz = new Nodo();
    Nodo izquierdo = new Nodo();
    Nodo derecho = new Nodo();

    nuevaRaiz.claves[0] = raiz.claves[2];
    nuevaRaiz.numClaves = 1;

    izquierdo.claves[0] = raiz.claves[0];
    izquierdo.claves[1] = raiz.claves[1];
    izquierdo.numClaves = 2;

    derecho.claves[0] = raiz.claves[3];
    derecho.numClaves = 1;

    if (!raiz.esHoja()) {
        izquierdo.hijos[0] = raiz.hijos[0];
        izquierdo.hijos[1] = raiz.hijos[1];
        izquierdo.hijos[2] = raiz.hijos[2];

        derecho.hijos[0] = raiz.hijos[3];
        derecho.hijos[1] = raiz.hijos[4];
    }

    nuevaRaiz.hijos[0] = izquierdo;
    nuevaRaiz.hijos[1] = derecho;

    raiz = nuevaRaiz;  
    }

    private void insertarOrdenado(Nodo nodo, int x) {
        int i = nodo.numClaves - 1;

        while (i >= 0 && x < nodo.claves[i]) {
            nodo.claves[i + 1] = nodo.claves[i];
            i--;
        }

        nodo.claves[i + 1] = x;
        nodo.numClaves++;
    }

    public void imprimirPorNiveles() {
        if (raiz == null) {
            return;
        }

        Nodo[] cola = new Nodo[100];
        int inicio = 0;
        int fin = 0;

        cola[fin++] = raiz;

        while (inicio < fin) {
        int cantidad = fin - inicio;

        for (int i = 0; i < cantidad; i++) {
            Nodo actual = cola[inicio++];

            System.out.print("[");
            for (int j = 0; j < actual.numClaves; j++) {
                if (j > 0) {
                    System.out.print(" | ");
                }
                System.out.print(actual.claves[j]);
            }
            System.out.print("] ");
            for (int j = 0; j <= actual.numClaves; j++) {
                if (actual.hijos[j] != null) {
                    cola[fin++] = actual.hijos[j];
                }
            }

        } 

        System.out.println();
        }
    }

    public void eliminar(int x) {
        if (raiz == null) {
            return;
        }

        eliminarRecursivo(raiz, x);

        if (raiz != null && raiz.numClaves == 0) {
            if (raiz.hijos[0] != null) {
                raiz = raiz.hijos[0];
            } else{
                raiz = null;
            }
        }
    }

    private int obtenerMaximo(Nodo nodo) {
        while (!nodo.esHoja()) {
            nodo = nodo.hijos[nodo.numClaves];
        }
        return nodo.claves[nodo.numClaves - 1];
    }

    public void eliminarRecursivo(Nodo nodo, int x) {
        int i = 0;

        while (i < nodo.numClaves && x > nodo.claves[i]) {
            i++;
        }

        if (i < nodo.numClaves && x == nodo.claves[i]) {
            if (nodo.esHoja()) {
                for (int j = i; j < nodo.numClaves - 1; j++) {
                    nodo.claves[j] = nodo.claves[j + 1];
                }
                nodo.numClaves--;
            }
            else {
                Nodo izquierdo = nodo.hijos[i];
                int reemplazo = obtenerMaximo(izquierdo);
                nodo.claves[i] = reemplazo;
                eliminarRecursivo(izquierdo, reemplazo);
                repararUnderflow(nodo, i);
            }
            return;
        } 
        
        if (!nodo.esHoja()) {
            eliminarRecursivo(nodo.hijos[i], x);
            repararUnderflow(nodo, i);
        }
    }

    private void repararUnderflow(Nodo padre, int posicion) {
        Nodo hijo = padre.hijos[posicion];
        if(hijo.numClaves > 0) {
            return;
        }
        if (posicion > 0 && padre.hijos[posicion -1].numClaves > 1) {
            redistribuirIzquierda(padre, posicion);
            return;
        }
        if (posicion < padre.numClaves && padre.hijos[posicion + 1].numClaves > 1) {
            redistribuirDerecha(padre, posicion);
            return;
        }
        if (posicion > 0) {
            fusionar(padre, posicion - 1);
        } else {
            fusionar(padre, posicion);
        }
    }

    private void fusionar(Nodo padre, int posicion) {

        Nodo izquierdo = padre.hijos[posicion];
        Nodo derecho = padre.hijos[posicion + 1];

        izquierdo.claves[izquierdo.numClaves] = padre.claves[posicion];
        izquierdo.numClaves++;
        
        for (int i = 0; i < derecho.numClaves; i++) {
            izquierdo.claves[izquierdo.numClaves] = derecho.claves[i];
            izquierdo.numClaves++;
            }
        if (!derecho.esHoja()) {
            int inicio = izquierdo.numClaves - derecho.numClaves;

            for (int i = 0; i <= derecho.numClaves; i++) {
                izquierdo.hijos[inicio + i] = derecho.hijos[i];
            }
        }
        for (int i = posicion; i < padre.numClaves - 1; i++) {
            padre.claves[i] = padre.claves[i + 1];
        }
        for (int i = posicion + 1; i < padre.numClaves; i++) {
            padre.hijos[i] = padre.hijos[i + 1];
        }

        padre.numClaves--;
    }

    private void redistribuirIzquierda(Nodo padre, int posicion) {
        Nodo hijo = padre.hijos[posicion];
        Nodo izquierdo = padre.hijos[posicion - 1];

        hijo.claves[0] = padre.claves[posicion - 1];
        hijo.numClaves++;

        padre.claves[posicion - 1] = izquierdo.claves[izquierdo.numClaves - 1];
        if (!izquierdo.esHoja()) {
            for (int i = hijo.numClaves; i > 0; i--) {
                hijo.hijos[i] = hijo.hijos[i - 1];
            }

            hijo.hijos[0] = izquierdo.hijos[izquierdo.numClaves];
            izquierdo.hijos[izquierdo.numClaves] = null;
        }
        izquierdo.numClaves--;
    }

    private void redistribuirDerecha(Nodo padre, int posicion) {
        Nodo hijo = padre.hijos[posicion];
        Nodo derecho = padre.hijos[posicion + 1];

        hijo.claves[hijo.numClaves] = padre.claves[posicion];
        hijo.numClaves++;


        padre.claves[posicion] = derecho.claves[0];
        for (int i = 0; i < derecho.numClaves - 1; i++) {
            derecho.claves[i] = derecho.claves[i + 1];
        }
        derecho.numClaves--;
    }

    

    


    


}

