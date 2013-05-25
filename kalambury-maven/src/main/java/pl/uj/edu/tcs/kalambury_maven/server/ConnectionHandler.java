package pl.uj.edu.tcs.kalambury_maven.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.LoginAttemptEvent;
import pl.uj.edu.tcs.kalambury_maven.event.LoginResponseEvent;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOnlineEvent;

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
		server.getGameLogic().reactTo(myNick, e);
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
				server.getGameLogic().reactTo(myNick, new UsersOnlineEvent(myNick));
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
			} catch(EOFException | SocketException e) {
				if(loggedIn)
					server.logout(myNick);
				return;
			}
			catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
				continue;
			}
			if (!(event instanceof Event))
				continue;
			handleEvent((Event) event);
		}
	}
}
