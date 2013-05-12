package pl.uj.edu.tcs.kalambury_maven.view;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import pl.uj.edu.tcs.kalambury_maven.model.Brush;

import javax.swing.ImageIcon;

/**
 * 
 * Zmienna paleta kolorów pędzli
 * 
 * @author Katarzyna Janocha, Michał Piekarz
 * 
 */

public class BrushPanel extends JPanel {

	private Brush brush;
	
	/**
	 * Create the panel.
	 */
	public BrushPanel() {
		setLayout(new GridLayout(5, 2, 0, 0));

		JButton btnBlack = new JButton("");
		btnBlack.setBackground(Color.BLACK);
		add(btnBlack);
		btnBlack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				brush.color = Color.BLACK;
			}
		});

		JButton btnTiny = new JButton("XS");
		add(btnTiny);
		btnTiny.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				brush.radius = Brush.TINY;
			}
		});

		JButton btnBlue = new JButton();
		btnBlue.setBackground(Color.BLUE);
		add(btnBlue);
		btnBlue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				brush.color = Color.BLUE;
			}
		});

		JButton btnSmall = new JButton("S");
		add(btnSmall);
		btnSmall.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				brush.radius = Brush.SMALL;
			}
		});

		JButton btnRed = new JButton();
		btnRed.setBackground(Color.RED);
		add(btnRed);
		btnRed.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				brush.color = Color.RED;
			}
		});
		
		
		JButton btnMedium = new JButton("M");
		add(btnMedium);
		btnMedium.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				brush.radius = Brush.MEDIUM;
			}
		});
		
		JButton btnYellow = new JButton();
		btnYellow.setBackground(Color.YELLOW);
		add(btnYellow);
		btnYellow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				brush.color = Color.YELLOW;
			}
		});

		JButton btnLarge = new JButton("L");
		add(btnLarge);
		btnLarge.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				brush.radius = Brush.LARGE;
			}
		});

		JButton btnEraser = new JButton();
		btnEraser.setIcon(new ImageIcon(BrushPanel.class.getResource("/eraser.jpeg")));
		btnEraser.setBackground(Color.WHITE);
		add(btnEraser);
		btnEraser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				brush.color = Color.WHITE;
			}
		});
		
		JButton btnXL = new JButton("XL");
		add(btnXL);
		btnXL.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				brush.radius = Brush.EXTRA_LARGE;
			}
		});

	}
}
