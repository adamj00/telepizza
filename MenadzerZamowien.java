import java.util.LinkedList;
import javax.swing. * ;
import java.io. * ;
import java.awt. * ;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.table.DefaultTableModel;
@SuppressWarnings({
	"serial",
	"unchecked"
})
public class MenadzerZamowien extends JFrame {
	JPanel mainPanel = new JPanel(new BorderLayout());
	LinkedList < Zamowienie > listofOrders = new LinkedList < Zamowienie > ();
	JComboBox < String > dateComboBox;
	int tableMode = 0;

	public MenadzerZamowien() {
		String dateStrings[] = {
			"Wszystkie",
			"Dzisiejsze",
			"Z tego tygodnia",
			"Z tego miesiąca",
			"Z tego roku"
		};
		dateComboBox = new JComboBox < String > (dateStrings);
		dateComboBox.addActionListener(event -> refresh());
		add(mainPanel);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
		refresh();
	}

	void refresh() {
		remove(mainPanel);
		loadOrders();
		tableMode = dateComboBox.getSelectedIndex();
		mainPanel = makeMainPanel();
		add(mainPanel);
		invalidate();
		validate();
	}

	JPanel makeMainPanel() {
		JPanel resPanel = new JPanel(new BorderLayout());

		JButton refreshButton = new JButton("Odśwież");
		refreshButton.addActionListener(event -> refresh());

		resPanel.add(dateComboBox, BorderLayout.NORTH);
		resPanel.add(refreshButton, BorderLayout.SOUTH);

		JTable ordersTable = makeTable();
		resPanel.add(new JScrollPane(ordersTable), BorderLayout.CENTER);

		return resPanel;
	}

	JTable makeTable() {
		LinkedList < Zamowienie > listofFilterOrders = filterOrders();
		String columnNames[] = {
			"Data",
			"Produkty",
			"Adres",
			"Numer telefonu",
			"Płatność kartą",
			"Uwagi",
			"Wartość",
			"Czy gotowe"
		};
		Object ordersData[][] = new Object[listofFilterOrders.size()][8];
		for (int i = 0; i < listofFilterOrders.size(); i++) {
			Zamowienie z = listofFilterOrders.get(i);
			ordersData[i][0] = z.data_zamowienia;
			ordersData[i][1] = productsString(z.produktList);
			ordersData[i][2] = z.adres;
			ordersData[i][3] = z.nr_tel;
			if (z.platnosc_karta) ordersData[i][4] = "TAK";
			else ordersData[i][4] = "NIE";
			ordersData[i][5] = z.uwagi;
			ordersData[i][6] = z.wartosc_zamowienia();
			if (z.czyGotowe) ordersData[i][7] = "TAK";
			else ordersData[i][7] = "NIE";
		}
		JTable ordersTable = new JTable();
		DefaultTableModel tableModel = new DefaultTableModel(ordersData, columnNames) {@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		ordersTable.setModel(tableModel);
		return ordersTable;
	}

	void loadOrders() {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream("zamowienia.txt");
			ois = new ObjectInputStream(fis);
			listofOrders = (LinkedList < Zamowienie > ) ois.readObject();
		} catch(FileNotFoundException err) {
			System.out.println("Nie znaleziono pliku 'zamowienia.txt.'");
		} catch(IOException err) {
			err.printStackTrace();
		} catch(ClassNotFoundException err) {
			err.printStackTrace();
		}
	}
	String productsString(LinkedList < Pair > listofPairs) {
		String res = new String();
		for (Pair p: listofPairs) {
			res += p.toString() + "; ";
		}
		return res;
	}
	LinkedList < Zamowienie > filterOrders() {
		LinkedList < Zamowienie > myList = new LinkedList < Zamowienie > ();
		for (Zamowienie z: listofOrders) {
			Calendar calendar1 = new GregorianCalendar();
			Calendar calendar2 = new GregorianCalendar();
			calendar1.setTime(z.data_zamowienia);
			int year1 = calendar1.get(Calendar.YEAR);
			int month1 = calendar1.get(Calendar.MONTH) + 1;
			int day1 = calendar1.get(Calendar.DAY_OF_MONTH);
			int year2 = calendar2.get(Calendar.YEAR);
			int month2 = calendar2.get(Calendar.MONTH) + 1;
			int day2 = calendar2.get(Calendar.DAY_OF_MONTH);
			if (tableMode == 0) myList.add(z);
			else if (tableMode == 1) {
				if (day1 == day2 && year1 == year2 && month1 == month2) myList.add(z);
			}
			else if (tableMode == 3) {
				if (year1 == year2 && month1 == month2) myList.add(z);
			}
			else if (tableMode == 2) {
				if (sameWeek(calendar1, calendar2)) myList.add(z);
			}
			else if (tableMode == 4) {
				if (year1 == year2) myList.add(z);
			}
		}
		return myList;
	}
	Boolean sameWeek(Calendar c1, Calendar c2) {
		return (c1.get(Calendar.WEEK_OF_YEAR) == c2.get(Calendar.WEEK_OF_YEAR) && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR));
	}
}