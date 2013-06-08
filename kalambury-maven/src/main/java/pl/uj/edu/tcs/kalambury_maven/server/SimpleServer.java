package pl.uj.edu.tcs.kalambury_maven.server;

import java.awt.EventQueue;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOfflineEvent;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOnlineEvent;
import pl.uj.edu.tcs.kalambury_maven.event.WordGuessedEvent;
import pl.uj.edu.tcs.kalambury_maven.view.AppView;
import pl.uj.edu.tcs.kalambury_maven.view.LoginWindow;
import pl.uj.edu.tcs.kalambury_maven.view.SimpleServerGui;

public class SimpleServer implements Server {

	private ServerSocket socket;
	private NewConnectionListener listener;
	private Thread listenerThread;
	private ExecutorService threads;
	private Map<String, ConnectionHandler> nicks;
	private GameLogic logic;
	private SimpleServerGui simpleGui;

	public SimpleServer(int port) throws IOException {
		socket = new ServerSocket(port);
		listener = new NewConnectionListener(this);
		threads = Executors.newCachedThreadPool();
		logic = new GameLogic();
		nicks = new ConcurrentHashMap<>();
		
		logic.setServer(this);
		displayGui();
	}
	
	private void displayGui() {
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					simpleGui = new SimpleServerGui(getGameLogic());
					simpleGui.setVisible(true);
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}

	ServerSocket getSocket() {
		return socket;
	}

	boolean login(String nickname, ConnectionHandler who) {
		System.out.println("Server is considering...");
		if (nicks.containsKey(nickname)) {
			System.out.println("False was returned!");
			return false;
		}
		System.out.println("Still at server side...");
		nicks.put(nickname, who);
		who.setLoggedIn(true);
		who.setMyNick(nickname);
		return true;
	}

	GameLogic getGameLogic() {
		return logic;
	}

	void nextConnection(Socket clientSocket) {
		System.out.println("Next connection actually sparked!");
		threads.submit(new ConnectionHandler(clientSocket, this));
	}
	
	void logout(String nickname) {
		nicks.remove(nickname);
		logic.reactTo(nickname, new UsersOfflineEvent(nickname));
	}

	public void listen() {
		listenerThread = new Thread(listener);
		listenerThread.start();
	}

	@Override
	public synchronized void sendEvent(String name, Event event) {
		try {
			nicks.get(name).getOutputStream().writeObject(event);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void broadcastEvent(Event event) {
		for (ConnectionHandler conn : nicks.values()) {
			try {
				conn.getOutputStream().writeObject(event);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void close() {
		listenerThread.interrupt();
		threads.shutdownNow();
	}

	/**
	 * For testing purposes only
	 */
	private void testConnection() {
		List<String> names = Arrays.asList(new String[] { "Michał Glapa",
				"Karol Kaszuba", "Kamil Rychlewicz", "Piotr Pakosz" });
		broadcastEvent(new UsersOnlineEvent(names));
		List<String> pakosz = Arrays.asList(new String[] { "Piotr Pakosz" });
//		for (int i = 0; i < 350; i++)
//			broadcastEvent(new WordGuessedEvent("Laser", "Karol Kaszuba"));
//		for (int i = 0; i < 280; i++)
//			broadcastEvent(new WordGuessedEvent("Laser", "Michał Glapa"));
//		for (int i = 0; i < 370; i++)
//			broadcastEvent(new WordGuessedEvent("Laser", "Kamil Rychlewicz"));
		broadcastEvent(new UsersOfflineEvent(pakosz));

	}
	
	public static void start(int port)
	{
		SimpleServer serv = null;
		try {
			serv = new SimpleServer(port);
			System.out.println("Server started!");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		serv.listen();
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serv.testConnection();
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serv.close();
	}

	public static void main(String[] args) {
		SimpleServer serv = null;
		try {
			serv = new SimpleServer(8888);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		serv.listen();
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serv.testConnection();
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serv.close();
	}
}
