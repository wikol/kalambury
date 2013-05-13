package pl.uj.edu.tcs.kalambury_maven.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class ChatBox extends JPanel {
	private static final long serialVersionUID = -1916333443076354308L;

	private JScrollPane scrollPane;
	private JPanel messagesPanel;
	private JTextField userInputField;
	private JButton sendButton;
	private JPanel sendingArea;

	public ChatBox() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		// scrollPane
		scrollPane = new JScrollPane();
		messagesPanel = new JPanel();
		messagesPanel.setLayout(new BoxLayout(messagesPanel,
				BoxLayout.PAGE_AXIS));
		messagesPanel.setSize(scrollPane.getSize());
		scrollPane.getViewport().add(messagesPanel);
		// scrollPane
		add(scrollPane);

		// sendingArea
		sendingArea = new JPanel();
		sendingArea.setLayout(new BoxLayout(sendingArea, BoxLayout.LINE_AXIS));

		// textInput
		userInputField = new JTextField();
		userInputField.setMaximumSize(new Dimension(Integer.MAX_VALUE,
				userInputField.getPreferredSize().height));
		userInputField.setAlignmentX(Component.LEFT_ALIGNMENT);
		// textInput
		sendButton = new JButton("Send");
		sendButton.setAlignmentX(Component.RIGHT_ALIGNMENT);

		sendingArea.add(userInputField);
		sendingArea.add(sendButton);
		// sendingArea

		add(sendingArea);

	}

	private void addNewMessage(ChatMessage chMes) {
		messagesPanel.add(chMes);
		validate();
		repaint();
		JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
		verticalBar.setValue(verticalBar.getMaximum());
	}

	private static class ChatMessage extends JLabel {
		private static final long serialVersionUID = -2110608566518190188L;

		public enum TYPE {
			SM_WROTE, SM_ONLINE, SM_OFFLINE, SM_DRAW, SM_GUESSED
		}

		static Color colors[] = { Color.BLACK, Color.DARK_GRAY, Color.GRAY,
				Color.BLUE, Color.RED };

		private String who;
		private String message;
		private TYPE type;

		/**
		 * For SM_WROTE you need to pass content of message as String int
		 * what[0]. For SM_ONLINE, SM_OFFLINE, SM_DRAW, SM_GUESSED you should
		 * leave what empty.
		 * 
		 * @param whoTyped
		 *            - for which user action was performed
		 * @param type
		 *            - type of action
		 * @param what
		 *            - additional information for action, for example when
		 *            action says that someone wrote new message here will be
		 *            content of that message
		 */
		public ChatMessage(String whoTyped, TYPE type, String... what) {
			super();
			this.who = whoTyped;
			this.type = type;

			setFont(getFont().deriveFont(getFont().getStyle() & ~Font.BOLD));

			// setLineWrap(true);
			// setWrapStyleWord(true);
			// setEditable(false);
			// setContentType("text/html");

			setForeground(colors[type.ordinal()]);
			switch (type) {
			case SM_WROTE:
				message = ": " + what[0];
				break;
			case SM_ONLINE:
				message = " is online!";
				break;
			case SM_OFFLINE:
				message = " is offline!";
				break;
			case SM_DRAW:
				message = " is drawing!";
				break;
			case SM_GUESSED:
				message = " guessed correctly!";
				break;
			}
			String text = String.format(
					"<html><div WIDTH=%d><b>%s</b>%s</div></html>",
					getSize().width-20, whoTyped, message);
			super.setText(text);

			addComponentListener(new ComponentListener() {
				@Override
				public void componentShown(ComponentEvent arg0) {
				}
				
				@Override
				public void componentResized(ComponentEvent arg0) {
					String text = String.format(
							"<html><div WIDTH=%d><b>%s</b> %s</div></html>",
							getSize().width-20, who, message);
					ChatMessage.this.setText(text);
				}

				@Override
				public void componentMoved(ComponentEvent arg0) {
				}
				@Override
				public void componentHidden(ComponentEvent arg0) {
				}
			});
		}

	}

	// Only for testing purposes
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(200, 200);
		ChatBox cb = new ChatBox();
		frame.add(cb);
		frame.setVisible(true);

		cb.addNewMessage(new ChatMessage("inny", ChatMessage.TYPE.SM_ONLINE));
		cb.addNewMessage(new ChatMessage("inny", ChatMessage.TYPE.SM_WROTE,
				"lubię placki"));
		cb.addNewMessage(new ChatMessage("zosiek", ChatMessage.TYPE.SM_ONLINE));
		cb.addNewMessage(new ChatMessage("titanic", ChatMessage.TYPE.SM_ONLINE));
		cb.addNewMessage(new ChatMessage("drzewo", ChatMessage.TYPE.SM_ONLINE));
		cb.addNewMessage(new ChatMessage("krzak", ChatMessage.TYPE.SM_ONLINE));
		cb.addNewMessage(new ChatMessage("dzik", ChatMessage.TYPE.SM_ONLINE));
		cb.addNewMessage(new ChatMessage("biedronka",
				ChatMessage.TYPE.SM_ONLINE));
		cb.addNewMessage(new ChatMessage("titanic", ChatMessage.TYPE.SM_OFFLINE));
		cb.addNewMessage(new ChatMessage("budzik", ChatMessage.TYPE.SM_ONLINE));
		cb.addNewMessage(new ChatMessage("pietruszka",
				ChatMessage.TYPE.SM_ONLINE));
		cb.addNewMessage(new ChatMessage("zielony groszek",
				ChatMessage.TYPE.SM_ONLINE));
		cb.addNewMessage(new ChatMessage("budzik", ChatMessage.TYPE.SM_OFFLINE));
		cb.addNewMessage(new ChatMessage("czarna mamba",
				ChatMessage.TYPE.SM_ONLINE));
		cb.addNewMessage(new ChatMessage("czarna mamba",
				ChatMessage.TYPE.SM_WROTE, "burak"));
		cb.addNewMessage(new ChatMessage("czarna mamba",
				ChatMessage.TYPE.SM_GUESSED));
		cb.addNewMessage(new ChatMessage("szpinak", ChatMessage.TYPE.SM_ONLINE));
	}
}
