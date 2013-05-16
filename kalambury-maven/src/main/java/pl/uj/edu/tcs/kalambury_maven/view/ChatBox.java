package pl.uj.edu.tcs.kalambury_maven.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventHandler;
import pl.uj.edu.tcs.kalambury_maven.event.MessageSendEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewMessageWrittenEvent;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOfflineEvent;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOnlineEvent;
import pl.uj.edu.tcs.kalambury_maven.event.WordGuessedEvent;

/**
 * Box to display chat. It distributes NewMessageWrittenEvent, when someone send
 * new message.
 * 
 * @author Anna Szybalska
 * 
 */
public class ChatBox extends JPanel implements EventHandler{
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
		userInputField.addActionListener(new SendAction());
		userInputField.setMaximumSize(new Dimension(Integer.MAX_VALUE,
				userInputField.getPreferredSize().height));
		userInputField.setAlignmentX(Component.LEFT_ALIGNMENT);
		// textInput
		sendButton = new JButton("Send");
		sendButton.addActionListener(new SendAction());
		sendButton.setAlignmentX(Component.RIGHT_ALIGNMENT);

		sendingArea.add(userInputField);
		sendingArea.add(sendButton);
		// sendingArea

		add(sendingArea);

	}
	
	@Override
	public void handle(Event e) {
		if (e.getClass().equals(MessageSendEvent.class)) {
			MessageSendEvent ev = (MessageSendEvent) e;
			addNewMessage(new ChatMessage(ev.getUser(),
					ChatMessage.TYPE.SM_WROTE, ev.getMessage()));
			
		} else if (e.getClass().equals(UsersOnlineEvent.class)) {
			UsersOnlineEvent ev = (UsersOnlineEvent) e;
			for (String user : ev.getUsersOnline()) {
				addNewMessage(new ChatMessage(user,
						ChatMessage.TYPE.SM_ONLINE));
			}
			
		} else if (e.getClass().equals(UsersOfflineEvent.class)) {
			UsersOfflineEvent ev = (UsersOfflineEvent) e;
			for (String user : ev.getUsersOffline()) {
				addNewMessage(new ChatMessage(user,
						ChatMessage.TYPE.SM_OFFLINE));
			}
			
		} else if (e.getClass().equals(WordGuessedEvent.class)) {
			WordGuessedEvent ev = (WordGuessedEvent) e;
			addNewMessage(new ChatMessage(ev.getUser(),
						ChatMessage.TYPE.SM_GUESSED));
			
		} else {
			// throw new
			// EventNotHandledException(e.getClass().toString()+" not supported by "+this.getClass().toString());
		}
	}

	private class SendAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			ChatBox.this.sendCurrentMessage();
		}

	}

	private void sendCurrentMessage() {
		String message = userInputField.getText().trim();
		if (!message.equals("")) {
			// TODO get current user name itd.
			String currUser = "currUser";
			NewMessageWrittenEvent event = new NewMessageWrittenEvent(currUser,
					userInputField.getText());
			// TODO send event and remove this:
			this.handle(new MessageSendEvent(currUser, message));

			userInputField.setText("");
		}
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

			setForeground(colors[type.ordinal()]);
			switch (type) {
			case SM_WROTE:
				message = what[0];
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
			updateText();

			addComponentListener(new ComponentListener() {
				@Override
				public void componentShown(ComponentEvent arg0) {
				}

				@Override
				public void componentResized(ComponentEvent arg0) {
					ChatMessage.this.updateText();
				}

				@Override
				public void componentMoved(ComponentEvent arg0) {
				}

				@Override
				public void componentHidden(ComponentEvent arg0) {
				}
			});
		}

		private void updateText() {
			String nick = who;
			if (type == TYPE.SM_WROTE) {
				nick += ":";
			}
			String text = String.format(
					"<html><div WIDTH=%d><b>%s</b> %s</div></html>",
					getSize().width - 20, nick, message);
			setText(text);
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

		cb.handle(new UsersOnlineEvent("inny"));
		cb.handle(new MessageSendEvent("inny", "lubię placki"));
		cb.handle(new UsersOnlineEvent("zosiek", "titanic", "drzewo", "krzak", "dzik", "biedronka"));
		cb.handle(new UsersOfflineEvent("titanic"));
		cb.handle(new UsersOnlineEvent("budzik", "pietruszka", "zielony_groszek"));
		cb.handle(new UsersOfflineEvent("budzik"));
		cb.handle(new UsersOnlineEvent("czarna_mamba"));
		cb.handle(new MessageSendEvent("czarna_mamba", "burak"));
		cb.handle(new WordGuessedEvent("burak", "czarna_mamba"));
		cb.handle(new UsersOnlineEvent("szpinak"));
	}

}
