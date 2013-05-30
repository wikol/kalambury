package pl.uj.edu.tcs.kalambury_maven.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class RiddleAndTime extends JPanel {

	private JTextField timeTextField;
	private JTextField riddleTextField;
	private String riddle = "";
	private long timeLeft;
	private long roundTimeInSeconds = 100;
	/**
	 * @wbp.nonvisual location=39,77
	 */
	private final JProgressBar progressBar;

	/**
	 * 
	 * Służy do ustawienia hasła.
	 * 
	 * @param riddle
	 *            - hasło jako String
	 */

	public void setRiddle(String riddle) {
		this.riddle = riddle;
	}

	/**
	 * 
	 * Ustawia czas trwania rundy.
	 * 
	 * @param roundTimeInSeconds
	 *            - czas trwania rundy jako long
	 */

	public void setRoundTime(long roundTimeInSeconds) {
		this.roundTimeInSeconds = roundTimeInSeconds;
	}

	/**
	 * 
	 * Decyduje o tym, czy pole z zagadką jest widoczne dla gracza.
	 * 
	 * @param b - boolean niosący powyższą informację 
	 */
	
	public void setRiddleVisible(boolean b){
		riddleTextField.setVisible(b);
	}
	
	/**
	 * 
	 * Metoda Ani - pobiera od serwera, ile czasu w sekundach mamy do końca rundy.
	 * 
	 */
	
	private long getTimeLeft(){
		return 0;
		//TODO pobieranie czasu, który zostałdo końca rundy
	}
	
	private String timeFormat(long arg) {
		String res = new String("");
		if (arg / 60 < 10)
			res += "0";
		res += arg / 60;
		res += " : ";
		if (arg % 60 < 10)
			res += "0";
		res += arg % 60;
		return res;
	}

	/**
	 * Create the panel.
	 */
	public RiddleAndTime() {
		setMinimumSize(new Dimension(250, 90));
		setPreferredSize(new Dimension(300, 90));
		setLayout(new GridLayout(3, 1, 0, 0));
		riddleTextField = new JTextField();
		riddleTextField.setHorizontalAlignment(SwingConstants.CENTER);
		add(riddleTextField);
		riddleTextField.setColumns(2);
		riddleTextField.setEditable(false);

		timeTextField = new JTextField();
		timeTextField.setHorizontalAlignment(SwingConstants.CENTER);
		add(timeTextField);
		timeTextField.setColumns(2);
		timeTextField.setEditable(false);
		Timer riddlaAndTimeTimer = new Timer();

		progressBar = new JProgressBar(0, (int) roundTimeInSeconds);
		add(progressBar);

		riddleTextField.setText(riddle);

		riddlaAndTimeTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				synchronized (this) {
					long curTime = System.currentTimeMillis() / 1000;
					long res = getTimeLeft();
					if (res < 0)
						return;
					timeTextField.setText(timeFormat(res));
					riddleTextField.setText(riddle);
					progressBar.setValue((int) (res));
					progressBar.repaint();
				}
			}

		}, 0, 100);
	}

	
//	 public static void main(String... args){ JFrame frame = new JFrame();
//	 frame.setSize(400, 400); RiddleAndTime panel = new RiddleAndTime();
//	 frame.setContentPane(panel); frame.setVisible(true); }
	 

}
