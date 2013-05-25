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
		System.out.println("Connection handler is being created!");
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
		System.out.println("Actually handling: " + e);
		if (e instanceof LoginAttemptEvent) {
			handleLogIn((LoginAttemptEvent) e);
			return;
		}
		if (!loggedIn)
			return;
	}

	private void handleLogIn(LoginAttemptEvent e) {
		System.out.println("logging in");
		if (loggedIn)
			return;
		try {
			if(!server.login(e.getLogin(), this)) {
				System.out.println("response: WRONG!");
				out.writeObject(new LoginResponseEvent(e,false));
			}
			else {
				System.out.println("response: OK!");
				out.writeObject(new LoginResponseEvent(e,true));
			}
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		System.out.println("logging in done!");
	}

	@Override
	public void run() {
		try {
			System.out.println("It hangs on stream creation!");
			in = new ObjectInputStream(clientSocket.getInputStream());
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			System.out.println("or not?");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Streams were not created!");
			return;
		}
		while (true) {
			Object event = null;
			try {
				System.out.println("It hangs on reading!");
				event = in.readObject();
				System.out.println("Or not?");
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
