package matrixmultiplication.JSAImplementation;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
