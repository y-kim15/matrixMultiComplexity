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
    private AdvancedMultiplier testSubject2 = new AdvancedMultiplier();
    private static String fileName = "./basic_mul_output.csv";
    private static FileWriter writer;
    private static List<String> inputBuffer = new ArrayList<String>();
    private static long[] totalTime=new long[]{0,0,0,0};
    private static int count = 0;
    private static int repeat = 20;
    @Parameterized.Parameters()//name= "{index}: {0}, {1}, n = {2}")
    public static Iterable<Object[]> data() {
        return Utils.getParams("INTM", 100,1000,20,100);
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
            Utils.writeCSVLine(writer, Arrays.asList("Input Length n","Basic","Improved","Advanced","CRS"));
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
        long startTime = System.nanoTime();
        c = testSubject.multiply(a,b);
        long endTime   = System.nanoTime();
        long time = (endTime - startTime)/100000;
        totalTime[0]+= time;

        startTime = System.nanoTime();
        c = testSubject.improvedMultiply(a,b);
        endTime   = System.nanoTime();
        time = (endTime - startTime)/100000;
        totalTime[1]+= time;

        startTime = System.nanoTime();
        c = testSubject2.multiply(a,b);
        endTime   = System.nanoTime();
        time = (endTime - startTime)/100000;
        totalTime[2]+= time;

        count++;
        if(count%repeat==0){
            for(int i=0; i<totalTime.length;i++){
                long average = totalTime[i]/repeat;
                inputBuffer.add(Long.toString(average));
            }
            Arrays.fill(totalTime,0);
        }
    }

    /*@Test
    //@BenchmarkOptions( benchmarkRounds = 5, warmupRounds = 4)
    public void testBasicMultiplication(){
        long startTime = System.nanoTime();
        c = testSubject.multiply(a,b);
        long endTime   = System.nanoTime();
        totalTime = (endTime - startTime)/100000;
        inputBuffer.addAll(Arrays.asList(Long.toString(totalTime),"-","-"));
    }

    @Test
    //@BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 4)
    public void testImprovedMultiplication(){
        long startTime = System.nanoTime();
        c = testSubject.improvedMultiply(a,b);
        long endTime   = System.nanoTime();
        totalTime = (endTime - startTime)/100000;
        inputBuffer.addAll(Arrays.asList("-",Long.toString(totalTime),"-"));
    }


    @Test
    //@BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 4)
    public void testAdvancedMultiplication(){
        long startTime = System.nanoTime();
        c = testSubject2.multiply(a,b);
        long endTime   = System.nanoTime();
        totalTime = (endTime - startTime)/100000;
        inputBuffer.addAll(Arrays.asList("-","-",Long.toString(totalTime)));

    }
*/


    @After
    public void recordOutput()throws IOException{
        if(count%repeat==0){
            //inputBuffer.add(Long.toString(totalTime));

            Utils.writeCSVLine(writer,inputBuffer);
            inputBuffer.clear();
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
