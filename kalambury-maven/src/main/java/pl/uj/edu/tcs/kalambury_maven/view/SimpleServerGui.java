package pl.uj.edu.tcs.kalambury_maven.view;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import pl.uj.edu.tcs.kalambury_maven.event.NewGameEvent;
import pl.uj.edu.tcs.kalambury_maven.server.GameLogic;
import pl.uj.edu.tcs.kalambury_maven.server.Options;

public class SimpleServerGui extends JFrame {

	private JPanel contentPane;
	private GameLogic gameLogic;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SimpleServerGui frame = new SimpleServerGui(null);
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
	public SimpleServerGui(GameLogic gl) {
		this.gameLogic = gl;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 200, 150);
		contentPane = new JPanel();
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, };
		gbl_contentPane.columnWeights = new double[]{0.0};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0,  Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JButton btnStartGame = new JButton("Start game");
		GridBagConstraints gbc_btnStartGame = new GridBagConstraints();
		gbc_btnStartGame.insets = new Insets(0, 0, 5, 0);
		gbc_btnStartGame.gridx = 0;
		gbc_btnStartGame.gridy = 0;
		btnStartGame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				gameLogic.reactTo(null, new NewGameEvent());
			}
		});
		contentPane.add(btnStartGame, gbc_btnStartGame);
		
		JButton btnSetRoundTime = new JButton("Set round time");
		GridBagConstraints gbc_btnSetRoundTime = new GridBagConstraints();
		gbc_btnSetRoundTime.insets = new Insets(0, 0, 5, 0);
		gbc_btnSetRoundTime.gridx = 0;
		gbc_btnSetRoundTime.gridy = 2;
		btnSetRoundTime.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				long roundTime = 1000*Integer.parseInt(JOptionPane.showInputDialog("Input round time in seconds:"));
				Options.getInstance().setRoundTimeInMilisec(roundTime);
			}
		});
		contentPane.add(btnSetRoundTime, gbc_btnSetRoundTime);
	}
	
	public void setGameLogic(GameLogic gl) {
		this.gameLogic = gl;
	}
}
