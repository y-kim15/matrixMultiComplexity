package matrixmultiplication.CRSImplementation;


import java.util.Arrays;

import static java.lang.StrictMath.pow;

/**
 * CRS implementation
 */
public class CRS {
    private int[] values;
    private int[] cols;
    private int[] rpointer;
    private int nnz;
    private int n;

    public CRS(int n){
        int max = (int)pow(n,2);
        values = new int[max];
        cols = new int[max];
        rpointer = new int[n+1];
        this.n =n;
        this.nnz=0;
    }
    public CRS(int n,int nnz){
        values = new int[nnz];
        cols = new int[nnz];
        rpointer = new int[n+1];
        this.n =n;
        this.nnz = 0;
    }

    public int getNnz() {
        return nnz;
    }
    public int getN(){return n;}


    public int getValue(int i){return values[i];}
    public int getRowPtr(int i){return rpointer[i];}
    public int getColInd(int i){return cols[i];}
    public void setNnz(int nnz) {
        this.nnz = nnz;
    }

    public void setValue(int i, int v){values[i]=v;}
    public void setRowPtr(int i, int r){rpointer[i]=r;}
    public void setColInd(int i, int c){cols[i]=c;}


    public void trimCRS(){
        int[] newVal = new int[nnz];
        int[] newCol = new int[nnz];
        for(int i=0; i<nnz; i++){
            newVal[i] = values[i];
            newCol[i] = cols[i];
        }
        values = newVal;
        cols = newCol;
    }


}