package matrixmultiplication;

import matrixmultiplication.IntMatrixMultiplication.AdvancedMultiplier;
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

/**
 * This test will compare different matrix multiplication algorithms
 * using IntMatrix class.basic, improve, advanced etc.
 */
@RunWith(Parameterized.class)
public class CompareMultipliers1Test {
    private BasicMultiplier testSubject = new BasicMultiplier();
    private AdvancedMultiplier testSubject2 = new AdvancedMultiplier();
    private static String fileName = "./intMatrix_mul_compare_output.csv";
    private static FileWriter writer;
    private static List<String> inputBuffer = new ArrayList<String>();
    private static long[] totalTime=new long[]{0,0,0};
    private static int count = 0;
    private static int repeat = 30;
    private static double sparsity = 0.75;
    private static int position = 0;
    @Parameterized.Parameters()//name= "{index}: {0}, {1}, n = {2}")
    public static Iterable<Object[]> data() {
        return Utils.getParamsByConditions(500,1000,repeat,50,sparsity,position);
        //return Utils.parametersForTestMatrixMultiplication(true);
    }

    private MatrixData a;
    private MatrixData b;
    private int n;

    public CompareMultipliers1Test (MatrixData inA, MatrixData inB, int len){
        a = inA;
        b = inB;
        n = len;
    }

    @BeforeClass
    public static void prepare() throws IOException
    {
        try {
            Files.deleteIfExists(Paths.get(fileName));
            writer = new FileWriter(fileName);
            Utils.writeCSVLine(writer, Arrays.asList("Matrix Dim n","Basic","Improved","Advanced"));
        }
        catch(IOException e){
            e.getMessage();
            System.out.println(e.getStackTrace());
        }
        //Utils.loadProperties();

    }

    @Before
    public void passInput(){

        if(count%repeat==0) inputBuffer.add(Integer.toString(n));
    }

    @Test
    public void testIntMatrixMultiplication(){
        IntMatrix m1 = new IntMatrix(a.values);
        IntMatrix m2 = new IntMatrix(b.values);
        long startTime = System.nanoTime();
        testSubject.multiply(m1,m2);
        long endTime   = System.nanoTime();
        long time = (endTime - startTime)/100000;
        totalTime[0]+= time;

        startTime = System.nanoTime();
        testSubject.improvedMultiply(m1,m2);
        endTime   = System.nanoTime();
        time = (endTime - startTime)/100000;
        totalTime[1]+= time;

        startTime = System.nanoTime();
        testSubject2.multiply(m1,m2);
        endTime   = System.nanoTime();
        time = (endTime - startTime)/100000;
        totalTime[2]+= time;

    }

    @After
    public void writeToCSV()throws IOException{
        count++;
        if(count==repeat){
            for(int i=0; i<totalTime.length;i++){
                long average = totalTime[i]/repeat;
                inputBuffer.add(Long.toString(average));
            }
            Arrays.fill(totalTime,0);
            Utils.writeCSVLine(writer,inputBuffer);
            inputBuffer.clear();
            count=0;
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
