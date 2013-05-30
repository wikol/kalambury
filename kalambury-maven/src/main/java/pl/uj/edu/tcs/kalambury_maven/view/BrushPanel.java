package pl.uj.edu.tcs.kalambury_maven.view;

import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

import pl.uj.edu.tcs.kalambury_maven.controller.DrawingController;
import pl.uj.edu.tcs.kalambury_maven.event.BrushChangedEvent;
import pl.uj.edu.tcs.kalambury_maven.model.Brush;
import java.awt.Label;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * 
 * Zmienna paleta kolorów pędzli
 * 
 * @author Katarzyna Janocha, Michał Piekarz
 * 
 */

public class BrushPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7847540478021860391L;
	private DrawingController controller;
	private int radius = Brush.MEDIUM;
	private Color color = Color.BLACK;
	private JLabel curColorLabel;
	
	/**
	 * Konstuktor
	 */
	public BrushPanel() {

		setLayout(new GridLayout(7, 2, 0, 0));

		JButton btnBlack = new JButton("");
		btnBlack.setBackground(new Color(15,15,29));
		add(btnBlack);
		btnBlack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				color = Color.BLACK;
				brushChanged();
			}
		});
				
				JLabel label = new JLabel("Brush size:");
				label.setHorizontalAlignment(SwingConstants.CENTER);
				add(label);
		
				JButton btnRed = new JButton();
				btnRed.setBackground(new Color(240, 3, 3));
				add(btnRed);
				btnRed.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						color = new Color(240, 3, 3);
						brushChanged();
					}
				});

		JButton btnTiny = new JButton();
		add(btnTiny);
		setImageForButton(btnTiny, Brush.TINY);
		btnTiny.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				radius = Brush.TINY;
				brushChanged();
			}
		});
		
				JButton btnYellow = new JButton();
				btnYellow.setBackground(new Color(255, 255, 51));
				add(btnYellow);
				btnYellow.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						color = new Color(255, 255, 51);
						brushChanged();
					}
				});

		JButton btnSmall = new JButton();
		add(btnSmall);
		setImageForButton(btnSmall, Brush.SMALL);
		btnSmall.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				radius = Brush.SMALL;
				brushChanged();
			}
		});
				
						JButton btnBlue = new JButton();
						btnBlue.setBackground(new Color(51, 102, 204));
						add(btnBlue);
						btnBlue.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								color = new Color(51, 102, 204);
								brushChanged();
							}
						});

		JButton btnMedium = new JButton();
		add(btnMedium);
		setImageForButton(btnMedium, Brush.MEDIUM);
		btnMedium.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				radius = Brush.MEDIUM;
				brushChanged();
			}
		});
		
				JButton btnGreen = new JButton("");
				btnGreen.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						color = new Color(51, 153, 0);
						brushChanged();
					}
				});
				btnGreen.setBackground(new Color(51, 153, 0));
				add(btnGreen);

		JButton btnLarge = new JButton();
		add(btnLarge);
		setImageForButton(btnLarge, Brush.LARGE);
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
		
		JButton btnXL = new JButton();
		add(btnXL);
		setImageForButton(btnXL, Brush.EXTRA_LARGE);
		btnXL.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				radius = Brush.EXTRA_LARGE;
				brushChanged();
			}
		});
		
		JButton btnPalette = new JButton("Choose color...");
		btnPalette.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				color = JColorChooser.showDialog(BrushPanel.this,"Wybierz kolor",color);
				if (color!=null)
					brushChanged();
			}
		});
		add(btnPalette);
		
		curColorLabel = new JLabel("Current Color");
		curColorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		curColorLabel.setOpaque(true);
		curColorLabel.setBackground(color);
		curColorLabel.setForeground(new Color(255-color.getRed(),255-color.getGreen(),255-color.getBlue()));
		add(curColorLabel);
		

	}

	/**
	 * Metoda wywoływana gdy zmienia się pędzel
	 */
	private void brushChanged() {
		BrushChangedEvent event = new BrushChangedEvent(
				(color == Color.WHITE) ? (int) (radius * 1.5) : radius, color);
		controller.reactTo(event);
		EventQueue.invokeLater(
				new Runnable() {
					
					@Override
					public void run() {
						curColorLabel.setBackground(color);
						curColorLabel.setForeground(new Color(255-color.getRed(),255-color.getGreen(),255-color.getBlue()));
						if(color.getGreen()>130 )curColorLabel.setForeground(new Color(color.getRed()/2,color.getGreen()/2,color.getBlue()/2));
						else if(color.getBlue()>130)curColorLabel.setForeground(new Color(color.getRed()/2,color.getGreen()/2,color.getBlue()/2));
					}
				}
				);
	}

	/**
	 * Ustawia controller z którego będzie korzystał BrushPanel
	 * 
	 * @param controller
	 */
	public void setController(DrawingController controller) {
		this.controller = controller;
	}

	private void setImageForButton(JButton button, int radius) {
		BufferedImage image = new BufferedImage(radius, radius,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		Shape circle = new Ellipse2D.Float(radius/4, radius/4, radius/2, radius/2);
		g.setColor(Color.BLACK);
		g.fill(circle);
		g.dispose();
		button.setIcon(new ImageIcon(image));
	}
}
