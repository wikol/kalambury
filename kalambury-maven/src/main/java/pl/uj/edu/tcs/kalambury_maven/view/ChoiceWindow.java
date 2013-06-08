package pl.uj.edu.tcs.kalambury_maven.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;

import pl.uj.edu.tcs.kalambury_maven.server.SimpleServer;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ChoiceWindow extends JFrame {

	private static final long serialVersionUID = 4123944096350386314L;
	private final AppView view;

	/**
	 * For testing purposes only
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChoiceWindow frame = new ChoiceWindow(null);
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
	public ChoiceWindow(AppView aview) {
		super("Kalambury");
		view = aview;
		System.out.println(view);
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JButton playButton = new JButton("Play");
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Thread() {
					@Override
					public void run() {
						view.displayLogin();
					}
				}.start();
				ChoiceWindow.this.dispose();
			}
		});
		getContentPane().add(playButton, BorderLayout.NORTH);

		JButton serverButton = new JButton("Start a server");
		serverButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					@Override
					public void run() {
						SimpleServer.start(8888);
					}
				}.start();
				ChoiceWindow.this.dispose();
			}
		});
		getContentPane().add(serverButton, BorderLayout.SOUTH);

		pack();
		setResizable(false);
	}

}
