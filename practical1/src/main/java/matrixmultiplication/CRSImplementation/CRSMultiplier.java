package matrixmultiplication.CRSImplementation;

import static java.lang.StrictMath.round;

public class CRSMultiplier {
    public String toString() {
        return "CRSMultiplier";
    }
    //estimated no of nnz is 1.5*(a.nnz+b.nnz)
    //note we always follow first element in index 0 not 1
    public CRS multiply(CRS a, CRS b) {
        int n = a.getN();
        SPA spa = new SPA(n);
        CRS c = new CRS(n);
        c.setRowPtr(0,0);
        for(int i=0; i<n; i++){
            for(int k=a.getRowPtr(i); k<a.getRowPtr(i+1);k++){
                for(int j=b.getRowPtr(a.getColInd(k)); j<b.getRowPtr(a.getColInd(k)+1); j++){
                    int val = a.getValue(k)*b.getValue(j);
                    spa.scatterSPA(val,b.getColInd(j));
                }
            }
            c = spa.gatherSPA(c,i);
            spa = new SPA(n);
        }
        c.trimCRS();
        return c;
    }

}
