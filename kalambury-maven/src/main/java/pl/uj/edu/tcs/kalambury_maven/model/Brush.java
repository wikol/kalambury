package pl.uj.edu.tcs.kalambury_maven.model;

import java.awt.Color;
import java.io.Serializable;

/**
 * Klasa odpowiadająca za pędzel
 * 
 * @author Katarzyna Janocha, Michał Piekarz
 * 
 */

public class Brush implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1103157615025275312L;
	public Brush(int radius2, Color color2) {
		this.radius = radius2;
		this.color = color2;
	}

	public Brush(Brush brush) {
		this.radius = brush.radius;
		this.color = brush.color;
	}

	public static final int TINY = 5;
	public static final int SMALL = 10;
	public static final int MEDIUM = 15;
	public static final int LARGE = 20;
	public static final int EXTRA_LARGE = 25;

	public int radius = MEDIUM;
	public Color color = Color.BLACK;
}
