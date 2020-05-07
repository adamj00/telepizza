import java.io.Serializable;
@SuppressWarnings("serial")
public class Produkt implements Serializable {
    String nazwa = "";
    double cena = 0.0;
    Produkt(String n, double c) {
        nazwa = n;
        cena = c;
    }
    Produkt (){}
    public void set (String n, double c){
        nazwa = n;
        cena = c;
    }
    public String toString(){
        return  nazwa + "\t\t" + cena;
    }
}