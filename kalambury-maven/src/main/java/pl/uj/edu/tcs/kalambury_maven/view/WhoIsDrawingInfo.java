package pl.uj.edu.tcs.kalambury_maven.view;

//import java.awt.GridBagConstraints;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class WhoIsDrawingInfo extends JPanel{

	private JLabel whoIsDrawingInfo;
	/**
	 * Wyświetlanie, kto aktualnie rysuje
	 * 
	 * @param nowDrawingName
	 *            - nazwa aktualnie rysującego użytkownika
	 */
	public void updateInfo(String nowDrawingName) {
		if (this.getComponents().length != 0) {
			for (Component c : this.getComponents())
				c.setVisible(false);
			this.removeAll();
		}
		
		whoIsDrawingInfo = new JLabel("Now drawing: " + ((nowDrawingName != null)?(nowDrawingName):("?")));
		this.add(whoIsDrawingInfo);
		this.revalidate();
	}
	
	
	public WhoIsDrawingInfo(String nowDrawingName) {
		super();
		updateInfo(nowDrawingName);
	}
	
}
