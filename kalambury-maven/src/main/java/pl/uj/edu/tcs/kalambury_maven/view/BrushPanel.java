package pl.uj.edu.tcs.kalambury_maven.view;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import pl.uj.edu.tcs.kalambury_maven.controller.DrawingController;
import pl.uj.edu.tcs.kalambury_maven.event.BrushChangedEvent;
import pl.uj.edu.tcs.kalambury_maven.model.Brush;

/**
 * 
 * Zmienna paleta kolorów pędzli
 * 
 * @author Katarzyna Janocha, Michał Piekarz
 * 
 */

public class BrushPanel extends JPanel {

	private DrawingController controller;
	private int radius;
	private Color color;

	/**
	 * Konstuktor
	 */
	public BrushPanel() {

		setLayout(new GridLayout(5, 2, 0, 0));

		JButton btnBlack = new JButton("");
		btnBlack.setBackground(Color.BLACK);
		add(btnBlack);
		btnBlack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				color = Color.BLACK;
				brushChanged();
			}
		});

		JButton btnTiny = new JButton("XS");
		add(btnTiny);
		btnTiny.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				radius = Brush.TINY;
				brushChanged();
			}
		});

		JButton btnBlue = new JButton();
		btnBlue.setBackground(Color.BLUE);
		add(btnBlue);
		btnBlue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				color = Color.BLUE;
				brushChanged();
			}
		});

		JButton btnSmall = new JButton("S");
		add(btnSmall);
		btnSmall.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				radius = Brush.SMALL;
				brushChanged();
			}
		});

		JButton btnRed = new JButton();
		btnRed.setBackground(Color.RED);
		add(btnRed);
		btnRed.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				color = Color.RED;
				brushChanged();
			}
		});

		JButton btnMedium = new JButton("M");
		add(btnMedium);
		btnMedium.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				radius = Brush.MEDIUM;
				brushChanged();
			}
		});

		JButton btnYellow = new JButton();
		btnYellow.setBackground(Color.YELLOW);
		add(btnYellow);
		btnYellow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				color = Color.YELLOW;
				brushChanged();
			}
		});

		JButton btnLarge = new JButton("L");
		add(btnLarge);
		btnLarge.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				radius = Brush.LARGE;
				brushChanged();
			}
		});

		JButton btnEraser = new JButton();
		btnEraser.setBackground(Color.WHITE);
		add(btnEraser);
		btnEraser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				color = Color.WHITE;
				brushChanged();
			}
		});

		JButton btnXL = new JButton("XL");
		add(btnXL);
		btnXL.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				radius = Brush.EXTRA_LARGE;
				brushChanged();
			}
		});

	}

	/**
	 * Metoda wywoływana gdy zmienia się pędzel
	 */
	private void brushChanged() {
		BrushChangedEvent event = new BrushChangedEvent((color == Color.WHITE) ? (int) (radius * 1.5)
				: radius, color);
		controller.sendEventToServer(event);
	}

	/**
	 * Ustawia controller z którego będzie korzystał BrushPanel
	 * @param controller
	 */
	public void setController(DrawingController controller) {
		this.controller = controller;
	}
}
