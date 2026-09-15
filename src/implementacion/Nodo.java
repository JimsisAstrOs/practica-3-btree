

 public class Nodo {

    int[] claves;
    Nodo[] hijos;
    int numClaves;
    
    public Nodo() {
        claves = new int[4];
        hijos = new Nodo[5];
        numClaves = 0;
    }

    public boolean esHoja() {
        return hijos[0] == null;
    }
    
}