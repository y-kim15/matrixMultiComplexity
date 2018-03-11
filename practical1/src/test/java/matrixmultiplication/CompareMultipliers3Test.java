package matrixmultiplication;

import Jama.Matrix;
import matrixmultiplication.CRSImplementation.CRS;
import matrixmultiplication.CRSImplementation.CRSMultiplier;
import matrixmultiplication.HashMapImplementation.MapMatrix;
import matrixmultiplication.IntMatrixMultiplication.AdvancedMultiplier;
import matrixmultiplication.IntMatrixMultiplication.IntMatrix;
import matrixmultiplication.JSAImplementation.JavaSparseArray;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test for Task 4 and 5
 * This will look at optimal implementation of matrix and multiplication
 * Advanced multiplier will be used. This test will also be used under different command line arguments for
 * sparsity and a type of matrix.
 */
@RunWith(Parameterized.class)
public class CompareMultipliers3Test {
    private AdvancedMultiplier testSubject = new AdvancedMultiplier();
    private static String fileName;
    private static FileWriter writer;
    private static List<String> inputBuffer = new ArrayList<String>();
    private static long[] totalTime = new long[]{0,0,0,0,0};
    private static int count = 0;
    private static int repeat = 30;
    private static double sparsity = 0.75;
    private static int position = 0;

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        double spar; int matrixType;
        if(System.getProperty("sparsity").isEmpty()) spar = sparsity;
        else spar = Math.round(Double.valueOf(System.getProperty("sparsity"))*100D)/100D;

        if(System.getProperty("matrixType").isEmpty()) matrixType = position;
        else matrixType = Integer.valueOf(System.getProperty("matrixType"));

        fileName = Utils.getFileName("comp3", spar, matrixType);

        return Utils.getParamsByConditions(500,1000, repeat,50, spar, matrixType);
    }

    private MatrixData a;
    private MatrixData b;
    private int len;

    public CompareMultipliers3Test(MatrixData a, MatrixData b, int len){
        this.a = a;
        this.b = b;
        this.len = len;
    }

    @BeforeClass
    public static void prepare() throws IOException
    {
        try {
            Files.deleteIfExists(Paths.get(fileName));
            writer = new FileWriter(fileName);
            Utils.writeCSVLine(writer, Arrays.asList("Matrix Dim n", "JSA", "MapMatrix", "CRS", "IntMatrix", "Jama"));
        }
        catch(IOException e){
            e.getMessage();
            System.out.println(e.getStackTrace());
        }

    }

    @Before
    public void passInput(){
        IntMatrix a1 = new IntMatrix(a.values);
        IntMatrix b1 = new IntMatrix(b.values);

    }
    @Test
    public void testJSAMultiply(){
        JavaSparseArray jsa1 = Utils.getJSA(a.values);
        JavaSparseArray jsa2 = Utils.getJSA(b.values);
        long startTime = System.nanoTime();
        IntMatrix m1 = Utils.convertToIntMatrix(jsa1);
        IntMatrix m2 = Utils.convertToIntMatrix(jsa2);
        testSubject.multiply(m1,m2);
        long endTime   = System.nanoTime();
        totalTime[0] += (endTime - startTime)/100000;

    }

    @Test
    public void testMapMatrixMultiply() throws IOException {
        MapMatrix map1 = Utils.getMapMatrix(a.values, a.nnz);
        MapMatrix map2 = Utils.getMapMatrix(b.values, b.nnz);
        long startTime = System.nanoTime();
        IntMatrix m1 = Utils.convertToIntMatrix(map1);
        IntMatrix m2 = Utils.convertToIntMatrix(map2);
        testSubject.multiply(m1,m2);
        long endTime   = System.nanoTime();
        totalTime[1] += (endTime - startTime)/100000;

    }

    @Test
    public void testCRSMultiply(){
        CRS crs1 = Utils.getCRS(a.values,a.nnz);
        CRS crs2 = Utils.getCRS(b.values,b.nnz);

        long startTime = System.nanoTime();
        new CRSMultiplier().multiply(crs1,crs2);
        long endTime   = System.nanoTime();
        totalTime[2] += (endTime - startTime)/100000;

    }

    @Test
    public void testIntMatrixMultiply(){
        IntMatrix newA = new IntMatrix(a.values);
        IntMatrix newB = new IntMatrix(b.values);

        long startTime = System.nanoTime();
        testSubject.multiply(newA, newB);
        long endTime = System.nanoTime();
        totalTime[3] += (endTime - startTime)/100000;


    }

    @Test
    public void testJamaMultiply() throws IOException{
        Matrix a1 = new Matrix(Utils.convertIntToDoubleArray(a.values));
        Matrix b1 = new Matrix(Utils.convertIntToDoubleArray(b.values));
        long startTime = System.nanoTime();
        a1.times(b1);
        long endTime   = System.nanoTime();
        totalTime[4] += (endTime - startTime)/100000;
        count++;

    }



    @After
    public void writeToCSV() throws IOException{
        if(count==repeat){
            inputBuffer.add(Integer.toString(len));
            for(int i=0; i<totalTime.length;i++){
                long average = totalTime[i]/repeat;
                inputBuffer.add(Long.toString(average));
            }

            Utils.writeCSVLine(writer,inputBuffer);
            Arrays.fill(totalTime,0);
            inputBuffer.clear();
            count =0;
        }
    }


    @AfterClass
    public static void tidy()throws IOException{
        try{
            writer.close();
        }
        catch (IOException e){
            e.getMessage();
            System.out.println(e.getStackTrace());
        }
    }

}
