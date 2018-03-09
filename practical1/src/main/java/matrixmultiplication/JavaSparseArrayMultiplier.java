package matrixmultiplication;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JavaSparseArrayMultiplier {
    public String toString(){return "JavaSparseArrayMultiplier";}

    public JavaSparseArray multipliy(JavaSparseArray a, JavaSparseArray b) {
        int[][] BM = b.getValueA();
        int[][] BIn = b.getIndexA();
        int n = BM.length;
        int[][] CIn = new int[n][];
        int[][] CM = new int[n][];
        int[] Arowi, Browi, Arowv, Browv;
        int nnz=0;
        //List<Integer> Crowi = new ArrayList<>();
        //List<Integer> Crowv = new ArrayList<>();
        int i, k, j;
        for (i = 0; i < n; i++) {
            Arowi = a.getIndexA()[i];
            Arowv = a.getValueA()[i];
            //int[] Crowi = new int[n];
            //int[] Crowv = new int[n];
            List<Integer> Crowi = new ArrayList<>();
            List<Integer> Crowv = new ArrayList<>();
            //Crowv = new double[n];

            //Arrays.fill(Crowv,0.0);
            for (j = 0; j < Arowi.length; j++) {
                int in = Arowi[j];
                Browi = BIn[in];
                Browv = BM[in];
                for (k = 0; k < Browi.length; k++) {
                    int index = Browi[k];
                    //Crowv[index] += Arowv[j]*Browv[k];
                    if (Crowi.contains(index)) {
                        int found = Crowi.indexOf(index);
                        int exist = Crowv.get(found);
                        Crowv.set(found, exist + Arowv[j] * Browv[k]);
                    } else {
                        nnz++;
                        Crowv.add(Arowv[j] * Browv[k]);
                        Crowi.add(index);
                    }
                    /*if(Crowv[index]>0){
                        Crowv[index]+= (Arowv[j]+Browv[k]);
                    }
                    else{
                        Crowi[index] = 1;
                        Crowv[index] = Arowv[j]+Browv[k];
                    }*/

                }
            }
            int[] cRowi = Crowi.stream().mapToInt(Integer::intValue).toArray();
            int[] cRowv = Crowv.stream().mapToInt(Integer::intValue).toArray();//ArrayUtils.toPrimitive((Integer[])Crowv.toArray());
            /*List<Integer> ci = Arrays.stream(Crowi).boxed().collect(Collectors.toList());
            List<Integer> cv = Arrays.stream(Crowv).boxed().collect(Collectors.toList());
            ci.removeIf(ind -> ind==0);
            cv.removeIf(val -> val==0);
            int[] cRowi = ci.stream().mapToInt(Integer::intValue).toArray();
            int[] cRowv = cv.stream().mapToInt(Integer::intValue).toArray();*/
            CIn[i] = cRowi;//new int[cRowi.length];
            CM[i] = cRowv;
            //valueList.add(Crowv);
            //indexList.add(Crowi);
        }
        return new JavaSparseArray(CM, CIn, nnz);
    }
}
