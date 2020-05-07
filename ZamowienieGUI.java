import java.awt.Color;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
public class ZamowienieGUI {
    String [] comboStrings;
    
    JFrame frame = new JFrame ();
    JTable table = new JTable () ;
    
    JComboBox produktSelectList;
    JTextField textIlosc = new JTextField();

    JButton dodajButton = new JButton ("Dodaj");
    JButton usunButton = new JButton ("Usuń");
    JButton odswiezButton = new JButton ("Odśwież");

    LinkedList<Pair> produktyDodane = new LinkedList<Pair>();
    LinkedList<Produkt> produktyWszystkie = new LinkedList<Produkt>();

    Object [] row = new Object [3]; 
    DefaultTableModel model = new DefaultTableModel();
    public ZamowienieGUI () {
        wczytaj_produkty();
        textIlosc.setText("1");
        produktSelectList = new JComboBox(comboStrings);

        Object [] columns = {"Nazwa","Cena","Ilość"};
        
        model.setColumnIdentifiers(columns);
        table.setModel (model);

       

        //table.setBackground(Color.cyan);
        //table.setForeground(Color.white);
        table.setRowHeight(30);
        
        produktSelectList.setBounds (20,220,100,25);
        //textNazwa.setBounds(20,220,100,25);
      //  textCena.setBounds(20,250,100,25);
        textIlosc.setBounds(20,280,100,25);

        dodajButton.setBounds (150,220,100,25);
        odswiezButton.setBounds (150,265,100,25);
        usunButton.setBounds (150,310,100,25);

        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(0,0,880,200);

        frame.setLayout(null);

        dodajButton.addActionListener(event -> dodaj_produkt());
        usunButton.addActionListener(event -> usun_produkt());
        
        frame.add (pane);

       frame.add (produktSelectList);
        // frame.add (textNazwa);
       // frame.add (textCena);
        frame.add (textIlosc);

        frame.add (dodajButton);
        frame.add (odswiezButton);
        frame.add (usunButton);

        frame.setSize(900,400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
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
                return;
            }
        }
        
        row [0] = produktyWszystkie.get(idx).nazwa;
        row[1] = produktyWszystkie.get(produktSelectList.getSelectedIndex()).cena;
        row[2] = textIlosc.getText();
        model.addRow(row);
    
        produktyDodane.add (new Pair (produktyWszystkie.get(idx),1));
    }

    private void usun_produkt () {
        int i = table.getSelectedRow();
        if (i >= 0) {
            produktyDodane.remove (i);
            model.removeRow(i);
        }
    }

}