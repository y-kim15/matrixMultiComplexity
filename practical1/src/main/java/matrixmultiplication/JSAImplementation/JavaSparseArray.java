package matrixmultiplication.JSAImplementation;

/**
 * Implementation of Matrix as a Java Sparse Array
 */
public class JavaSparseArray {
    private int[][] valueA;
    private int[][] indexA;
    private int dim;
    private int nnz;

    public JavaSparseArray(int[][] valueA, int[][] indexA, int nnz){
        this.valueA = valueA;
        this.indexA = indexA;
        this.dim = valueA.length;
        this.nnz = nnz;
    }

    public int[][] getValueA(){
        return valueA;
    }

    public int[][] getIndexA(){
        return indexA;
    }

    public int getDim(){return dim;}

}
