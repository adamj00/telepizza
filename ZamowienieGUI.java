
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.Date;

import java.awt.*;
public class ZamowienieGUI {
    Zamowienie zamowienie = new Zamowienie ();
    LinkedList<Zamowienie> zamowieniaWszystkie = new LinkedList<Zamowienie>();

    String [] comboStrings;
    JPanel zamowieniePanel = new JPanel ();
    JPanel danePanel = new JPanel();
    JPanel podsumowaniePanel = new JPanel ();
    JFrame main_frame = new JFrame ();
    JTable table = new JTable () ;
    
    JCheckBox platnoscKarta = new JCheckBox();

    JComboBox produktSelectList;
    JTextField textIlosc = new JTextField();

    JTextField textAdres = new JTextField (50);
    JTextField textNr = new JTextField (50);
    JTextField textEmail = new JTextField (50);
    JTextField textUwagi = new JTextField("Brak uwag");

    JTextField suma = new JTextField();
    
    Font font = new Font("Times New Roman", Font.BOLD, 20);

    LinkedList<Pair> produktyDodane = new LinkedList<Pair>();
    LinkedList<Produkt> produktyWszystkie = new LinkedList<Produkt>();

    Object [] row = new Object [3]; 
    DefaultTableModel model = new DefaultTableModel();
    
    public ZamowienieGUI () {
        wczytaj_produkty();
        tab1 ();
        tab2 ();
        tab3 ();
        zamowienieUpdate();
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add ("Zamówienie",zamowieniePanel);
        tabbedPane.add ("Dane klienta",danePanel);
        tabbedPane.add ("Podsumowanie", podsumowaniePanel);
        main_frame.add (tabbedPane);
        main_frame.setSize(900,400);
        main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main_frame.setVisible(true);
        
    }

    private void tab1 () {  //Zamówienie
        textIlosc.setText("1");
        produktSelectList = new JComboBox(comboStrings);

        Object [] columns = {"Nazwa","Cena","Ilość"};
        
        
        suma.setBounds(600, 199, 280, 30);
        suma.setEditable(false);
        suma.setFont(font);

        JLabel razem = new JLabel ("Razem: ");
        razem.setBounds(500, 199, 270, 30);
        razem.setFont(font);

        model.setColumnIdentifiers(columns);
        table.setModel (model);

        table.setRowHeight(30);
        
        produktSelectList.setBounds (20,220,100,25);
        textIlosc.setBounds(150,220,100,25);

        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(0,0,880,200);

        zamowieniePanel.setLayout(null);

        JButton dodajButton = new JButton ("Dodaj");
        JButton usunButton = new JButton ("Usuń");
        dodajButton.setBounds (20,265,100,25);
        usunButton.setBounds (150,265,100,25);
        dodajButton.addActionListener(event -> dodaj_produkt());
        usunButton.addActionListener(event -> usun_produkt());

        zamowieniePanel.add (pane);
        zamowieniePanel.add (produktSelectList);
        zamowieniePanel.add (textIlosc);
        zamowieniePanel.add (dodajButton);
        zamowieniePanel.add (razem);
        zamowieniePanel.add (suma);
        zamowieniePanel.add (usunButton);
        zamowieniePanel.setVisible(true);
    }

    private void tab2 () {  // Dane klienta
        danePanel.setLayout(new GridLayout(3,2,100,0));

        JLabel adresLabel = new JLabel ("Adres dostawy: ");
        JLabel numerLabel = new JLabel ("Numer telefonu: ");
        JLabel emailLabel = new JLabel ("Adres Email: ");
        
        adresLabel.setFont(font);
        numerLabel.setFont(font);
        emailLabel.setFont(font);
        textAdres.setFont(font);
        textEmail.setFont(font);
        textNr.setFont(font);

        danePanel.add(adresLabel);
        danePanel.add(textAdres);
        danePanel.add(numerLabel);
        danePanel.add(textNr);
        danePanel.add(emailLabel);
        danePanel.add(textEmail);
    }

    private void tab3 () {
        podsumowaniePanel.setLayout(new GridLayout(6,1,5,5));

        JPanel topPanel = new JPanel ();
        topPanel.setLayout(new GridLayout (2,2,4,5));
        JLabel topLabel1 = new JLabel ("Do zapłaty: ");
        JLabel topLabel2 = new JLabel ("Adres dostawy: ");
        zamowienieUpdate();
        JTextField topText1 = new JTextField (suma.getText());
        JTextField topText2 = new JTextField();

        textAdres.getDocument().addDocumentListener(new DocumentListener(){
            public void removeUpdate(DocumentEvent e) {topText2.setText(textAdres.getText());}
            public void insertUpdate(DocumentEvent e) {topText2.setText(textAdres.getText());}
            public void changedUpdate(DocumentEvent e) {topText2.setText(textAdres.getText());}
        });
        
        suma.getDocument().addDocumentListener(new DocumentListener(){
            public void removeUpdate(DocumentEvent e) {topText1.setText(suma.getText());}
            public void insertUpdate(DocumentEvent e) {topText1.setText(suma.getText());}
            public void changedUpdate(DocumentEvent e) {topText1.setText(suma.getText());}
        });

        topText1.setEditable(false);
        topText2.setEditable(false);
        topLabel1.setFont(font);
        topText1.setFont(font);
        topLabel2.setFont(font);
        topText2.setFont(font);
        topPanel.add (topLabel1);
        topPanel.add (topText1);
        topPanel.add (topLabel2);
        topPanel.add (topText2);

        JPanel midPanel1 = new JPanel ();
        midPanel1.setLayout(new GridLayout (1,2,4,4));
        JLabel midLabel1 = new JLabel ("Uwagi: ");
        midLabel1.setFont(font);
        textUwagi.setFont(font);
        midPanel1.add (midLabel1);
        midPanel1.add (textUwagi);
        
        
        JPanel midPanel2 = new JPanel ();
        midPanel2.setLayout(new GridLayout (1,2,4,4));
        JLabel midLabel2 = new JLabel ("Płatność kartą");
        
        midLabel2.setFont(font);
        midPanel2.add (midLabel2);
        midPanel2.add (platnoscKarta);
        

        JLabel dumbLabel1 = new JLabel ("");
        JLabel dumbLabel2 = new JLabel ("");

        JPanel bottPanel = new JPanel ();
        bottPanel.setLayout(new FlowLayout ());
        JButton bottPanelButton1 = new JButton ("Zatwierdź zamówienie");
        bottPanel.add (bottPanelButton1);
        bottPanelButton1.addActionListener(event -> zatwierdzZamowienie ());

        podsumowaniePanel.add (topPanel);
        podsumowaniePanel.add (dumbLabel1);
        podsumowaniePanel.add (midPanel1);
        podsumowaniePanel.add (dumbLabel2);
        podsumowaniePanel.add (midPanel2);
        podsumowaniePanel.add (bottPanel);
    }

    private void wczytaj_produkty () {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream("produkty.txt");
            ois = new ObjectInputStream(fis);
            produktyWszystkie = (LinkedList<Produkt>) ois.readObject();
        } catch (FileNotFoundException err) {
            System.out.println("Nie znaleziono pliku 'produkty.txt.'");
        } catch (IOException err) {
            err.printStackTrace();
        } catch (ClassNotFoundException err) {
            err.printStackTrace();
        }

        int i = 0;
        comboStrings = new String [produktyWszystkie.size()];
        for (Produkt p : produktyWszystkie){
            comboStrings[i] = p.nazwa;
            i++;
        }
    }

    private void dodaj_produkt () {
        int idx = produktSelectList.getSelectedIndex();
        String dodawana_nazwa = produktyWszystkie.get(produktSelectList.getSelectedIndex()).nazwa;
        for (int i = 0; i < produktyDodane.size(); i++){
            if (produktyDodane.get(i).getP().nazwa.compareTo(dodawana_nazwa) == 0){
                produktyDodane.get(i).incI(Integer.parseInt(textIlosc.getText()));
                model.setValueAt(produktyDodane.get(i).getI(), i, 2);
                zamowienieUpdate();
                return;
            }
        }
        
        row[0] = produktyWszystkie.get(idx).nazwa;
        row[1] = produktyWszystkie.get(produktSelectList.getSelectedIndex()).cena;
        row[2] = textIlosc.getText();
        model.addRow(row);
    
        produktyDodane.add (new Pair (produktyWszystkie.get(idx),1));
        zamowienieUpdate();
    }

    private void usun_produkt () {
        int i = table.getSelectedRow();
        if (i >= 0) {
            produktyDodane.remove (i);
            model.removeRow(i);
        }
        zamowienieUpdate();
    }

    private void zamowienieUpdate () {
        zamowienie.adres = textAdres.getText();
        zamowienie.nr_tel = textNr.getText();
        zamowienie.uwagi = textUwagi.getText();
        zamowienie.produktList = produktyDodane;

        suma.setText( Double.toString(zamowienie.wartosc_zamowienia()) + " PLN");
    }

    private void zatwierdzZamowienie () {
        zamowienie = new Zamowienie(produktyDodane, 
                                    textAdres.getText(),
                                    textNr.getText(),
                                    platnoscKarta.isSelected(),
                                    new Date (), 
                                    textUwagi.getText(), 
                                    textEmail.getText());

        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream("zamowienia.txt");
            ois = new ObjectInputStream(fis);
            zamowieniaWszystkie = (LinkedList<Zamowienie>) ois.readObject();
        } catch (FileNotFoundException err) {
            JOptionPane.showMessageDialog(null, "Nie znaleziono pliku 'zamowienia.txt'", "Błąd", 0);
        } catch (IOException err) {
            err.printStackTrace();
        } catch (ClassNotFoundException err) {
            err.printStackTrace();
        }

        zamowieniaWszystkie.add (zamowienie);

        try {
            FileOutputStream fos = new FileOutputStream("zamowienia.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(zamowieniaWszystkie);
            oos.flush();
            oos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        
        System.out.println("Wszystkich zamówień jest: " + zamowieniaWszystkie.size());
        for (Zamowienie z : zamowieniaWszystkie){
            System.out.println(z);
            for (Pair p : z.produktList){
                System.out.println(p);
            }
        }
        System.exit(0);
    }
}