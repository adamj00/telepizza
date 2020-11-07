import javax.swing. * ;
import javax.swing.border.Border;
import java.awt. * ;
import java.io. * ;
import java.util.LinkedList;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

@SuppressWarnings({
	"serial",
	"unchecked"
})
public class PanelKuchni extends JFrame {

	private JPanel panelsContainer = makeContainer();
	private Dimension preferredPanelSize = new Dimension(600, 300);
	private Dimension maxPanelSize = new Dimension(Integer.MAX_VALUE, 400);
	private Border myBorder = BorderFactory.createEmptyBorder(20, 0, 0, 0);
	private LinkedList < Zamowienie > ordersList = new LinkedList < Zamowienie > ();
	private LinkedList < Zamowienie > allOrdersList = new LinkedList < Zamowienie > ();

	private JPanel makeContainer() {
		JPanel myContainer = new JPanel();
		myContainer.setLayout(new BoxLayout(myContainer, BoxLayout.Y_AXIS));
		return myContainer;
	}

	public PanelKuchni() {
		setFocusable(true);
		addKeyListener(createKeyListener());
		init();
	}

	private void init() {
		panelsContainer.removeAll();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		JScrollPane scrollPane = new JScrollPane(panelsContainer);
		scrollPane.setPreferredSize(new Dimension(900, 800));

		setContentPane(scrollPane);
		setVisible(true);
		loadAndFilterOrders();
		addAllOrders();
		repaint();
	}

	private void loadAndFilterOrders() {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream("zamowienia.txt");
			ois = new ObjectInputStream(fis);
			allOrdersList = (LinkedList < Zamowienie > ) ois.readObject();
		} catch(FileNotFoundException err) {
			System.out.println("Nie znaleziono pliku 'zamowienia.txt.'");
		} catch(IOException err) {
			err.printStackTrace();
		} catch(ClassNotFoundException err) {
			err.printStackTrace();
		}

		ordersList.clear();
		for (Zamowienie z: allOrdersList) {
			if (!z.czyGotowe) ordersList.add(z);
		}
	}

	private void addPanel(JPanel panel) {
		panel.setPreferredSize(preferredPanelSize);
		panel.setMaximumSize(maxPanelSize);
		panel.setBorder(myBorder);
		panelsContainer.add(panel);
	}

	private void addAllOrders() {
		JPanel p;
		for (Zamowienie z: ordersList) {
			p = makePanel(z);
			addPanel(p);
		}
	}

	private JPanel makePanel(Zamowienie zamowienie) {

		JPanel resPanel = new JPanel();
		resPanel.setLayout(new BorderLayout());

		JButton confirmButton = new JButton("Zatwierdź zamówienie");
		confirmButton.addActionListener(event -> confirmOrder(zamowienie));
		JLabel dateLabel = new JLabel(zamowienie.data_zamowienia.toString());

		JTextField adressTextField = new JTextField(zamowienie.adres);
		JTextField telTextField = new JTextField(zamowienie.nr_tel);
		JTextField commentsTextField = new JTextField(zamowienie.uwagi);

		adressTextField.setEditable(false);
		telTextField.setEditable(false);
		commentsTextField.setEditable(false);

		JPanel insideCommentPanel = new JPanel();
		insideCommentPanel.setLayout(new BoxLayout(insideCommentPanel, BoxLayout.Y_AXIS));
		insideCommentPanel.add(new JScrollPane(dateLabel));
		insideCommentPanel.add(new JScrollPane(adressTextField));
		insideCommentPanel.add(new JScrollPane(telTextField));
		insideCommentPanel.add(new JScrollPane(commentsTextField));
		insideCommentPanel.add(new JScrollPane(confirmButton));

		String columnNames[] = {
			"Nazwa",
			"Ilość"
		};
		String tableData[][] = new String[zamowienie.produktList.size()][2];
		int i = 0;
		for (Pair p: zamowienie.produktList) {
			tableData[i][0] = p.getP().nazwa;
			tableData[i][1] = Integer.toString(p.getI());
			i++;
		}

		JTable orderTable = new JTable();
		DefaultTableModel tableModel = new DefaultTableModel(tableData, columnNames) {@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		orderTable.setRowHeight(30);
		orderTable.setFont(new Font("Monaco", Font.PLAIN, 20));
		orderTable.setModel(tableModel);

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(1, 3, 1, 1));
		centerPanel.add(new JScrollPane(orderTable));
		centerPanel.add(insideCommentPanel);

		resPanel.add(centerPanel, BorderLayout.CENTER);
		return resPanel;
	}

	private void confirmOrder(Zamowienie z) {
		allOrdersList.get(allOrdersList.indexOf(z)).czyGotowe = true;
		try {
			FileOutputStream fos = new FileOutputStream("zamowienia.txt");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(allOrdersList);
			oos.flush();
			oos.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		init();
	}
	private KeyListener createKeyListener() {
		KeyAdapter keyListener = new KeyAdapter() {@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_F5) {
					init();
				}
			}
		};
		return keyListener;
	}
}