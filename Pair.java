import java.io.Serializable;

public class Pair implements Serializable {
    private Produkt p;
    private int ilosc;
    public Pair (Produkt pr, int il){
        p = pr;
        ilosc = il;
    }
    public Produkt getP (){
        return p;
    }
    public int getI () {
        return ilosc;
    }
    public void setP (Produkt pr) {
        p = pr;
    }
    public void setI (int i){
        ilosc = i;
    }
    public void set (Produkt pr, int il) {
        p = pr;
        ilosc = il;
    }
    public void incI (int x){
        ilosc +=x;
    }
    public String toString () {
        return p.nazwa + " x " + ilosc;
    }
}