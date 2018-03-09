package matrixmultiplication;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaSparseArray {
    private int[][] valueA;
    private int[][] indexA;
    private int len;
    private int nnz;

    public JavaSparseArray(int[][] valueA, int[][] indexA, int nnz){
        this.valueA = valueA;
        this.indexA = indexA;
        this.len = valueA.length;
        this.nnz = nnz;
    }

    public int[][] getValueA(){
        return valueA;
    }

    public int[][] getIndexA(){
        return indexA;
    }

    public int getLen(){return len;}

}
