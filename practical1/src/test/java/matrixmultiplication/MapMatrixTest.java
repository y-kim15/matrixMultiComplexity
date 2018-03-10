package matrixmultiplication;

import Jama.Matrix;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import matrixmultiplication.IntMatrixMultiplication.AdvancedMultiplier;
import matrixmultiplication.IntMatrixMultiplication.IntMatrix;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class MapMatrixTest {
    private static String fileName = "./jama_mul_output.csv";
    private static FileWriter writer;
    private static List<String> inputBuffer = new ArrayList<String>();
    private static long totalTime = 0;
    private static int count = 0;
    private static int repeat = 2;
    @Parameterized.Parameters()//name= "{index}: {0}, {1}, n = {2}")
    public static Iterable<Object[]> data() {
        return Utils.getParams("MAP", 5, 5, repeat, 1);
    }

    private MapMatrix a;
    private MapMatrix b;
    private int n;

    private static Matrix createMatrix(int[][] values){
        double[][] array = Utils.convertIntToDoubleArray(values);
        Matrix m = new Matrix(array);
        return m;
    }

    public MapMatrixTest(MapMatrix a, MapMatrix b, int len){
        this.a = a;//createMatrix(a.getValues());
        this.b = b;//createMatrix(b.getValues());
        n = len;
    }

    public static String convertString(MapMatrix map) {
        StringBuilder sb = new StringBuilder();
        Map<Pair, Integer> matrixMap = map.getMatrix();
        Iterator<Map.Entry<Pair,Integer>> iter = matrixMap.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry<Pair, Integer> entry = iter.next();
            sb.append(entry.getKey().getX()+" " + entry.getKey().getY());
            sb.append('=').append('"');
            sb.append(entry.getValue());
            sb.append('"');
            if (iter.hasNext()) {
                sb.append(',').append(' ');
            }
        }
        return sb.toString();

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
        System.out.println(convertString(a));
        System.out.println(convertString(b));
        if(count%repeat==0) inputBuffer.add(Integer.toString(n));

    }

    @Test
    public void testConvertAndMultiply(){
        IntMatrix a1 = Utils.convertToIntMatrix(a);
        System.out.println(Arrays.deepToString(a1.getValues()));
        IntMatrix b1 = Utils.convertToIntMatrix(b);
        System.out.println(Arrays.deepToString(b1.getValues()));
        long startTime = System.nanoTime();
        new AdvancedMultiplier().multiply(a1,b1);
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
