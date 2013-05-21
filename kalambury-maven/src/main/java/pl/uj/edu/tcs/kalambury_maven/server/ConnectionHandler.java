package pl.uj.edu.tcs.kalambury_maven.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.LoginAttemptEvent;
import pl.uj.edu.tcs.kalambury_maven.event.LoginResponseEvent;

public class ConnectionHandler implements Runnable {
	private boolean loggedIn = false;
	private String myNick;
	private SimpleServer server;
	private Socket clientSocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	public ConnectionHandler(Socket clientSocket, SimpleServer server) {
		this.server = server;
		this.clientSocket = clientSocket;
	}

	public void setLoggedIn(boolean l) {
		loggedIn = l;
	}

	public void setMyNick(String s) {
		myNick = s;
	}

	public ObjectOutputStream getOutputStream() {
		return out;
	}

	private void handleEvent(Event e) {
		if (e instanceof LoginAttemptEvent) {
			handleLogIn((LoginAttemptEvent) e);
			return;
		}
		if (!loggedIn)
			return;
	}

	private void handleLogIn(LoginAttemptEvent e) {
		if (loggedIn)
			return;
		try {
			if(!server.login(e.getLogin(), this))
				out.writeObject(new LoginResponseEvent(e,false));
			else
				out.writeObject(new LoginResponseEvent(e,true));
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			in = new ObjectInputStream(clientSocket.getInputStream());
			out = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		while (true) {
			Object event = null;
			try {
				event = in.readObject();
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
				continue;
			}
			if (!(event instanceof Event))
				continue;
			handleEvent((Event) event);
		}
	}
}
