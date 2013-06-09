package pl.uj.edu.tcs.kalambury_maven.view;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import pl.uj.edu.tcs.kalambury_maven.event.NewGameEvent;
import pl.uj.edu.tcs.kalambury_maven.server.GameLogic;
import pl.uj.edu.tcs.kalambury_maven.server.Options;
import pl.uj.edu.tcs.kalambury_maven.server.PilipiukGenerator;
import pl.uj.edu.tcs.kalambury_maven.server.RiddlesGeneratorLevelHard;
import pl.uj.edu.tcs.kalambury_maven.server.SimplePointManager;

public class SimpleServerGui extends JFrame {

	private static final long serialVersionUID = 2635343922761450269L;
	private JPanel contentPane;
	private GameLogic gameLogic;
	private JComboBox<String> chooseGenerator;
	private JComboBox<String> chooseManager;

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
		setBounds(100, 100, 300, 150);
		contentPane = new JPanel();
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 228 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, };
		gbl_contentPane.columnWeights = new double[] { 1.0 };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JButton btnSetRoundTime = new JButton("Set round time");
		GridBagConstraints gbc_btnSetRoundTime = new GridBagConstraints();
		gbc_btnSetRoundTime.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSetRoundTime.insets = new Insets(0, 0, 5, 0);
		gbc_btnSetRoundTime.gridx = 0;
		gbc_btnSetRoundTime.gridy = 2;
		btnSetRoundTime.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				long roundTime = 1000 * Integer.parseInt(JOptionPane
						.showInputDialog("Input round time in seconds:"));
				Options.getInstance().setRoundTimeInMilisec(roundTime);
			}
		});

		JButton btnStartGame = new JButton("Start game");
		GridBagConstraints gbc_btnStartGame = new GridBagConstraints();
		gbc_btnStartGame.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnStartGame.insets = new Insets(0, 0, 5, 0);
		gbc_btnStartGame.gridx = 0;
		gbc_btnStartGame.gridy = 1;
		btnStartGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(chooseGenerator.getSelectedItem());
				switch (chooseGenerator.getSelectedItem().toString()) {
				case "Hard generator":
					gameLogic
							.setRiddlesGenerator(new RiddlesGeneratorLevelHard());
					break;
				case "Pilipiuk generator":
					gameLogic
							.setRiddlesGenerator(new PilipiukGenerator());
					break;
				default:
					gameLogic
							.setRiddlesGenerator(new RiddlesGeneratorLevelHard());
					break;
				}
				switch (chooseManager.getSelectedItem().toString()) {
				case "Simple manager":
					gameLogic.setPointsManager(new SimplePointManager());
					break;
				default:
					gameLogic.setPointsManager(new SimplePointManager());
					break;
				}
				gameLogic.reactTo(null, new NewGameEvent());
			}
		});
		contentPane.add(btnStartGame, gbc_btnStartGame);
		contentPane.add(btnSetRoundTime, gbc_btnSetRoundTime);

		chooseGenerator = new JComboBox<String>();
		chooseGenerator.setModel(new DefaultComboBoxModel<String>(new String[] {
				"Choose riddle generator...", "Hard generator",
				"Pilipiuk generator" }));
		GridBagConstraints gbc_chooseGenerator = new GridBagConstraints();
		gbc_chooseGenerator.insets = new Insets(0, 0, 5, 0);
		gbc_chooseGenerator.fill = GridBagConstraints.HORIZONTAL;
		gbc_chooseGenerator.gridx = 0;
		gbc_chooseGenerator.gridy = 3;
		contentPane.add(chooseGenerator, gbc_chooseGenerator);

		chooseManager = new JComboBox<>();
		chooseManager.setModel(new DefaultComboBoxModel<>(new String[] {
				"Choose points manager...", "Simple manager" }));
		GridBagConstraints gbc_chooseManager = new GridBagConstraints();
		gbc_chooseManager.insets = new Insets(0, 0, 5, 0);
		gbc_chooseManager.fill = GridBagConstraints.HORIZONTAL;
		gbc_chooseManager.gridx = 0;
		gbc_chooseManager.gridy = 4;
		contentPane.add(chooseManager, gbc_chooseManager);
	}

	public void setGameLogic(GameLogic gl) {
		this.gameLogic = gl;
	}
}
