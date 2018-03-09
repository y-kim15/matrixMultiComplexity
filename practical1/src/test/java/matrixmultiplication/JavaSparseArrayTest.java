package matrixmultiplication;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import org.apache.commons.lang3.ArrayUtils;
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
public class JavaSparseArrayTest {
    private JavaSparseArrayMultiplier testSubject = new JavaSparseArrayMultiplier();
    private BasicMultiplier testSubject2 = new BasicMultiplier();
    private static String fileName = "./jsa_mul_output.csv";
    private static FileWriter writer;
    private static List<String> inputBuffer = new ArrayList<String>();
    private static long totalTime;
    @Parameterized.Parameters()//name= "{index}: {0}, {1}, n = {2}")
    public static Iterable<Object[]> data() {
        return Utils.parametersForTestMatrixMultiplication(false);
    }

    private JavaSparseArray a;
    private JavaSparseArray b;
    private JavaSparseArray c;
    private IntMatrix inA;
    private IntMatrix inB;
    private int n;

    public JavaSparseArrayTest(JavaSparseArray jsaA, JavaSparseArray jsaB, int len){
        a = jsaA;
        b = jsaB;
        n = len;
    }

    @BeforeClass
    public static void prepare() throws IOException
    {
        try {
            Files.deleteIfExists(Paths.get(fileName));
            writer = new FileWriter(fileName);
            Utils.writeCSVLine(writer, Arrays.asList("Length N", "ConvertMultiply","JSAMultiply"));
        }
        catch(IOException e){
            e.getMessage();
            System.out.println(e.getStackTrace());
        }
        //Utils.loadProperties();

    }

    @Before
    public void passInput(){
        inputBuffer.add(Integer.toString(n));
        //System.out.println(Arrays.deepToString(a.getValueA()));
        //System.out.println(Arrays.deepToString(a.getIndexA()));
        //System.out.println(Arrays.deepToString(b.getValueA()));
        //System.out.println(Arrays.deepToString(b.getIndexA()));
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 4)
    public void testJavaSparseArrayMultiplication(){
        long startTime = System.nanoTime();
        c=testSubject.multipliy(a,b);//c = a.multiply(b);
        long endTime   = System.nanoTime();
        totalTime = (endTime - startTime)/100000;
        inputBuffer.addAll(Arrays.asList("-", Long.toString(totalTime)));
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 4)
    public void testConvertAndMultiply(){
        long startTime = System.nanoTime();
        inA = Utils.converToIntMarix(a);//c = a.multiply(b);
        inB = Utils.converToIntMarix(b);
        testSubject2.multiply(inA,inB);
        long endTime   = System.nanoTime();
        totalTime = (endTime - startTime)/100000;
        inputBuffer.addAll(Arrays.asList(Long.toString(totalTime),"-"));
    }

    @After
    public void recordOutput()throws IOException{
        //inputBuffer.add(Long.toString(totalTime));
        Utils.writeCSVLine(writer,inputBuffer);
        inputBuffer.clear();


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
