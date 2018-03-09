package matrixmultiplication;

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
public class BasicMultiplierTest{
    private BasicMultiplier testSubject = new BasicMultiplier();
    private static String fileName = "./basic_mul_output.csv";
    private static FileWriter writer;
    private static List<String> inputBuffer = new ArrayList<String>();
    private static long totalTime=0;
    private static int count = 0;
    private static int repeat = 25;
    @Parameterized.Parameters()//name= "{index}: {0}, {1}, n = {2}")
    public static Iterable<Object[]> data() {
        return Utils.getParams("INTM",300,1000,repeat,50);
        //return Utils.parametersForTestMatrixMultiplication(true);
    }

    private IntMatrix a;
    private IntMatrix b;
    private IntMatrix c;
    private int n;

    public BasicMultiplierTest(IntMatrix inA, IntMatrix inB, int len){
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
        //Utils.loadProperties();

    }

    @Before
    public void passInput(){

        if(count%repeat==0) inputBuffer.add(Integer.toString(n));
    }

    @Test
    public void testIntMatrixMultiplication() throws IOException{
        long startTime = System.nanoTime();
        c = testSubject.multiply(a,b);
        long endTime   = System.nanoTime();
        long time = (endTime - startTime)/100000;
        totalTime += time;

        count++;
        if(count%repeat==0){
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
