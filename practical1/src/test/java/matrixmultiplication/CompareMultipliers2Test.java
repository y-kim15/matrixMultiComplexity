package matrixmultiplication;

import Jama.Matrix;
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
    private static long[] totalTime = new long[]{0,0,0,0};
    private static int count = 0;
    private static int repeat = 20;

    @Parameterized.Parameters()//name= "{index}: {0}, {1}, n = {2}")
    public static Iterable<Object[]> data() {

        return Utils.getParams("default",100,1000,20,100);
    }

    private Pair a;
    private Pair b;
    //private CRS c;
    private int len; //same value as nEach //repeat could go to instance init?


    public CompareMultipliers2Test(Pair a, Pair b, int len){
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
            Utils.writeCSVLine(writer, Arrays.asList("Matrix Dim n", "JSA", "CRS", "IntMatrix", "Jama"));
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
    public void testJSAMultiply(){
        System.out.println("JSA multiply");
        JavaSparseArray jsa1 = Utils.convertToJSA(a.values);
        JavaSparseArray jsa2 = Utils.convertToJSA(b.values);
        long startTime = System.nanoTime();
        new JavaSparseArrayMultiplier().multipliy(jsa1,jsa2);
        long endTime   = System.nanoTime();
        totalTime[0] += (endTime - startTime)/100000;

    }

    @Test
    //@BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 4)
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
        new BasicMultiplier().multiply(newA, newB);
        long endTime = System.nanoTime();
        totalTime[2] += (endTime - startTime)/100000;


    }



    @Test
    public void testJamaMultiply(){
        System.out.println("Jama multiply");
        Matrix a1 = new Matrix(Utils.convertIntToDoubleArray(a.values));
        Matrix b1 = new Matrix(Utils.convertIntToDoubleArray(b.values));
        long startTime = System.nanoTime();
        a1.times(b1);
        long endTime   = System.nanoTime();
        totalTime[3] += (endTime - startTime)/100000;

        count++;
        if(count%repeat==0){
            for(int i=0; i<totalTime.length;i++){
                long average = totalTime[i]/repeat;
                inputBuffer.add(Long.toString(average));
            }


        }


    }



    @After
    public void recordOutput()throws IOException{
        if(count%repeat==0){
            Arrays.fill(totalTime,0);
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
