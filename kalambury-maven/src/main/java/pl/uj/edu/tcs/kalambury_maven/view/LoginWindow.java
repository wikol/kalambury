package pl.uj.edu.tcs.kalambury_maven.view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import pl.uj.edu.tcs.kalambury_maven.event.EventNotHandledException;
import pl.uj.edu.tcs.kalambury_maven.event.LoginAttemptEvent;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Login window
 * 
 * @author Wiktor Kuropatwa
 * 
 */
public class LoginWindow extends JFrame {

	private static final long serialVersionUID = -3042477817267367998L;
	private JPanel contentPane;
	private JLabel serverLabel;
	private JLabel portLabel;
	private JLabel loginLabel;
	private JTextField serverField;
	private JTextField portField;
	private JTextField loginField;
	private JButton loginButton;
	private final View view;

	/**
	 * For testing puropses only
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginWindow frame = new LoginWindow(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginWindow(View view) {
		super("Kalambury - login");
		this.view = view;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		contentPane
				.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("51px"),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						ColumnSpec.decode("169px"),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						ColumnSpec.decode("35px"),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						ColumnSpec.decode("59px"),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC, },
						new RowSpec[] { FormFactory.UNRELATED_GAP_ROWSPEC,
								RowSpec.decode("25px"),
								FormFactory.RELATED_GAP_ROWSPEC,
								FormFactory.DEFAULT_ROWSPEC,
								FormFactory.RELATED_GAP_ROWSPEC,
								FormFactory.DEFAULT_ROWSPEC, }));
		serverLabel = new JLabel("Server:");
		contentPane.add(serverLabel, "2, 2, left, center");
		portLabel = new JLabel("Port:");
		portField = new JTextField();
		serverField = new JTextField();
		contentPane.add(serverField, "4, 2, fill, fill");
		contentPane.add(portLabel, "6, 2, left, center");
		contentPane.add(portField, "8, 2, fill, fill");

		loginLabel = new JLabel("Login:");
		contentPane.add(loginLabel, "2, 4, left, center");
		loginField = new JTextField();
		contentPane.add(loginField, "4, 4, fill, fill");

		loginButton = new JButton("Login");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					LoginWindow.this.view.getController().reactTo(
							new LoginAttemptEvent(serverField.getText(),
									portField.getText(), loginField.getText()));
				} catch (EventNotHandledException exc) {
					exc.printStackTrace();
					System.exit(1);
				}
			}
		});
		contentPane.add(loginButton, "6, 4, 3, 1, fill, fill");

		pack();
		setResizable(false);
	}
}
