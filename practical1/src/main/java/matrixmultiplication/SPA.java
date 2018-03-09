package matrixmultiplication;

import java.util.Arrays;

public class SPA {
    private int[] v;
    private boolean[] b;
    private int[] col;
    private int ncur;

    //initialise v with length n(for n*n matrix)
    public SPA(int n){
        v = new int[n];
        Arrays.fill(v, 0);
        b = new boolean[n];
        Arrays.fill(b,false);
        col = new int[n]; //max would be n nonzero values
        Arrays.fill(col,0);
        ncur = 0;
    }

    //scatterSPA will add a scalar value to a specific position
    //of the SPA.w vector and update the other elements accordingly.
    //note this pos value is the column index
    public void scatterSPA(int value, int pos){
        if(!this.b[pos]){
            this.v[pos] = value;
            this.b[pos] = true;
            this.col[ncur++] = pos;
        }
        else this.v[pos] += value;
    }

    //gatherSPA will append the information in SPA
    //to the matrix, returning it updated to include the latest
    //SPA data
    //for us, col will always be emptied after each row
    public CRS gatherSPA(CRS c, int rowIndex){
        int curNnz = c.getNnz();
        int cptr = 0;
        int nnzRowI = 0;
        while(cptr < this.ncur){
            int colIn = this.col[cptr];
            c.setColInd(curNnz+nnzRowI, colIn);
            c.setValue(curNnz+nnzRowI, this.v[colIn]);
            nnzRowI++;
            cptr++;
        }
        c.setNnz(curNnz+nnzRowI);
        c.setRowPtr(rowIndex,curNnz);
        c.setRowPtr(rowIndex+1,c.getNnz());
        return c;
    }



}
