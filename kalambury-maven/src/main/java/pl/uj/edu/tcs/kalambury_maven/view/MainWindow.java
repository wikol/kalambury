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
	private WhoIsDrawingInfo whoIsDrawingInfo;

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
	
	public WhoIsDrawingInfo getWhoIsDrawingInfo() {
		return whoIsDrawingInfo;
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
				ColumnSpec.decode("250px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("600px:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("100dlu:grow"),},
			new RowSpec[] {
				RowSpec.decode("131px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("100dlu"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("100dlu:grow"),
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("150px"),}));
		drawingPanel = new DrawingPanel();
		contentPane.add(drawingPanel, "2, 1, 2, 10, fill, fill");
				
				riddleAndTime = new TimerAndProgressBar();
				contentPane.add(riddleAndTime, "5, 1, fill, fill");
				brushPanel = new BrushPanel();
				contentPane.add(brushPanel, "1, 1, 1, 5, fill, fill");
						
						whoIsDrawingInfo = new WhoIsDrawingInfo((String) null);
						contentPane.add(whoIsDrawingInfo, "5, 3, fill, fill");
				
						
						ranking = new Ranking(new HashMap<String, Integer>());
						contentPane.add(ranking, "5, 5, 1, 5, fill, top");
						
						
						panel = new JPanel();
						GridBagConstraints gbc_panel = new GridBagConstraints();
						gbc_panel.insets = new Insets(0, 0, 5, 5);
						gbc_panel.fill = GridBagConstraints.BOTH;
						gbc_panel.gridx = 1;
						gbc_panel.gridy = 1;
						ranking.add(panel, gbc_panel);
		
		clearButton = new ClearButton();
		contentPane.add(clearButton, "1, 7, fill, fill");
		chatBox = new ChatBox();
		contentPane.add(chatBox, "3, 11, fill, fill");
		pack();

	} 

}
