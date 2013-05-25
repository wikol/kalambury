package pl.uj.edu.tcs.kalambury_maven.view;

import java.awt.ComponentOrientation;
import java.awt.EventQueue;
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

public class WordInputWindow extends JFrame {
	private JPanel contentPane;
	private JLabel lblEnterWord;
	private JTextField txtInputWord;
	private JButton btnOk;

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
			String word = WordInputWindow.this.txtInputWord.getText().trim();
			if (word.equals("")) return;
			
		}
		
	}
	
	
	/**
	 * Create the frame.
	 */
	public WordInputWindow() {
		super("Kalambury - word to draw");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		contentPane.add(txtInputWord, gbc_textField);
		txtInputWord.setColumns(10);
		
		btnOk = new JButton("OK");
		GridBagConstraints gbc_btnOk = new GridBagConstraints();
		gbc_btnOk.anchor = GridBagConstraints.EAST;
		gbc_btnOk.gridx = 2;
		gbc_btnOk.gridy = 3;
		contentPane.add(btnOk, gbc_btnOk);
		
		pack();
		setResizable(false);
	}

}
