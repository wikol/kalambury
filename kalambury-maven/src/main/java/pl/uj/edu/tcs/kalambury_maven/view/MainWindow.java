package pl.uj.edu.tcs.kalambury_maven.view;

import java.awt.EventQueue;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import pl.uj.edu.tcs.kalambury_maven.controller.AppController;
import pl.uj.edu.tcs.kalambury_maven.controller.DrawingController;
import pl.uj.edu.tcs.kalambury_maven.model.ChatMessagesList;
import pl.uj.edu.tcs.kalambury_maven.model.DrawingModel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	private ChatBox chatBox;
	private Ranking ranking;
	private DrawingPanel drawingPanel;
	private BrushPanel brushPanel;

	/**
	 * For test purposes only
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow("test");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ChatBox getChatBox() {
		return chatBox;
	}

	public Ranking getRanking() {
		return ranking;
	}

	public DrawingPanel getDrawingPanel() {
		return drawingPanel;
	}

	public BrushPanel getBrushPanel() {
		return brushPanel;
	}

	public void setupChatBox(ChatMessagesList model, AppController controller) {
		chatBox.setModel(model);
		chatBox.setController(controller);
	}

	public void setupDrawingPanel(DrawingController controller,
			DrawingModel model) {
		drawingPanel.setController(controller);
		drawingPanel.setModel(model);
		brushPanel.setController(controller);
	}

	/**
	 * Create the frame.
	 */
	public MainWindow(String myNick) {
		super("Kalambury - " + myNick);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 988, 686);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("500px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("250px"), }, new RowSpec[] {
				RowSpec.decode("131px"), FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("300px"), FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("131px"), }));
		brushPanel = new BrushPanel();
		contentPane.add(brushPanel, "3, 1, fill, fill");
		drawingPanel = new DrawingPanel();
		contentPane.add(drawingPanel, "1, 1, 1, 3, fill, fill");
		ranking = new Ranking(new HashMap<String, Integer>());
		contentPane.add(ranking, "3, 3, 1, 3, fill, fill");
		chatBox = new ChatBox();
		contentPane.add(chatBox, "1, 5, fill, fill");
		pack();

	}

}
