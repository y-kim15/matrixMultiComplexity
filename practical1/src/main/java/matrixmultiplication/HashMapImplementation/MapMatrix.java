package matrixmultiplication.HashMapImplementation;

import java.util.HashMap;

/**
 * Implementation of Matrix using HashMap
 */
public class MapMatrix {
    private HashMap<Pair, Integer> matrix;
    private int nnz;
    private int dim;

    public MapMatrix(int nnz, int dim){
        matrix = new HashMap<>();
        this.nnz = nnz;
        this.dim = dim;
    }

    public HashMap<Pair, Integer> getMatrix() {
        return matrix;
    }

    public void put(int x, int y, int val){
        matrix.put(new Pair(x,y), val);
    }

    public int getNnz(){ return nnz;}

    public int getDim(){return dim;}
}
