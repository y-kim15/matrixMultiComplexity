package matrixmultiplication;

import Jama.Matrix;
import matrixmultiplication.CRSImplementation.CRS;
import matrixmultiplication.CRSImplementation.CRSMultiplier;
import matrixmultiplication.IntMatrixMultiplication.AdvancedMultiplier;
import matrixmultiplication.IntMatrixMultiplication.BasicMultiplier;
import matrixmultiplication.IntMatrixMultiplication.IntMatrix;
import matrixmultiplication.JSAImplementation.JavaSparseArray;
import matrixmultiplication.JSAImplementation.JavaSparseArrayMultiplier;
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
 * this is looking at space complexity by storing at different data structure and convert to multiply
 */
@RunWith(Parameterized.class)
public class CompareMultipliers3Test {
    private static String fileName = "./compare_dataStruct2_mul_output.csv";
    private static FileWriter writer;
    private static List<String> inputBuffer = new ArrayList<String>();
    private static long[] totalTime = new long[]{0,0,0,0};
    private static int count = 0;
    private static int repeat = 10;
    private static double sparsity = 0.75;
    private static int position = 0;

    @Parameterized.Parameters()//name= "{index}: {0}, {1}, n = {2}")
    public static Iterable<Object[]> data() {

        return Utils.getParamsByConditions(5,25, repeat,5, 0.75, 0);
    }

    private MatrixData a;
    private MatrixData b;
    private int len; //same value as nEach //repeat could go to instance init?

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
            Utils.writeCSVLine(writer, Arrays.asList("Sparsity", Double.toString(sparsity), "Positions", Integer.toString(position), "random"));
            Utils.writeCSVLine(writer, Arrays.asList("Matrix Dim n", "JSA", "CRS", "IntMatrix", "MapMatrix"));
        }
        catch(IOException e){
            e.getMessage();
            System.out.println(e.getStackTrace());
        }
        //Utils.loadProperties();

    }

    @Before
    public void passInput(){
        if(count%repeat==0) inputBuffer.add(Integer.toString(len));
    }

    @Test
    public void testJSAMultiply(){
        JavaSparseArray jsa1 = Utils.convertToJSA(a.values);
        JavaSparseArray jsa2 = Utils.convertToJSA(b.values);
        long startTime = System.nanoTime();
        IntMatrix m1 = Utils.convertToIntMarix(jsa1);
        IntMatrix m2 = Utils.convertToIntMarix(jsa2);
        new AdvancedMultiplier().multiply(m1,m2);
        long endTime   = System.nanoTime();
        totalTime[0] += (endTime - startTime)/100000;

    }
    @Test
    public void testConvertAndMultiply(){ //testCRS
        System.out.println("CRS multiply");
        CRS crs1 = Utils.convertToCRS(a.values,a.nnz);
        CRS crs2 = Utils.convertToCRS(b.values, b.nnz);
        long startTime = System.nanoTime();
        new CRSMultiplier().multiply(crs1,crs2);
        long endTime   = System.nanoTime();
        totalTime[1] += (endTime - startTime)/100000;

    }

    @Test
    public void testIntMatrixMultiply(){
        System.out.println("IntMatrix multiply");;
        IntMatrix newA = new IntMatrix(a.values);
        IntMatrix newB = new IntMatrix(b.values);

        long startTime = System.nanoTime();
        new AdvancedMultiplier().multiply(newA, newB);
        long endTime = System.nanoTime();
        totalTime[2] += (endTime - startTime)/100000;


    }



    @Test
    public void testMapMatrixMultiply() throws IOException {
        MapMatrix map1 = Utils.getMapMatrix(a.values, a.nnz);
        MapMatrix map2 = Utils.getMapMatrix(b.values, b.nnz);
        System.out.println("MapMatrixMultiply");
        long startTime = System.nanoTime();
        IntMatrix m1 = Utils.convertToIntMatrix(map1);
        IntMatrix m2 = Utils.convertToIntMatrix(map2);
        new AdvancedMultiplier().multiply(m1,m2);
        long endTime   = System.nanoTime();
        totalTime[3] += (endTime - startTime)/100000;

    }

    @After
    public void writeToCSV() throws IOException{
        count++;
        if(count%repeat==0){
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
