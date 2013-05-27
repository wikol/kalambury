package pl.uj.edu.tcs.kalambury_maven.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventHandler;
import pl.uj.edu.tcs.kalambury_maven.event.EventNotHandledException;
import pl.uj.edu.tcs.kalambury_maven.event.LoginAttemptEvent;
import pl.uj.edu.tcs.kalambury_maven.event.LoginUnsuccessfulEvent;

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
public class LoginWindow extends JFrame implements EventHandler {

	private static final long serialVersionUID = -3042477817267367998L;
	private JPanel contentPane;
	private JLabel serverLabel;
	private JLabel portLabel;
	private JLabel loginLabel;
	private JTextField serverField;
	private JTextField portField;
	private JTextField loginField;
	private JButton loginButton;
	private final AppView view;
	private JLabel errorLabel;

	/**
	 * For testing puropses only
	 */
	/*
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { LoginWindow frame = new
	 * LoginWindow(null); frame.setVisible(true); } catch (Exception e) {
	 * e.printStackTrace(); } } }); }
	 */

	private void displayError(String msg) {
		errorLabel.setText(msg);
	}

	/**
	 * Create the frame.
	 */
	public LoginWindow(AppView view) {
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
		portField.setText("8888");
		serverField = new JTextField();
		serverField.setText("localhost");
		contentPane.add(serverField, "4, 2, fill, fill");
		contentPane.add(portLabel, "6, 2, left, center");
		contentPane.add(portField, "8, 2, fill, fill");

		loginLabel = new JLabel("Login:");
		contentPane.add(loginLabel, "2, 4, left, center");
		loginField = new JTextField();
		loginField.setText("test");
		contentPane.add(loginField, "4, 4, fill, fill");

		loginButton = new JButton("Login");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (LoginWindow.this.view.getController() == null)
					System.out.println("WTF?!");
				new Thread() {
					@Override
					public void run() {
						LoginWindow.this.view.getController().reactTo(
								new LoginAttemptEvent(serverField.getText(),
										portField.getText(), loginField
												.getText()));
					}
				}.start();
			}
		});
		contentPane.add(loginButton, "6, 4, 3, 1, fill, fill");

		errorLabel = new JLabel(" ");
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		errorLabel.setForeground(Color.RED);
		contentPane.add(errorLabel, "2, 6, 7, 1");

		pack();
		setResizable(false);
	}

	@Override
	public void handle(Event e) {
		displayError(((LoginUnsuccessfulEvent) e).getMessage());
	}
}
