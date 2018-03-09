package matrixmultiplication;

import Jama.Matrix;
import org.apache.commons.lang3.ArrayUtils;
import org.omg.PortableInterceptor.INACTIVE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.pow;
import static java.lang.Math.round;

public class Utils {
    private static final int MAX = 100;
    private static final int MIN = 20;
    private static final int COUNTS = 5;
    private static final int RANGE=10;

    private static class Pair{
        int[][] values;
        int nnz;
        public Pair(int[][] values, int nnz){
            this.values = values;
            this.nnz = nnz;
        }

    }
    /**
     * Generates 2d array implementation of a sparse matrix
     * This is a naive approach of storing all the values including
     * zeros. This is not a written as an optimised implementation deliberately.
     * ** note this may generate matrix with some rows with all zeros
     * @param n length of square matrix
     * @return 2d int array representing a sparse matrix
     */
    public static Pair getSparseMatrix(int n){
        int[][] matrix = new int[n][n];
        int total = (int)pow(n,2);
        int min = round(total/2);
        Random random = new Random();

        // generates number of zeros there will be
        int nZeros = random.nextInt(total-min+1) + min;
        // generates random sequence of indices to place values
        List<Integer> range = IntStream.rangeClosed(0, n*n-1)
                .boxed().collect(Collectors.toList());
        Collections.shuffle(range);
        // non zeros values is capped to be between 1 and RANGE-1
        for(int i=0; i<n*n; i++){
            int val = range.get(i);
            int first=(val/n); int second=(val%n);
            if(i<nZeros) matrix[first][second]=0;
            else matrix[first][second]=random.nextInt(RANGE-1)+1;
        }

        return new Pair(matrix, total-nZeros);//matrix;
    }

    /**
     * Generates a sparse matrix with at least non zero element per row
     * @param n
     * @return
     */
    public static int[][] getSparseMatrix1(int n){
        return null;
    }

    public static int[][] convertListToArray(List<List<Integer>> lists){
        int[][] array = new int[lists.size()][];
        int[] blankArray=new int[0];
        for(int i=0; i < lists.size(); i++) {
            blankArray = ArrayUtils.toPrimitive((Integer[])lists.get(i).toArray());//lists.get(i).toArray(blankArray);
            array[i] = blankArray;
            //array[i] = lists.get(i).toArray(blankArray);
        }
        return array;
    }

    public static double[][] convertIntToDoubleArray(int[][] values){
        int n = values.length;
        double[][] converted = new double[n][n];
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                converted[i][j] = (double)values[i][j];
            }
        }
        return converted;
    }

    public static JavaSparseArray convertToJSA(int[][] matrix){
        int[][] values = new int[matrix.length][];
        int[][] index = new int[matrix.length][];
        int nnz=0;
        for(int i =0; i<matrix.length; i++){
            List<Integer> rowi = new ArrayList<>();
            List<Integer> rowv = new ArrayList<>();
            for(int j=0; j<matrix.length; j++){
                if(matrix[i][j]>0){
                    nnz++;
                    rowv.add(matrix[i][j]);
                    rowi.add(j);
                }
            }
            int[] rowi1 = rowi.stream().mapToInt(Integer:: intValue).toArray();
            int[] rowv1 = rowv.stream().mapToInt(Integer:: intValue).toArray();
            index[i] = rowi1;
            values[i] = rowv1;

        }
        return new JavaSparseArray(values, index,nnz);
    }

    public static IntMatrix converToIntMarix(JavaSparseArray a){
        int n = a.getLen();
        IntMatrix b = new IntMatrix(n);
        int[][] v = a.getValueA();
        int[][] i = a.getIndexA();
        for(int j=0; j<n; j++){
            int[] ind = i[j];
            int[] val = v[j];
            int k=0, pos =k;
            for(k=0; k<n; k++){
                if(ind.length>0 && k==ind[pos]){
                    b.set(j,k,val[pos]);
                    pos++;
                    if(pos==ind.length) pos--;
                }
                else b.set(j,k,0);

            }
        }
        return b;
    }

    public static CRS convertToCRS(int[][] values, int nnz){
        int n = values.length;
        CRS c = new CRS(n,nnz);
        c.setRowPtr(0,0);
        int curNnz=0;
        for(int i=0; i<n; i++){
            int[] row = values[i];
            int rowP=0;
            for(int j=0; j<n; j++){
                if(row[j]>0){
                    c.setValue(curNnz,row[j]);
                    c.setColInd(curNnz,j);
                    curNnz++;
                    rowP++;
                }
            }
            c.setRowPtr(i+1,c.getRowPtr(i)+rowP);
        }
        return c;
    }

    /**
     * designed to be used in parameterised tests
     * @param structure
     * @return
     */
    // true for intmatrix and false for javasparsearray
    public static List<Object[]> parametersForTestMatrixMultiplication(boolean structure){
        List<Object[]> list = new ArrayList<>(); //number of runs will be NMATS/2
        //how many different lengths we will have and of what length
        for(int i=MIN; i<=MAX; i+=10){//+){
            // how many matrices of equal length we will have
            for(int o=0; o<COUNTS; o++){
                Object[] ob=new Object[3];
                // generates two matrix for each parameterised test
                for(int j=0;j<2;j++){
                    Pair p = Utils.getSparseMatrix(i);
                    int[][] matrix= p.values;//Utils.getSparseMatrix(i);
                    if(structure){
                        IntMatrix inM = new IntMatrix(matrix);
                        ob[j]=inM;
                    }
                    else{
                        JavaSparseArray jsa = convertToJSA(matrix);
                        ob[j] = jsa;
                    }
                }
                ob[2]=i;
                list.add(ob);//objects[i/2-1]=ob;
            }
        }
        return list;
    }

    public static List<Object[]> getParams(String type, int min, int max, int nEach, int step){
        List<Object[]> list = new ArrayList<>();
        for(int i=min; i<=max; i+=step){
            // how many matrices of equal length we will have
            for(int o=0; o<nEach; o++){
                Object[] ob=new Object[3];
                // generates two matrix for each parameterised test
                for(int j=0;j<2;j++){
                    Pair p = Utils.getSparseMatrix(i);
                    switch (type){
                        case "INTM": IntMatrix intm=new IntMatrix(p.values);
                                    ob[j]=intm;
                                    break;
                        case "CRS": CRS c=convertToCRS(p.values, p.nnz);
                                    ob[j]=c;
                                    break;
                        case "JSA": JavaSparseArray jsa=convertToJSA(p.values);
                                    ob[j]=jsa;
                                    break;
                        case "JAMA":Matrix matrix = new Matrix(convertIntToDoubleArray(p.values));
                                    ob[j]=matrix;
                                    break;

                    }
                    //ob[j]=c;
                }
                ob[2]=i;
                list.add(ob);
            }
        }
        return list;
    }


    //https://www.mkyong.com/java/how-to-export-data-to-csv-file-java/
    public static void writeCSVLine(FileWriter writer, List<String> inputs)throws IOException{
        StringBuilder sb = new StringBuilder();
        sb.append(inputs.get(0));
        for(int i=1; i<inputs.size(); i++){
            sb.append(",");
            sb.append(inputs.get(i));
        }
        sb.append("\n");
        try{
            writer.append(sb.toString());
        }
        catch (IOException e){
            e.getMessage();
            System.out.println("Failed to write input line to CSV");
        }

    }
    //https://shantonusarker.blogspot.co.uk/2017/01/junit-benchmark.html
    public static void loadProperties() throws IOException {
        Properties p = new Properties();
        p.load(new FileInputStream(new File("src/test/resources/jub.properties")));
        for(String k:p.stringPropertyNames()){
            System.setProperty(k,p.getProperty(k));
        }
    }


}
