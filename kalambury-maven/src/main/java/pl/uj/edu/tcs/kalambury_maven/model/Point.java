package pl.uj.edu.tcs.kalambury_maven.model;

import java.awt.Color;

/**
 * 
 * Punkt
 * 
 * @author Katarzyna Janocha
 *
 */

public class Point {
	public float centreX;
	public float centreY;
	public float paintRadius;
	public Color color;
	
	/**
	 * Konstruktor punktu
	 * @param centreX - środek punktu w osi OX, z przedziału [0,1]
	 * @param centreY - środek punktu w osi OY, z przedziału [0,1]
	 * @param paintRadius - promień punktu
	 * @param color - kolor punktu
	 */
	public Point (float centreX, float centreY, float paintRadius, Color color){
		this.centreX = centreX;
		this.centreY = centreY;
		this.paintRadius = paintRadius;
		this.color = color;
	}
	
	public String toString(){
		return centreX + " " + centreY;
	}
}