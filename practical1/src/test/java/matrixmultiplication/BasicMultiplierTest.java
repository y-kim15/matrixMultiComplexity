package matrixmultiplication;

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
 * Test for Task 2, tests Basic Algorithm
 */
@RunWith(Parameterized.class)
public class BasicMultiplierTest{
    private BasicMultiplier testSubject = new BasicMultiplier();
    private static String fileName;
    private static FileWriter writer;
    private static List<String> inputBuffer = new ArrayList<String>();
    private static long totalTime=0;
    private static int count = 0;
    private static int repeat = 20;
    private static double sparsity = 0.75;
    private static int position = 0;

    /**
     * Generates matrices used according to command line arguments
     * or using default if the arguments are not provided.
     * @return output of getParamsByConditions method (a list of arrays of 2 matrices)
     */
    @Parameterized.Parameters()
    public static Iterable<Object[]> data()throws IOException {
        double spar; int matrixType;
        if(System.getProperty("sparsity").isEmpty()) spar = sparsity;
        else spar = Math.round(Double.valueOf(System.getProperty("sparsity"))*100D)/100D;

        if(System.getProperty("matrixType").isEmpty()) matrixType = position;
        else matrixType = Integer.valueOf(System.getProperty("matrixType"));

        fileName = Utils.getFileName("basic", spar, matrixType, false);

        String inputFile = Utils.getFileName("basic", spar, matrixType, true);
        if(Files.exists(Paths.get(inputFile))) return Utils.getParams(inputFile, 300,1000,repeat,50);
        else return Utils.getParamsByConditions(300, 1000, repeat, 50, spar, matrixType);
    }

    private MatrixData a;
    private MatrixData b;
    private int n;

    public BasicMultiplierTest(MatrixData inA, MatrixData inB, int len){
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
            Utils.writeCSVLine(writer, Arrays.asList("Matrix Dim n","Basic Multiplier"));
        }
        catch(IOException e){
            e.getMessage();
            System.out.println(e.getStackTrace());
        }

    }

    @Test
    public void testIntMatrixMultiplication(){
        IntMatrix m1 = new IntMatrix(a.values);
        IntMatrix m2 = new IntMatrix(b.values);
        long startTime = System.nanoTime();
        testSubject.multiply(m1,m2);
        long endTime   = System.nanoTime();
        long time = (endTime - startTime)/100000;
        totalTime += time;

        count++;

    }

    @After
    public void writeToCSV() throws IOException {
        if(count==repeat){
            inputBuffer.add(Integer.toString(n));
            long average = totalTime/repeat;
            inputBuffer.add(Long.toString(average));
            totalTime =0;
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
