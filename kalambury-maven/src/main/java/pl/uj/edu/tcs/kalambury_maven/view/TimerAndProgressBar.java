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

public class TimerAndProgressBar extends JPanel {

	private JTextField timeTextField;
	private long startTime;
	private long roundTimeInSeconds = 180;
	private Timer riddlaAndTimeTimer;
	private boolean timerStarted = false;
	/**
	 * @wbp.nonvisual location=39,77
	 */
	private final JProgressBar progressBar;

	/**
	 * 
	 * Ustawia czas trwania rundy.
	 * 
	 * @param roundTimeInSeconds
	 *            - czas trwania rundy jako long
	 */

	public void setRoundTime(long roundTimeInSeconds) {
		this.roundTimeInSeconds = roundTimeInSeconds;
		this.progressBar.setMaximum((int)roundTimeInSeconds);
	}

	public void startNextRound(final long timeLeftInSeconds) {
		stopTimer();
		if (timerStarted)
			System.out.println("Timer already started");
		timerStarted = true;
		startTime = System.currentTimeMillis()/1000;

		riddlaAndTimeTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				synchronized (this) {

					long curTime = System.currentTimeMillis() / 1000;
					long res = timeLeftInSeconds - (curTime - startTime);
					if (res < 0)
						return;
					timeTextField.setText(timeFormat(res));
					progressBar.setValue((int) (res));
					progressBar.repaint();
				}
			}

		}, 0, 100);
	}

	public void stopTimer() {
		timerStarted = false;
		riddlaAndTimeTimer.cancel();
		riddlaAndTimeTimer = new Timer();
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
	public TimerAndProgressBar() {
		setMinimumSize(new Dimension(250, 90));
		setPreferredSize(new Dimension(300, 90));
		setLayout(new GridLayout(2, 1, 0, 0));

		timeTextField = new JTextField();
		timeTextField.setHorizontalAlignment(SwingConstants.CENTER);
		add(timeTextField);
		timeTextField.setColumns(2);
		timeTextField.setEditable(false);
		riddlaAndTimeTimer = new Timer();

		progressBar = new JProgressBar(0, (int) roundTimeInSeconds);
		add(progressBar);
	}

	public static void main(String... args) {
		JFrame frame = new JFrame();
		frame.setSize(400, 400);
		TimerAndProgressBar panel = new TimerAndProgressBar();
		frame.setContentPane(panel);
		frame.setVisible(true);
	}

}
