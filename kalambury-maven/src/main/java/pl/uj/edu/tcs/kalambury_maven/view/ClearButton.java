package pl.uj.edu.tcs.kalambury_maven.view;

import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JButton;

import pl.uj.edu.tcs.kalambury_maven.controller.DrawingController;
import pl.uj.edu.tcs.kalambury_maven.event.ClearScreenEvent;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * 
 * @author Katarzyna Janocha, Michał Piekarz
 *
 */

public class ClearButton extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4954475641943716520L;
	private DrawingController controller;
	
	/**
	 * Create the panel.
	 */
	public ClearButton() {
		setLayout(new GridLayout(1, 0, 0, 0));
		
		JButton btnClear = new JButton("CLEAR");
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.sendEventToServer(new ClearScreenEvent());
			}
		});
		add(btnClear);

	}
	
	public void setController(DrawingController controller){
		this.controller = controller;
	}

}
