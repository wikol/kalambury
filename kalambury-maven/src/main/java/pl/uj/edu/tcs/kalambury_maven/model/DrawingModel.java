package pl.uj.edu.tcs.kalambury_maven.model;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import pl.uj.edu.tcs.kalambury_maven.view.DrawingPanel;

/**
 * Prosty model rysunku
 * 
 * @author Katarzyna Janocha, Michał Piekarz
 * 
 */

public class DrawingModel {

	private List<Point> drawing = new LinkedList<>();
	private final Brush brush = new Brush(Brush.MEDIUM, Color.BLACK);
	private DrawingPanel drawingPanel;

	/**
	 * Ustawia DrawingPanel z którym komunikuje się ten model
	 * 
	 * @param drawingPanel
	 */
	public void setDrawingPanel(DrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}

	/**
	 * Aktualizuje rysunek o podaną listę punktów
	 * 
	 * @param newPoints
	 *            - lista punktów do dodania do rysunku
	 */
	public void actualiseDrawing(List<Point> newPoints) {
		synchronized (drawing) {
			drawing.addAll(newPoints);
		}
	}

	/**
	 * Metoda do uzyskiwania aktualnego rysunku
	 * 
	 * @return - rysunek przechowywany przez model
	 */
	public List<Point> getDrawing() {
		synchronized (drawing) {
			return new LinkedList<>(drawing);
		}
	}

	/**
	 * Getter do Brusha
	 * 
	 * @return - pędzel przechowywany przez model
	 */
	public Brush getBrush() {
		synchronized (brush) {
			return brush;
		}
	}

	/**
	 * Setter do pędzla przechowywanego przez model
	 * 
	 * @param radius
	 *            - promień pędzla
	 * @param color
	 *            - kolor pędzla
	 */
	public void setBrush(int radius, Color color) {
		synchronized (brush) {
			brush.radius = radius;
			brush.color = color;
			drawingPanel.brushChanged();
		}
	}

}
