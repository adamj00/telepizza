import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.*;
@SuppressWarnings("serial")
public class ProduktGUI extends JFrame {
    JTextArea produktTextArea = new JTextArea();
    Font font = new Font("Times New Roman", Font.BOLD, 20);
    JLabel nazwa_etykieta = new JLabel("Nazwa:");
    JTextField f_nazwa = new JTextField(10);
    JLabel cena_etykieta = new JLabel("Cena:");
    JTextField f_cena = new JTextField(10);

    JButton dodaj = new JButton("Dodaj");
    JButton usun = new JButton("Usuń");
    JButton wyjscie = new JButton("Wyjście");

    private LinkedList <Produkt> produktList = new LinkedList<Produkt>();

    public ProduktGUI () {
        odczyt ();
        JPanel flow1Panel = new JPanel (new FlowLayout (FlowLayout.CENTER));
        JPanel flow2Panel = new JPanel (new FlowLayout (FlowLayout.CENTER));
        JPanel gridPanel = new JPanel (new GridLayout (2,1)); 

        produktTextArea.setEditable(false);

        flow1Panel.add (nazwa_etykieta);
        flow1Panel.add (f_nazwa);
        flow1Panel.add (cena_etykieta);
        flow1Panel.add (f_cena);

        flow2Panel.add (dodaj);
        flow2Panel.add (usun);
        flow2Panel.add (wyjscie);

        gridPanel.add (flow1Panel);
        gridPanel.add (flow2Panel);

        add (produktTextArea, BorderLayout.CENTER);
        add (gridPanel, BorderLayout.SOUTH);

        produktTextArea.setFont(font);

        dodaj.addActionListener(event -> dodajProdukt ());
        wyjscie.addActionListener(event -> wyjdz ());
        usun.addActionListener(event -> usunProdukt ());

        displayAll();
    }
    private void dodajProdukt (){
        produktList.add (new Produkt (f_nazwa.getText(), 
                                      Double.parseDouble(f_cena.getText())));
        displayAll();
    }
    
    private void wyjdz () {
        // Zapis
        zapis ();
        System.exit(0);
    }

    private void zapis () {
        try {
            FileOutputStream fos = new FileOutputStream("produkty.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(produktList);
            oos.flush();
            oos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void odczyt () {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream("produkty.txt");
            ois = new ObjectInputStream(fis);
            produktList = (LinkedList<Produkt>) ois.readObject();
        } catch (FileNotFoundException err) {
            System.out.println("Nie znaleziono pliku 'produkty.txt.'");
        } catch (IOException err) {
            err.printStackTrace();
        } catch (ClassNotFoundException err) {
            err.printStackTrace();
        }
    }

    private void displayAll () {
        produktTextArea.setText("Nazwa\t\tCena\n\n");
        for (Produkt p : produktList){
            produktTextArea.append (p.toString() + "\n");
        }
    }

    private void usunProdukt (){
        for (Produkt p : produktList){
            if (p.nazwa.compareToIgnoreCase(f_nazwa.getText()) == 0){
                produktList.remove (p);
                displayAll();
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Nie ma produktu o takiej nazwie", "Błąd", 0);
    }

    
}