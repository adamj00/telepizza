import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
@SuppressWarnings("serial")
public class ProduktGUI extends JFrame {
    JTextArea produktTextArea = new JTextArea();
    JLabel nazwa_etykieta = new JLabel("Nazwa:");
    JTextField f_nazwa = new JTextField(10);
    JLabel cena_etykieta = new JLabel("Cena:");
    JTextField f_cena = new JTextField(10);

    JButton dodaj = new JButton("Dodaj");
    JButton usun = new JButton("Usuń");
    JButton wyjscie = new JButton("Wyjście");

    private LinkedList <Produkt> produktList = new LinkedList<Produkt>();

    public ProduktGUI () {
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
        // Zapis!!
        System.exit(0);
    }

    private void displayAll () {
        produktTextArea.setText("Nazwa\tCena\n\n");
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
            JOptionPane.showMessageDialog(null, "Nie ma produktu o takiej nazwie", "Błąd", 0);
        }
    }
}