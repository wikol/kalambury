package pl.uj.edu.tcs.kalambury_maven.model;

import java.util.ArrayList;
import java.util.List;

import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.MessageSendEvent;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOfflineEvent;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOnlineEvent;
import pl.uj.edu.tcs.kalambury_maven.event.WordGuessedEvent;
import pl.uj.edu.tcs.kalambury_maven.view.ChatBox;

/**
 * Model for ChatBox.
 * 
 * @author Anna Szybalska
 * 
 */
public class ChatMessagesList {
	private List<ChatMessage> messages;
	private ChatBox chatBox;

	public ChatMessagesList() {
		messages = new ArrayList<>();
	}

	public ChatMessagesList(List<ChatMessage> messages) {
		this.messages = new ArrayList<>(messages);
	}

	public void reactTo(Event e) {
		if (e.getClass().equals(MessageSendEvent.class)) {
			MessageSendEvent ev = (MessageSendEvent) e;
			messages.add(new ChatMessage(ev.getUser(),
					ChatMessage.TYPE.SM_WROTE, ev.getMessage()));

		} else if (e.getClass().equals(UsersOnlineEvent.class)) {
			UsersOnlineEvent ev = (UsersOnlineEvent) e;
			for (String user : ev.getUsersOnline()) {
				messages.add(new ChatMessage(user, ChatMessage.TYPE.SM_ONLINE));
			}

		} else if (e.getClass().equals(UsersOfflineEvent.class)) {
			UsersOfflineEvent ev = (UsersOfflineEvent) e;
			for (String user : ev.getUsersOffline()) {
				messages.add(new ChatMessage(user, ChatMessage.TYPE.SM_OFFLINE));
			}

		} else if (e.getClass().equals(WordGuessedEvent.class)) {
			WordGuessedEvent ev = (WordGuessedEvent) e;
			messages.add(new ChatMessage(ev.getUser(),
					ChatMessage.TYPE.SM_GUESSED));

		} else {
			// throw new
			// EventNotHandledException(e.getClass().toString()+" not supported by "+this.getClass().toString());
		}
		if (chatBox != null) {
			chatBox.updateChatBox();
		}
	}

	/**
	 * Returned list should not be modified!
	 * 
	 * @return list of chat messages
	 */
	public List<ChatMessage> getMessagesList() {
		return messages;
	}

	public void setView(ChatBox cb) {
		this.chatBox = cb;
	}
}
