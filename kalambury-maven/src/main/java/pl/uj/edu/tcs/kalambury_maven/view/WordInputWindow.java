package pl.uj.edu.tcs.kalambury_maven.view;

import java.awt.ComponentOrientation;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pl.uj.edu.tcs.kalambury_maven.controller.AppController;
import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventHandler;
import pl.uj.edu.tcs.kalambury_maven.event.NewWordForGuessingEvent;
import pl.uj.edu.tcs.kalambury_maven.event.CloseWordInputEvent;

public class WordInputWindow extends JFrame implements EventHandler {

	private static final long serialVersionUID = -2416791690369189983L;
	private AppController controller;
	private JPanel contentPane;
	private JLabel lblEnterWord;
	private JLabel lblSending;
//	private JLabel lblSendingFailed;
	private JTextField txtInputWord;
	private JButton btnOk;

	private boolean sendingInProgress;

	private String strSending = "Sending...";
//	private String strSendingFailed = "Sending failed, please try once again";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WordInputWindow frame = new WordInputWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private class WordToSendListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (sendingInProgress)
				return;
			String word = WordInputWindow.this.txtInputWord.getText().trim();
			if (word.equals(""))
				return;
			NewWordForGuessingEvent event = new NewWordForGuessingEvent(word);
			lblSending.setText(strSending);
			lblSending.setVisible(true);
			sendingInProgress = true;
			controller.reactTo(event);
		}

	}

	/**
	 * Create the frame.
	 */
	public WordInputWindow() {
		super("Kalambury - word to draw");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);

		GridBagLayout gbl_contentPane = new GridBagLayout();
		contentPane = new JPanel(gbl_contentPane);
		contentPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);

		lblEnterWord = new JLabel("Please, enter word you want to draw:");
		GridBagConstraints gbc_lblEnterWord = new GridBagConstraints();
		gbc_lblEnterWord.gridwidth = 3;
		gbc_lblEnterWord.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblEnterWord.insets = new Insets(0, 0, 0, 0);
		gbc_lblEnterWord.gridx = 0;
		gbc_lblEnterWord.gridy = 0;
		contentPane.add(lblEnterWord, gbc_lblEnterWord);

		txtInputWord = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.gridwidth = 3;
		gbc_textField.insets = new Insets(0, 0, 0, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 2;
		txtInputWord.setColumns(10);
		txtInputWord.addActionListener(new WordToSendListener());
		contentPane.add(txtInputWord, gbc_textField);

		btnOk = new JButton("OK");
		GridBagConstraints gbc_btnOk = new GridBagConstraints();
		gbc_btnOk.anchor = GridBagConstraints.EAST;
		gbc_btnOk.gridx = 0;
		gbc_btnOk.gridy = 3;
		btnOk.addActionListener(new WordToSendListener());
		contentPane.add(btnOk, gbc_btnOk);

		lblSending = new JLabel(strSending);
		GridBagConstraints gbc_lblSending = new GridBagConstraints();
		gbc_lblSending.gridx = 2;
		gbc_lblSending.gridy = 3;
		Font font = lblSending.getFont();
		font = font.deriveFont(font.getStyle() & ~Font.BOLD);
		Float fontSize = font.getSize2D();
		fontSize -= 2.0f;
		font = font.deriveFont(fontSize);
		lblSending.setFont(font);
		lblSending.setVisible(false);
		contentPane.add(lblSending, gbc_lblSending);

		pack();
		setResizable(false);

		sendingInProgress = false;
	}

	public void setController(AppController contr) {
		this.controller = contr;
	}

	@Override
	public void handle(Event e) {
		sendingInProgress = false;
		if (e instanceof CloseWordInputEvent) {
			dispose();
		}
		/*
		 * if (serwer timeout) { lblSending.setText(strSendingFailed); }
		 */
	}
}
