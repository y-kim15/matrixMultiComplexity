package matrixmultiplication;

import Jama.Matrix;
import matrixmultiplication.CRSImplementation.CRS;
import matrixmultiplication.CRSImplementation.CRSMultiplier;
import matrixmultiplication.IntMatrixMultiplication.AdvancedMultiplier;
import matrixmultiplication.JSAImplementation.JavaSparseArray;
import matrixmultiplication.JSAImplementation.JavaSparseArrayMultiplier;
import matrixmultiplication.IntMatrixMultiplication.BasicMultiplier;
import matrixmultiplication.IntMatrixMultiplication.IntMatrix;
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

@RunWith(Parameterized.class)
public class CompareMultipliers2Test {
    private static String fileName = "./compare_dataStruct_mul_output.csv";
    private static FileWriter writer;
    private static List<String> inputBuffer = new ArrayList<String>();
    private static long[] totalTime = new long[]{0,0,0,0,0};
    private static int count = 0;
    private static int repeat = 30;
    private static double sparsity = 0.75;
    private static int position = 0;

    @Parameterized.Parameters()//name= "{index}: {0}, {1}, n = {2}")
    public static Iterable<Object[]> data() {

        return Utils.getParamsByConditions(500,1000,repeat, 50, sparsity, position);
    }

    private MatrixData a;
    private MatrixData b;
    //private CRS c;
    private int len; //same value as nEach //repeat could go to instance init?


    public CompareMultipliers2Test(MatrixData a, MatrixData b, int len){
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
        //Utils.loadProperties();

    }


    @Test
    public void testJSAMultiply(){
        System.out.println("jsa");
        JavaSparseArray jsa1 = Utils.convertToJSA(a.values);
        JavaSparseArray jsa2 = Utils.convertToJSA(b.values);
        long startTime = System.nanoTime();
        IntMatrix m1 = Utils.convertToIntMarix(jsa1);
        IntMatrix m2 = Utils.convertToIntMarix(jsa2);
        new BasicMultiplier().multiply(m1,m2);
        long endTime   = System.nanoTime();
        totalTime[0] += (endTime - startTime)/100000;
    }

    @Test
    public void testMapMatrixMultiply() throws IOException {
        MapMatrix map1 = Utils.getMapMatrix(a.values, a.nnz);
        MapMatrix map2 = Utils.getMapMatrix(b.values, b.nnz);
        System.out.println("MapMatrixMultiply");
        long startTime = System.nanoTime();
        IntMatrix m1 = Utils.convertToIntMatrix(map1);
        IntMatrix m2 = Utils.convertToIntMatrix(map2);
        new BasicMultiplier().multiply(m1,m2);
        long endTime   = System.nanoTime();
        totalTime[1] += (endTime - startTime)/100000;

    }

    @Test
    //@BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 4)
    public void testCRSMultiply(){
        System.out.println("crs");
        CRS crs1 = Utils.convertToCRS(a.values,a.nnz);
        CRS crs2 = Utils.convertToCRS(b.values, b.nnz);
        long startTime = System.nanoTime();
        new CRSMultiplier().multiply(crs1,crs2);
        long endTime   = System.nanoTime();
        totalTime[2] += (endTime - startTime)/100000;

    }

    @Test
    public void testIntMatrixMultiply(){
        System.out.println("int");
        IntMatrix newA = new IntMatrix(a.values);
        IntMatrix newB = new IntMatrix(b.values);

        long startTime = System.nanoTime();
        new BasicMultiplier().multiply(newA, newB);
        long endTime = System.nanoTime();
        totalTime[3] += (endTime - startTime)/100000;


    }



    @Test
    public void testJamaMultiply() throws IOException{
        System.out.println("jama");
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
            Arrays.fill(totalTime,0);
            Utils.writeCSVLine(writer,inputBuffer);
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
