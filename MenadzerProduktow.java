import javax.swing. * ;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt. * ;
import java.util.LinkedList;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io. * ;
@SuppressWarnings({
	"serial",
	"unchecked"
})

public class MenadzerProduktow extends JFrame {

	private LinkedList < Produkt > productList = new LinkedList < Produkt > ();
	private JPanel mainPanel = new JPanel(new BorderLayout());
	private JTable productsTable = new JTable();
	public MenadzerProduktow() {
		loadProducts();
		init();
	}
	private void init() {
		mainPanel.removeAll();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		makeTable();
		mainPanel.add(new JScrollPane(productsTable), BorderLayout.CENTER);
		mainPanel.add(makeSouthPanel(), BorderLayout.SOUTH);
		add(mainPanel);
		setVisible(true);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		repaint();
	}

	private JPanel makeSouthPanel() {
		JPanel southPanel = new JPanel(new GridLayout(2, 1));

		JPanel textFieldsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		JLabel nameLabel = new JLabel("Nazwa:");
		JTextField nameTextField = new JTextField(10);
		JLabel priceLabel = new JLabel("Cena:");
		JTextField priceTextField = new JTextField(10);

		textFieldsPanel.add(nameLabel);
		textFieldsPanel.add(nameTextField);
		textFieldsPanel.add(priceLabel);
		textFieldsPanel.add(priceTextField);

		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		JButton addButton = new JButton("Dodaj");
		addButton.addActionListener(event -> addProduct(nameTextField.getText(), Double.parseDouble(priceTextField.getText())));
		JButton deleteButton = new JButton("Usuń");
		deleteButton.addActionListener(event -> deleteProduct(productsTable.getSelectedRow()));
		JButton exitButton = new JButton("Zapisz i wyjdź");
		exitButton.addActionListener(event -> saveProducts());

		buttonsPanel.add(addButton);
		buttonsPanel.add(deleteButton);
		buttonsPanel.add(exitButton);

		southPanel.add(textFieldsPanel);
		southPanel.add(buttonsPanel);

		return southPanel;
	}

	private void makeTable() {
		productsTable = new JTable();
		String columnNames[] = {
			"Nazwa",
			"Cena"
		};
		Object productsData[][] = new Object[productList.size()][2];

		for (int i = 0; i < productList.size(); i++) {
			productsData[i][0] = productList.get(i).nazwa;
			productsData[i][1] = Double.toString(productList.get(i).cena);
		}

		DefaultTableModel tableModel = new DefaultTableModel(productsData, columnNames);
		tableModel.addTableModelListener(new TableModelListener() {@Override
			public void tableChanged(TableModelEvent e) {
				int c = e.getColumn();
				int r = e.getFirstRow();
				if (c == 0) {
					productList.get(r).nazwa = tableModel.getValueAt(r, c).toString();
				}
				else if (c == 1) {
					productList.get(r).cena = Double.parseDouble(tableModel.getValueAt(r, c).toString());
				}
				init();
			}
		});

		productsTable.setModel(tableModel);
		productsTable.setRowHeight(40);

	}

	private void loadProducts() {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream("produkty.txt");
			ois = new ObjectInputStream(fis);
			productList = (LinkedList < Produkt > ) ois.readObject();
		} catch(FileNotFoundException err) {
			System.out.println("Nie znaleziono pliku 'produkty.txt.'");
		} catch(IOException err) {
			err.printStackTrace();
		} catch(ClassNotFoundException err) {
			err.printStackTrace();
		}
	}

	private void saveProducts() {
		try {
			FileOutputStream fos = new FileOutputStream("produkty.txt");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(productList);
			oos.flush();
			oos.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		dispose();
	}

	private void addProduct(String name, double price) {
		productList.add(new Produkt(name, price));
		init();
	}

	private void deleteProduct(int idx) {
		if (idx >= 0) {
			productList.remove(idx);
			init();
		}
	}

}