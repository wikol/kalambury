package pl.uj.edu.tcs.kalambury_maven.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import pl.uj.edu.tcs.kalambury_maven.model.Brush;
import pl.uj.edu.tcs.kalambury_maven.model.Point;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

/**
 * Panel wyświetlając rysunek i pozwalający graczowi przedtawiającemu hasło na
 * jego edycję.
 * 
 * @author Katarzyna Janocha, Michał Piekarz
 * 
 */

public class DrawingPanel extends JPanel {

	private List<Point> points = new LinkedList<>();
	private List<Point> pointsToCommit = new LinkedList<>();

	private long width, height;

	private Brush brush;

	/**
	 * Adapter ruchów myszki potrzebny do komunikacji z rysującym użytkownikiem.
	 */

	private class MyMouseAdapter implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			// graphics.setColor(brush.color);
			// graphics.drawOval(e.getX(), e.getY(), brush.radius,
			// brush.radius);
			// System.out.println(e.getX()+" "+e.getY());
			synchronized (points) {
				points.add(new Point(e.getX(), e.getY(), brush.radius,
						brush.color));
				DrawingPanel.this.repaint();
			}
			synchronized (pointsToCommit) {
				float x = (float) e.getX() / (float) width;
				float y = (float) e.getY() / (float) height;
				pointsToCommit.add(new Point(x, y, brush.radius, brush.color));
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}

	}

	/**
	 * Konstruktor panela
	 */
	public DrawingPanel() {
		setBackground(Color.WHITE);

		addMouseMotionListener(new MyMouseAdapter());

		width = this.getWidth();
		height = this.getHeight();
		brush = new Brush();
		// brush.radius = 1000;
	}

	public static void main(String... args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(250, 200);
		DrawingPanel cb = new DrawingPanel();
		frame.getContentPane().add(cb);
		frame.setVisible(true);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Point point : points) {
			g.setColor(point.color);
			g.fillOval((int) point.centreX, (int) point.centreY,
					(int) point.paintRadius, (int) point.paintRadius);
		}
	}
	
	public void setBrush(Brush brush){
		this.brush = brush;
	}

}
