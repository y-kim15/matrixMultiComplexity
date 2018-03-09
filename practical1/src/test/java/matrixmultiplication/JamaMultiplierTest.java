package matrixmultiplication;

import Jama.Matrix;
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
public class JamaMultiplierTest {
    private static String fileName = "./jama_mul_output.csv";
    private static FileWriter writer;
    private static List<String> inputBuffer = new ArrayList<String>();
    private static long totalTime = 0;
    private static int count = 0;
    private static int repeat = 5;
    @Parameterized.Parameters()//name= "{index}: {0}, {1}, n = {2}")
    public static Iterable<Object[]> data() {
        return Utils.getParams("JAMA", 100, 1000, 5, 100);
    }

    private Matrix a;
    private Matrix b;
    private int n;

    private static Matrix createMatrix(int[][] values){
        double[][] array = Utils.convertIntToDoubleArray(values);
        Matrix m = new Matrix(array);
        return m;
    }

    public JamaMultiplierTest(Matrix a, Matrix b, int len){
        this.a = a;//createMatrix(a.getValues());
        this.b = b;//createMatrix(b.getValues());
        n = len;
    }

    @BeforeClass
    public static void prepare() throws IOException
    {
        try {
            Files.deleteIfExists(Paths.get(fileName));
            writer = new FileWriter(fileName);
            Utils.writeCSVLine(writer, Arrays.asList("Input Size n", "Jama_Times"));
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
        //System.out.println(Arrays.deepToString(a.getValueA()));
        //System.out.println(Arrays.deepToString(a.getIndexA()));
        //System.out.println(Arrays.deepToString(b.getValueA()));
        //System.out.println(Arrays.deepToString(b.getIndexA()));
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 4)
    public void testConvertAndMultiply(){
        long startTime = System.nanoTime();
        a.times(b);
        long endTime = System.nanoTime();
        totalTime += (endTime - startTime)/100000;
        count++;
        if(count%repeat==0){
            long average = totalTime/repeat;
            inputBuffer.add(Long.toString(average));
            totalTime =0;
        }
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
