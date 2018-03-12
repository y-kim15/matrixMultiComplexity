package matrixmultiplication;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Data {

    /**
     * Stores values of a matrix in a string format to be saved in a CSV file
     * @param data a pair data containing 2D int array and int for nnz value
     * @return a string containing values separated by commas for row.
     * Important thing to note is that we set all entries to be one digit number.
     */
    public static String makeString(MatrixData data){
        StringBuilder builder = new StringBuilder();
        int[][] v = data.values;
        for(int i =0; i < v.length; i++){
            for(int j=0; j < v.length; j++){
                builder.append(Integer.toString(v[i][j])+"");
            }
            if(i < v.length-1)builder.append(",");

        }
        builder.append(";");
        builder.append(data.nnz);
        builder.append("\n");
        return builder.toString();
    }

    /**
     * Writes input matrices to a CSV file
     * https://stackoverflow.com/questions/34958829/how-to-save-a-2d-array-into-a-text-file-with-bufferedwriter
     * @param filePath file to write
     * @param data a list of arrays containing matrices to write
     * @throws IOException occurs when the file path is invalid
     */
    public static void writeOutData(String filePath, List<Object[]> data) throws IOException{
        try{
            Files.deleteIfExists(Paths.get(filePath));
            BufferedWriter w = new BufferedWriter(new FileWriter(filePath));
            for(int i=0; i< data.size(); i++){
                for(int j=0; j< 2; j++){
                    MatrixData d = (MatrixData) data.get(i)[j];
                    String matStr = makeString(d);
                    w.write(matStr);

                }
            }
            w.close();
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException{
        String fileName;
        List<Object[]> params = new ArrayList<>();
        String testName = args[0];
        double sparsity; int matrixType;
        if(args.length ==1) sparsity = 0.75;
        else sparsity = Math.round(Double.valueOf(args[1])*100D)/100D;
        if(args.length == 1 || args.length == 2) matrixType = 0;
        else matrixType = Integer.valueOf(args[2]);
        int min=500, max=1000, repeat=25, step=50;
        if(testName.equals("basic")){
            min = 300; repeat=20;
        }
        params = Utils.getParamsByConditions(min,max,repeat,step,sparsity,matrixType);
        fileName = Utils.getFileName(testName, sparsity, matrixType, true);
        writeOutData(fileName, params);

    }
}
