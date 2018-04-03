package matrixmultiplication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(Parameterized.class)
public class DataTest {
    private static int min = 500;
    private static int max = 1000;
    private static int repeat = 25;
    private static int step = 50;
    private static double spar = 0.75;
    private static int position = 0;

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() throws IOException {
        List<Object[]> params = new ArrayList<>();
        String testName;
        double spars; int matrixType;

        if(System.getProperty("testName").isEmpty()) testName ="comp";
        else testName = System.getProperty("testName");


        if(System.getProperty("sparsity").isEmpty()) spars = spar;
        else spars = Math.round(Double.valueOf(System.getProperty("sparsity"))*100D)/100D;

        if(System.getProperty("matrixType").isEmpty()) matrixType = position;
        else matrixType = Integer.valueOf(System.getProperty("matrixType"));

        params.add(new Object[]{testName, spars, matrixType});
        return params;

    }

    private String testName;
    private double sparsity;
    private int matrixType;
    private String fileName;

    public DataTest (String testName, double sparsity, int matrixType){
        this.testName = testName;
        this.sparsity = sparsity;
        this.matrixType = matrixType;
    }

    @Test
    public void writeOutDataTest() throws IOException {

        if(testName.equals("basic")){
            min = 300; repeat=20;
        }

        List<Object[]> params = Utils.getParamsByConditions(min,max,repeat,step,sparsity,matrixType);
        fileName = Utils.getFileName(testName, sparsity, matrixType, true);
        Utils.writeOutData(fileName, params);
    }


}
