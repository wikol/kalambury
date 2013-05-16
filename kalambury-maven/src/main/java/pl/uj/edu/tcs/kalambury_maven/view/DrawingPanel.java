package pl.uj.edu.tcs.kalambury_maven.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import pl.uj.edu.tcs.kalambury_maven.controller.DrawingController;
import pl.uj.edu.tcs.kalambury_maven.event.NewPointsDrawnEvent;
import pl.uj.edu.tcs.kalambury_maven.model.Brush;
import pl.uj.edu.tcs.kalambury_maven.model.DrawingModel;
import pl.uj.edu.tcs.kalambury_maven.model.Point;

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

	private final List<Point> pointsToCommit = new LinkedList<>();
	/**
	 * Timer wysyłający Eventy
	 */

	private final Timer drawingTimer;

	private DrawingController controller;
	private DrawingModel model;
	private Brush brush;

	/**
	 * Adapter ruchów myszki potrzebny do komunikacji z rysującym użytkownikiem.
	 */

	private class MyMouseAdapter implements MouseMotionListener, MouseListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			synchronized (pointsToCommit) {
				float x = (float) e.getX()
						/ (float) DrawingPanel.this.getWidth();
				float y = (float) e.getY()
						/ (float) DrawingPanel.this.getHeight();
				pointsToCommit.add(new Point(x, y, brush.radius, brush.color));
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			synchronized (pointsToCommit) {
				float x = (float) e.getX()
						/ (float) DrawingPanel.this.getWidth();
				float y = (float) e.getY()
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
		}

	}

	/**
	 * Konstruktor panela
	 * 
	 */

	public DrawingPanel() {
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

		}, 0, 100);
	}

	// public static void main(String... args) {
	// JFrame frame = new JFrame();
	// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	// frame.setSize(250, 200);
	// DrawingPanel cb = new DrawingPanel(new DrawingModel(),
	// new DrawingController(), 0);
	// cb.setDrawingPlayerID(0);
	// frame.getContentPane().setLayout(new GridLayout(1, 2, 0, 0));
	// frame.getContentPane().add(cb);
	// frame.getContentPane().add(new BrushPanel(cb.controller, cb.model, cb));
	// frame.setVisible(true);
	// cb.controller.setModel(cb.model);
	// cb.controller.setView(cb);
	// cb.model.registerView(cb);
	// cb.controller.addHandler(DrawingActualisationEvent.class, new
	// DrawingActualisationEventHandler(cb.model, cb));
	// cb.reactor.setHandler(DrawingActualisationEvent.class, new
	// DrawingActualisationEventHandler(cb.model, cb));
	// cb.model.addHandler(DrawingActualisationEvent.class, new
	// DrawingActualisationEventHandler(cb.model, cb));
	// }

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
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
	}

}
