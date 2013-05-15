package pl.uj.edu.tcs.kalambury_maven.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import pl.uj.edu.tcs.kalambury_maven.controller.AppController;
import pl.uj.edu.tcs.kalambury_maven.controller.Controller;
import pl.uj.edu.tcs.kalambury_maven.controller.DrawingController;
import pl.uj.edu.tcs.kalambury_maven.event.DrawingActualisationEvent;
import pl.uj.edu.tcs.kalambury_maven.event.DrawingActualisationEventHandler;
import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventHandler;
import pl.uj.edu.tcs.kalambury_maven.event.EventNotHandledException;
import pl.uj.edu.tcs.kalambury_maven.event.EventReactor;
import pl.uj.edu.tcs.kalambury_maven.model.Brush;
import pl.uj.edu.tcs.kalambury_maven.model.DrawingModel;
import pl.uj.edu.tcs.kalambury_maven.model.Model;
import pl.uj.edu.tcs.kalambury_maven.model.Point;
import pl.uj.edu.tcs.kalambury_maven.model.SimpleModel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
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

public class DrawingPanel extends JPanel implements View{

	private final int playerID;
	private final List<Point> points = new LinkedList<>();
	private final List<Point> pointsToCommit = new LinkedList<>();
	private final EventReactor reactor = new EventReactor();

	private int drawingPlayerID;
	private Brush brush;
	private DrawingController controller;
	private DrawingModel model;
	
	/**
	 * Timer wysyłający Eventy
	 */
	
	private Timer drawingTimer;

	/**
	 * Adapter ruchów myszki potrzebny do komunikacji z rysującym użytkownikiem.
	 */

	private class MyMouseAdapter implements MouseMotionListener, MouseListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			synchronized (pointsToCommit) {
				float x = (float) e.getX() / (float) DrawingPanel.this.getWidth();
				float y = (float) e.getY() / (float) DrawingPanel.this.getHeight();
				pointsToCommit.add(new Point(x, y, brush.radius, brush.color));
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			synchronized (pointsToCommit) {
				float x = (float) e.getX() / (float) DrawingPanel.this.getWidth();
				float y = (float) e.getY() / (float) DrawingPanel.this.getHeight();
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
		}

	}

	/**
	 * Konstruktor panela
	 * @param model 
	 * @param controller
	 * @param playerID 
	 */
	
	public DrawingPanel(DrawingModel model, DrawingController controller, int playerID) {
		setBackground(Color.WHITE);

		addMouseMotionListener(new MyMouseAdapter());
		addMouseListener(new MyMouseAdapter());

		brush = new Brush(Brush.MEDIUM,Color.BLACK);
		drawingTimer = new Timer();

		this.model = model;
		this.controller = controller;
		this.playerID = playerID;

		drawingTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				if (DrawingPanel.this.playerID == drawingPlayerID) {
					synchronized (pointsToCommit) {
						if (pointsToCommit.isEmpty()) return;
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
		DrawingPanel cb = new DrawingPanel(new DrawingModel(),
				new DrawingController(), 0);
		cb.setDrawingPlayerID(0);
		frame.getContentPane().setLayout(new GridLayout(1, 2, 0, 0));
		frame.getContentPane().add(cb);
		frame.getContentPane().add(new BrushPanel(cb.controller, cb.model, cb));
		frame.setVisible(true);
		cb.controller.setModel(cb.model);
		cb.controller.setView(cb);
		cb.model.registerView(cb);
		cb.controller.addHandler(DrawingActualisationEvent.class, new DrawingActualisationEventHandler(cb.model, cb));
		cb.reactor.setHandler(DrawingActualisationEvent.class, new DrawingActualisationEventHandler(cb.model, cb));
		cb.model.addHandler(DrawingActualisationEvent.class, new DrawingActualisationEventHandler(cb.model, cb));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Point point : points) {
			g.setColor(point.color);
			g.fillOval((int) (point.centreX*this.getWidth()), (int) (point.centreY*this.getHeight()),
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
		System.out.println(points.toString());
		repaint();
	}

	/**
	 * Aktualizacja pędzla
	 * @param brush - pedzel
	 */
	
	public void setBrush(Brush brush) {
		this.brush = brush;
	}

	@Override
	public void reactTo(Event e) throws EventNotHandledException {
		reactor.handle(e);
	}

	@Override
	public void setController(Controller c) {
		if (c instanceof DrawingController)
			controller = (DrawingController)c;
	}
	
	public void addHandler(Class<? extends Event> e, EventHandler h){
		reactor.setHandler(e, h);
	}

	@Override
	public void setModel(Model m) {
		if (m instanceof DrawingModel) model = (DrawingModel)m;
	}

	public void setBrush(int radius, Color color) {
		brush = new Brush(radius,color);
	}

}
