package matrixmultiplication.IntMatrixMultiplication;

/**
 * Basic Algorithm and improved basic algorithm implementations
 */
public class BasicMultiplier {

    public String toString() {
        return "BasicMultiplier";
    }

    public IntMatrix multiply(IntMatrix a, IntMatrix b) {
        int dim = a.getDim();
        IntMatrix result = new IntMatrix(dim);

        for(int i = 0; i < dim; i++) {
            for(int j = 0; j < dim; j++) {
                int sum = 0;
                for(int k = 0; k < dim; k++) {
                    sum += a.get(i,k) * b.get(k,j);
                }
                result.set(i,j,sum);
            }
        }

        return result;
    }

    /**
     * Method which checks if the entry is non zero and
     * skips operations if zero
     * @param a matrix
     * @param b matrix
     * @return matrix, a product of a and b
     */
    public IntMatrix improvedMultiply(IntMatrix a, IntMatrix b){
        //validity check
        int dim = a.getDim();
        IntMatrix C = new IntMatrix(dim);

        for(int i=0; i<dim; i++){
            for(int k=0; k<dim; k++){
                if(a.get(i,k)!=0){
                    for(int j=0; j<dim; j++){
                        int val = C.get(i,j);
                        val += a.get(i,k)*b.get(k,j);
                        C.set(i,j,val);
                    }
                }
            }
        }

        return C;

    }

}
