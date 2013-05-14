package pl.uj.edu.tcs.kalambury_maven.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import pl.uj.edu.tcs.kalambury_maven.controller.AppController;
import pl.uj.edu.tcs.kalambury_maven.controller.Controller;
import pl.uj.edu.tcs.kalambury_maven.event.DrawingActualisationEvent;
import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventHandler;
import pl.uj.edu.tcs.kalambury_maven.event.EventNotHandledException;
import pl.uj.edu.tcs.kalambury_maven.model.Brush;
import pl.uj.edu.tcs.kalambury_maven.model.Model;
import pl.uj.edu.tcs.kalambury_maven.model.Point;
import pl.uj.edu.tcs.kalambury_maven.model.SimpleModel;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Panel wyświetlający rysunek i pozwalający graczowi przedtawiającemu hasło na
 * jego edycję.
 * 
 * @author Katarzyna Janocha, Michał Piekarz
 * 
 */

public class DrawingPanel extends JPanel {

	private final int playerID;
	private final long width, height;
	private final Model model;
	private final Controller controller;
	private final List<Point> points = new LinkedList<>();
	private final List<Point> pointsToCommit = new LinkedList<>();

	private int drawingPlayerID;
	private Brush brush;
	
	/**
	 * Timer wysyłający Eventy
	 */
	
	private Timer drawingTimer;

	/**
	 * Adapter ruchów myszki potrzebny do komunikacji z rysującym użytkownikiem.
	 */

	private class MyMouseAdapter implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
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
	 * @param model 
	 * @param controller
	 * @param playerID 
	 */
	
	public DrawingPanel(Model model, Controller controller, int playerID) {
		setBackground(Color.WHITE);

		addMouseMotionListener(new MyMouseAdapter());

		width = this.getWidth();
		height = this.getHeight();
		brush = new Brush();
		drawingTimer = new Timer();

		this.model = model;
		this.controller = controller;
		this.playerID = playerID;

		drawingTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				if (DrawingPanel.this.playerID == drawingPlayerID) {
					synchronized (pointsToCommit) {
						try {
							DrawingPanel.this.controller
									.reactTo(new DrawingActualisationEvent(
											pointsToCommit));
						} catch (EventNotHandledException e) {
							e.printStackTrace();
						}
						pointsToCommit.clear();
					}
				}
			}

		}, 0, 100);
	}

	public static void main(String... args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(250, 200);
		DrawingPanel cb = new DrawingPanel(new SimpleModel(),
				new AppController(), 0);
		frame.getContentPane().add(cb);
		frame.setVisible(true);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Point point : points) {
			g.setColor(point.color);
			g.fillOval((int) point.centreX, (int) point.centreY,
					(int) point.paintRadius, (int) point.paintRadius);
		}
	}

	/**
	 * Ustawia ID aktualnego gracza
	 * @param drawingPlayerID 
	 */
	
	public void setDrawingPlayerID(int drawingPlayerID) {
		this.drawingPlayerID = drawingPlayerID;
		if (this.drawingPlayerID == playerID) {
		}
	}
	
	/**
	 * Wyświetla podany rysunek
	 * @param points - zbiór punktów do narysowania
	 */
	
	public void drawPoints(List<Point> points){
		this.points.clear();
		this.points.addAll(points);
		repaint();
	}

	/**
	 * Aktualizacja pędzla
	 * @param brush - pedzel
	 */
	
	public void setBrush(Brush brush) {
		this.brush = brush;
	}

}
