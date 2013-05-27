package pl.uj.edu.tcs.kalambury_maven.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import pl.uj.edu.tcs.kalambury_maven.controller.DrawingController;
import pl.uj.edu.tcs.kalambury_maven.event.NewPointsDrawnEvent;
import pl.uj.edu.tcs.kalambury_maven.model.Brush;
import pl.uj.edu.tcs.kalambury_maven.model.DrawingModel;
import pl.uj.edu.tcs.kalambury_maven.model.Point;

/**
 * Panel wyświetlający rysunek i pozwalający graczowi przedtawiającemu hasło na
 * jego edycję.
 * 
 * @author Katarzyna Janocha, Michał Piekarz
 * 
 */

public class DrawingPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2111488806612395791L;
	private final List<Point> pointsToCommit = new LinkedList<>();
	/**
	 * Timer wysyłający Eventy
	 */

	private final Timer drawingTimer;

	private Point prevPoint;
	private DrawingController controller;
	private DrawingModel model;
	private Brush brush;

	/**
	 * Adapter ruchów myszki potrzebny do komunikacji z rysującym użytkownikiem.
	 */

	private class MyMouseAdapter implements MouseMotionListener, MouseListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			float x = ((float) (e.getX() - brush.radius / 2.0))
					/ (float) DrawingPanel.this.getWidth();
			float y = ((float) (e.getY() - brush.radius / 2.0))
					/ (float) DrawingPanel.this.getHeight();

			float tempX = e.getX();
			float tempY = e.getY();
			if (prevPoint == null)
				prevPoint = new Point(tempX, tempY, brush.radius, brush.color);
			else {
				synchronized (prevPoint) {
					if (prevPoint.centreX == tempX) {
						int difference = 5;
						if (prevPoint.centreY > tempY)
							difference = -5;
						float temp = prevPoint.centreY + difference;
						while ((temp > prevPoint.centreY && temp < tempY)
								|| (temp < prevPoint.centreY && temp > tempY)) {
							float comX = (float) (tempX - brush.radius / 2.0);
							comX /= (float) DrawingPanel.this.getWidth();
							float comY = (float) (temp - brush.radius / 2.0);
							comY /= (float) DrawingPanel.this.getHeight();
							synchronized (pointsToCommit) {
								pointsToCommit.add(new Point(comX, comY,
										brush.radius, brush.color));
							}
							temp += difference;
						}
					} else {
						float a = prevPoint.centreY - tempY;
						a /= (prevPoint.centreX - tempX);
						float b = tempY - a * tempX;
						int difference = 5;
						if (prevPoint.centreX > tempX)
							difference = -5;
						float temp = prevPoint.centreX + difference;
						while ((temp > prevPoint.centreX && temp < tempX)
								|| (temp < prevPoint.centreX && temp > tempX)) {
							float comX = (float) (temp - brush.radius / 2.0);
							comX /= (float) DrawingPanel.this.getWidth();
							float comY = (float) (temp * a + b - brush.radius / 2.0);
							comY /= (float) DrawingPanel.this.getHeight();
							synchronized (pointsToCommit) {
								pointsToCommit.add(new Point(comX, comY,
										brush.radius, brush.color));
							}
							temp += difference;
						}
					}
				}
				prevPoint = new Point(tempX, tempY, brush.radius, brush.color);
			}
			synchronized (pointsToCommit) {
				pointsToCommit.add(new Point(x, y, brush.radius, brush.color));
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			synchronized (pointsToCommit) {
				float x = ((float) (e.getX() - brush.radius / 2.0))
						/ (float) DrawingPanel.this.getWidth();
				float y = ((float) (e.getY() - brush.radius / 2.0))
						/ (float) DrawingPanel.this.getHeight();
				pointsToCommit.add(new Point(x, y, brush.radius, brush.color));
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (prevPoint == null)
				return;
			synchronized (prevPoint) {
				prevPoint = null;
			}
		}

	}

	/**
	 * Konstruktor panela
	 * 
	 */

	public DrawingPanel() {
		setMinimumSize(new Dimension(500, 400));
		setPreferredSize(new Dimension(400, 300));
		setBackground(Color.WHITE);

		addMouseMotionListener(new MyMouseAdapter());
		addMouseListener(new MyMouseAdapter());

		brush = new Brush(Brush.MEDIUM, Color.BLACK);
		drawingTimer = new Timer();

		drawingTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				synchronized (pointsToCommit) {
					if (pointsToCommit.isEmpty() || controller == null)
						return;
					DrawingPanel.this.controller
							.sendEventToServer(new NewPointsDrawnEvent(
									pointsToCommit));
					pointsToCommit.clear();
				}
			}

		}, 0, 50);

		setCustomCursor();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		/*
		 * rysunek przykładowy - zgodnie z żądaniem
		 */
		g.setColor(Color.YELLOW);
		g.fillOval(10, 10, 200, 200);
		g.setColor(Color.BLACK);
		g.fillOval(50, 50, 40, 40);
		g.fillOval(120, 50, 40, 40);
		g.fillOval(40, 120, 30, 30);
		g.fillOval(50, 130, 30, 30);
		g.fillOval(60, 138, 30, 30);
		g.fillOval(70, 145, 30, 30);
		g.fillOval(80, 150, 30, 30);
		g.fillOval(90, 151, 30, 30);
		g.fillOval(100, 150, 30, 30);
		g.fillOval(110, 145, 30, 30);
		g.fillOval(120, 138, 30, 30);
		g.fillOval(130, 130, 30, 30);
		g.fillOval(140, 120, 30, 30);
		if (model == null)
			return;
		List<Point> points = model.getDrawing();
		for (Point point : points) {
			g.setColor(point.color);
			g.fillOval((int) (point.centreX * this.getWidth()),
					(int) (point.centreY * this.getHeight()),
					(int) point.paintRadius, (int) point.paintRadius);
		}
	}

	/**
	 * Aktualizacja pędzla
	 * 
	 * @param brush
	 *            - pedzel
	 */

	public void setBrush(Brush brush) {
		this.brush = brush;
	}

	/**
	 * Ustawia controller
	 * 
	 * @param controller
	 */
	public void setController(DrawingController controller) {
		this.controller = controller;
	}

	/**
	 * Ustawia model
	 * 
	 * @param model
	 */
	public void setModel(DrawingModel model) {
		this.model = model;
	}

	/**
	 * Wywołane przez model gdy następują zmiany
	 */
	public void modelChanged() {
		this.brush = new Brush(model.getBrush());
		this.setCustomCursor();
		this.repaint();
	}

	private void setCustomCursor() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dim = toolkit.getBestCursorSize(brush.radius, brush.radius);
		BufferedImage image = new BufferedImage(dim.width, dim.height,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = image.createGraphics();
		Shape circle = new Ellipse2D.Float(0, 0, dim.width - 1, dim.height - 1);
		g.setColor(brush.color);
		g.fill(circle);
		if (brush.color == Color.WHITE) {
			g.setColor(Color.BLACK);
			g.draw(circle);
		}
		int centerX = (dim.width - 1) / 2;
		int centerY = (dim.height - 1) / 2;
		g.dispose();

		final Cursor cursor = toolkit.createCustomCursor(image, new java.awt.Point(
				centerX, centerY), "myCursor");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				setCursor(cursor);
			}
		});
	}
}
