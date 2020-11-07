import java.util.LinkedList;
import javax.swing. * ;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.io. * ;
import java.awt. * ;
import java.util.Date;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings({
	"serial",
	"unchecked"
})
public class WprowadzanieZamowienia extends JFrame {
	LinkedList < Pair > choosedProductsList = new LinkedList < Pair > ();
	LinkedList < Produkt > allProductsList = new LinkedList < Produkt > ();

	JTabbedPane mainTabbedPane = new JTabbedPane();

	Zamowienie myOrder = new Zamowienie();

	JTextField adressText = new JTextField();
	JTextField telText = new JTextField();
	JTextField notesText = new JTextField("Brak uwag");
	JCheckBox cardBox = new JCheckBox();

	Font font1 = new Font("Monaco", Font.PLAIN, 20);
	Font font2 = new Font("Monaco", Font.PLAIN, 30);
	int lastSelectedIdx = 0;

	public WprowadzanieZamowienia() {
		adressText.setFont(font1);
		telText.setFont(font1);
		notesText.setFont(font1);
		cardBox.setFont(font1);

		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		refresh();
	}

	void refresh() {

		remove(mainTabbedPane);

		allProductsList = loadProductsFromFile();

		refreshOrderData();

		JPanel tab1 = makeTab1();
		JPanel tab2 = makeTab2();
		JPanel tab3 = makeTab3();

		mainTabbedPane = new JTabbedPane();
		mainTabbedPane.add("Zamówienie", tab1);
		mainTabbedPane.add("Dane dostawy", tab2);
		mainTabbedPane.add("Podsumowanie", tab3);

		add(mainTabbedPane);
		invalidate();
		validate();

	}

	void refreshOrderData() {
		myOrder.produktList = choosedProductsList;
		myOrder.adres = adressText.getText();
		myOrder.nr_tel = telText.getText();
		myOrder.platnosc_karta = cardBox.isSelected();
		myOrder.uwagi = notesText.getText();
	}

	JPanel makeTab1() {
		JPanel mainOrderPanel = new JPanel(new BorderLayout());

		JTable productsTable = makeProductsTable();
		mainOrderPanel.add(new JScrollPane(productsTable), BorderLayout.CENTER);

		JPanel orderBottomPanel = new JPanel(new GridLayout(1, 4, 2, 2));

		LinkedList < Produkt > listofProducts = loadProductsFromFile();
		LinkedList < String > namesofProducts = makeListofProductsNames(listofProducts);
		String arrofNames[] = new String[namesofProducts.size()];
		namesofProducts.toArray(arrofNames);
		JComboBox < String > productsComboBox = new JComboBox < String > (arrofNames);

		productsComboBox.setSelectedIndex(lastSelectedIdx);
		productsComboBox.addActionListener(event -> comboBoxAction(productsComboBox.getSelectedIndex()));

		JButton addButton = new JButton("Dodaj");
		addButton.addActionListener(event -> addAction(productsComboBox.getSelectedItem().toString()));

		JButton deleteButton = new JButton("Usuń");
		deleteButton.addActionListener(event -> deleteAction(productsTable.getSelectedRow()));

		JTextField totalTextField = new JTextField(Double.toString(myOrder.wartosc_zamowienia()) + " PLN");
		totalTextField.setFont(font1);
		totalTextField.setEditable(false);

		orderBottomPanel.add(productsComboBox);
		orderBottomPanel.add(addButton);
		orderBottomPanel.add(deleteButton);
		orderBottomPanel.add(totalTextField);

		mainOrderPanel.add(orderBottomPanel, BorderLayout.SOUTH);

		return mainOrderPanel;
	}

	JPanel makeTab2() {
		JPanel deliveryPanel = new JPanel(new GridLayout(3, 2, 1, 1));

		JLabel adressLabel = new JLabel("Adres dostawy");
		JLabel telLabel = new JLabel("Numer telefonu");
		JLabel cardLabel = new JLabel("Płatność kartą");

		adressLabel.setFont(font1);
		telLabel.setFont(font1);
		cardLabel.setFont(font1);

		deliveryPanel.add(adressLabel);
		deliveryPanel.add(adressText);
		deliveryPanel.add(telLabel);
		deliveryPanel.add(telText);
		deliveryPanel.add(cardLabel);
		deliveryPanel.add(cardBox);

		return deliveryPanel;
	}

	JPanel makeTab3() {
		JPanel summaryPanel = new JPanel(new BorderLayout());
		JPanel topPanel = new JPanel(new GridLayout(2, 2, 2, 2));

		JLabel totaLabel = new JLabel("Do zapłaty:");
		totaLabel.setFont(font1);
		JTextField totalText = new JTextField(Double.toString(myOrder.wartosc_zamowienia()) + " PLN");
		totalText.setFont(font2);

		totalText.setEditable(false);

		JLabel notesLabel = new JLabel("Uwagi do zamówienia:");
		notesLabel.setFont(font1);

		JButton confirmButton = new JButton("Zatwierdź zamówienie");
		confirmButton.addActionListener(event -> confirmAndSaveOrder());

		topPanel.add(totaLabel);
		topPanel.add(totalText);
		topPanel.add(notesLabel);
		topPanel.add(notesText);

		summaryPanel.add(topPanel, BorderLayout.CENTER);
		summaryPanel.add(confirmButton, BorderLayout.SOUTH);

		return summaryPanel;
	}

	JTable makeProductsTable() {
		String[] columnNames = {
			"Nazwa",
			"Cena",
			"Ilość"
		};
		Object tableData[][] = new Object[choosedProductsList.size()][3];

		for (int i = 0; i < choosedProductsList.size(); i++) {
			tableData[i][0] = choosedProductsList.get(i).getP().nazwa;
			tableData[i][1] = Double.toString(choosedProductsList.get(i).getP().cena);
			tableData[i][2] = Integer.toString(choosedProductsList.get(i).getI());
		}

		JTable productsTable = new JTable();

		DefaultTableModel tableModel = new DefaultTableModel(tableData, columnNames) {@Override
			public boolean isCellEditable(int row, int column) {
				return (column == 2);
			}
		};

		tableModel.addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent e) {
				choosedProductsList.get(e.getFirstRow()).setI(Integer.parseInt(tableModel.getValueAt(e.getFirstRow(), 2).toString()));
				refresh();
			}
		});

		productsTable.setModel(tableModel);

		productsTable.setFont(font1);

		productsTable.setRowHeight(40);

		return productsTable;
	}

	LinkedList < Produkt > loadProductsFromFile() {
		LinkedList < Produkt > productsList = new LinkedList < Produkt > ();
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream("produkty.txt");
			ois = new ObjectInputStream(fis);
			productsList = (LinkedList < Produkt > ) ois.readObject();
		} catch(FileNotFoundException err) {
			System.out.println("Nie znaleziono pliku 'produkty.txt.'");
		} catch(IOException err) {
			err.printStackTrace();
		} catch(ClassNotFoundException err) {
			err.printStackTrace();
		}
		return productsList;
	}

	LinkedList < String > makeListofProductsNames(LinkedList < Produkt > listofProducts) {
		LinkedList < String > listofNames = new LinkedList < String > ();
		for (Produkt p: listofProducts) {
			listofNames.add(p.nazwa);
		}
		return listofNames;
	}

	Produkt searchProductByName(String name) {
		for (Produkt p: allProductsList) {
			if (p.nazwa.compareTo(name) == 0) return p;
		}
		return null;
	}

	void addAction(String name) {
		for (Pair p: choosedProductsList) {
			if (p.getP().nazwa.compareTo(name) == 0) {
				p.incI(1);
				refresh();
				return;
			}
		}
		choosedProductsList.add(new Pair(searchProductByName(name), 1));
		refresh();
		return;
	}

	void deleteAction(int idx) {
		if (idx < 0) return;
		else {
			choosedProductsList.remove(idx);
			refresh();
			return;
		}
	}

	void comboBoxAction(int idx) {
		lastSelectedIdx = idx;
		return;
	}

	void confirmAndSaveOrder() {
		refreshOrderData();
		myOrder.data_zamowienia = new Date();
		LinkedList < Zamowienie > allOrders = new LinkedList < Zamowienie > ();
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream("zamowienia.txt");
			ois = new ObjectInputStream(fis);
			allOrders = (LinkedList < Zamowienie > ) ois.readObject();
		} catch(FileNotFoundException err) {
			JOptionPane.showMessageDialog(null, "Nie znaleziono pliku 'zamowienia.txt'", "Błąd", 0);
		} catch(IOException err) {
			err.printStackTrace();
		} catch(ClassNotFoundException err) {
			err.printStackTrace();
		}

		allOrders.add(myOrder);

		try {
			FileOutputStream fos = new FileOutputStream("zamowienia.txt");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(allOrders);
			oos.flush();
			oos.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		dispose();
	}
}