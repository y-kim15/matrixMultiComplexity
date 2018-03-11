package matrixmultiplication;

/**
 * Internal Custom Class for collecting generated matrix
 * values with number of non zero entries it contains.
 * Used to be converted to other matrix implementations
 */
public class MatrixData {
    int[][] values;
    int nnz;

    public MatrixData (int[][] values, int nnz){
        this.values = values;
        this.nnz = nnz;
    }
}
