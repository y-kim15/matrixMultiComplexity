package matrixmultiplication;

public class MatrixData {
    int[][] values;
    int nnz;

    public MatrixData (int[][] values, int nnz){
        this.values = values;
        this.nnz = nnz;
    }
}
