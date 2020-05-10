import java.util.LinkedList;
import java.io.Serializable;
import java.util.Date;
@SuppressWarnings("serial")
public class Zamowienie implements Serializable {
    LinkedList<Pair> produktList = new LinkedList<Pair>();

    String adres = "";
    String nr_tel = "";
    boolean platnosc_karta = false;
    Date data_zamowienia = new Date ();
    String uwagi = "";

    public Zamowienie (LinkedList<Pair> pL, String a, String n, boolean p, Date d, String u){
        produktList = pL;
        adres = a;
        nr_tel = n;
        platnosc_karta = p;
        data_zamowienia = d;
        uwagi = u;
    }

    public double wartosc_zamowienia () {
        double wynik = 0.0;
        for (Pair p : produktList){
            wynik += p.getP().cena * p.getI();
        }
        return wynik;
    }
}