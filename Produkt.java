public class Produkt {
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
        return  nazwa + "\t" + cena;
    }
}