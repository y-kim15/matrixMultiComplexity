package matrixmultiplication;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
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
public class CRSMultiplierTest {
    private CRSMultiplier testSubject = new CRSMultiplier();
    private static String fileName = "./crs_mul_output1.csv";
    private static FileWriter writer;
    private static List<String> inputBuffer = new ArrayList<String>();
    private static long totalTime=0;
    private static int count = 0;
    private static int repeat = 20;

    @Parameterized.Parameters()//name= "{index}: {0}, {1}, n = {2}")
    public static Iterable<Object[]> data() {
        return Utils.getParams("CRS",100,1000,20,100);
    }

    private CRS a;
    private CRS b;
    private CRS c;
    private int len; //same value as nEach //repeat could go to instance init?


    public CRSMultiplierTest(CRS a, CRS b, int len){
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
            Utils.writeCSVLine(writer, Arrays.asList("Input Size n", "CRS_Times"));
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
        /*System.out.println("Before====");
        a.printCRS();
        System.out.println("no of nonzeros: " + a.getNnz() );
        b.printCRS();
        System.out.println("no of nonzeros: " + b.getNnz());*/
        //System.out.println(Arrays.deepToString(a.getValueA()));
        //System.out.println(Arrays.deepToString(a.getIndexA()));
        //System.out.println(Arrays.deepToString(b.getValueA()));
        //System.out.println(Arrays.deepToString(b.getIndexA()));
    }

    @Test
    //@BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 4)
    public void testConvertAndMultiply(){
        long startTime = System.nanoTime();
        c = testSubject.multiply(a,b);
        long endTime   = System.nanoTime();
        totalTime += (endTime - startTime)/100000;
        count++;
        if(count%repeat==0){
            long average = totalTime/repeat;
            inputBuffer.add(Long.toString(average));
            totalTime =0;
        }

        //System.out.println("Result====");
        //c.printCRS();

    }

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
