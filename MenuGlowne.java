import javax.swing. * ;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt. * ;

@SuppressWarnings({
	"serial"
})
public class MenuGlowne extends JFrame {
	Font font = new Font("Monaco", Font.PLAIN, 30);
	public MenuGlowne() {
		init();
	}
	private void init() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.add(makeLeftPanel());
		mainPanel.add(makeRightPanel());
		add(mainPanel);
		setVisible(true);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	JPanel makeLeftPanel() {
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(4, 1));

		JButton makeOrderButton = new JButton("Wprowadzanie zamówienia");
		JButton kitchenButton = new JButton("Panel kuchni");
		JButton orderMenButton = new JButton("Menadżer zamówień");
		JButton productsButton = new JButton("Menadżer produktów");

		makeOrderButton.setFont(font);
		kitchenButton.setFont(font);
		orderMenButton.setFont(font);
		productsButton.setFont(font);

		makeOrderButton.addActionListener(event -> actionListener(0));
		kitchenButton.addActionListener(event -> actionListener(1));
		orderMenButton.addActionListener(event -> actionListener(2));
		productsButton.addActionListener(event -> actionListener(3));

		leftPanel.add(makeOrderButton);
		leftPanel.add(kitchenButton);
		leftPanel.add(orderMenButton);
		leftPanel.add(productsButton);

		return leftPanel;
	}
	JPanel makeRightPanel() {
		// Obrazek: http://www.freepik.com
		JPanel rightPanel = new JPanel();

		BufferedImage myPicture;
		try {
			myPicture = ImageIO.read(new File("image.jpg"));
			JLabel picLabel = new JLabel(new ImageIcon(myPicture));
			rightPanel.add(picLabel);
		} catch(IOException ex) {
			System.out.println("Nie udało się wczytać obrazka");
		}
		return rightPanel;
	}
	private void actionListener(int n) {
		if (n == 0) {
			new WprowadzanieZamowienia();
		}
		else if (n == 1) {
			new PanelKuchni();
		}
		else if (n == 2) {
			new MenadzerZamowien();
		}
		else if (n == 3) {
			new MenadzerProduktow();
		}
		else {
			throw new RuntimeException();
		}
	}
}