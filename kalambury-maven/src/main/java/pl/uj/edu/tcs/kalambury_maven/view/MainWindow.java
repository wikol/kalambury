package pl.uj.edu.tcs.kalambury_maven.view;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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


	private static final long serialVersionUID = -5853323500541154221L;
	private JPanel contentPane;
	private ChatBox chatBox;
	private Ranking ranking;
	private DrawingPanel drawingPanel;
	private BrushPanel brushPanel;
	private JPanel panel;
	private ClearButton clearButton;
	private TimerAndProgressBar riddleAndTime;

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
	
	public TimerAndProgressBar getTimer() {
		return riddleAndTime;
	}

	public ClearButton getClearButton(){
		return clearButton;
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

	public void setupClearButton(DrawingController controller){
		clearButton.setController(controller);
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
				ColumnSpec.decode("250px:grow"),},
			new RowSpec[] {
				RowSpec.decode("131px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("300px"),
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("131px"),}));
		brushPanel = new BrushPanel();
		contentPane.add(brushPanel, "3, 1, fill, fill");
		drawingPanel = new DrawingPanel();
		contentPane.add(drawingPanel, "1, 1, 1, 9, fill, fill");
		
		clearButton = new ClearButton();
		contentPane.add(clearButton, "3, 3, fill, fill");
		
		riddleAndTime = new TimerAndProgressBar();
		contentPane.add(riddleAndTime, "3, 5, fill, fill");

		
		ranking = new Ranking(new HashMap<String, Integer>());
		GridBagLayout gridBagLayout = (GridBagLayout) ranking.getLayout();
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0};
		contentPane.add(ranking, "3, 9, 1, 3, fill, fill");
		
		
		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 1;
		ranking.add(panel, gbc_panel);
		chatBox = new ChatBox();
		contentPane.add(chatBox, "1, 11, fill, fill");
		pack();

	} 

}
