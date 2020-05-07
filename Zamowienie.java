import java.util.LinkedList;
import java.io.Serializable;
import java.util.Date;
@SuppressWarnings("serial")
public class Zamowienie implements Serializable {
    LinkedList<Produkt> produktList = new LinkedList<Produkt>();

    String adres = "";
    String nr_tel = "";
    boolean platnosc_karta = false;
    Date data_zamowienia = new Date ();
    String uwagi = "";

    public Zamowienie (LinkedList<Produkt> pL, String a, String n, boolean p, Date d, String u){
        produktList = pL;
        adres = a;
        nr_tel = n;
        platnosc_karta = p;
        data_zamowienia = d;
        uwagi = u;
    }

    public double wartosc_zamowienia () {
        double wynik = 0.0;
        for (Produkt p : produktList){
            wynik += p.cena;
        }
        return wynik;
    }
}