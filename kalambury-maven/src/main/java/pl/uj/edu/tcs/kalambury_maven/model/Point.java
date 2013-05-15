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
