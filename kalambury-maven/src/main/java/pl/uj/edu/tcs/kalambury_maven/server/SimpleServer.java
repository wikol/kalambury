package pl.uj.edu.tcs.kalambury_maven.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.uj.edu.tcs.kalambury_maven.event.Event;

public class SimpleServer implements Server {

	private ServerSocket socket;
	private NewConnectionListener listener;
	private Thread listenerThread;
	private ExecutorService threads;
	private Map<String, ConnectionHandler> nicks;
	private DummyGameLogic logic;

	public SimpleServer(int port) throws IOException {
		socket = new ServerSocket(port);
		listener = new NewConnectionListener(this);
		threads = Executors.newCachedThreadPool();
		logic = new DummyGameLogic();
	}

	ServerSocket getSocket() {
		return socket;
	}

	boolean login(String nickname, ConnectionHandler who) {
		if (nicks.containsKey(nickname))
			return false;
		nicks.put(nickname, who);
		who.setLoggedIn(true);
		return true;
	}

	DummyGameLogic getGameLogic() {
		return logic;
	}

	void nextConnection(Socket clientSocket) {
		threads.execute(new ConnectionHandler(clientSocket, this));
	}

	public void listen() {
		listenerThread = new Thread(listener);
		listenerThread.start();
	}

	@Override
	public void sendEvent(String name, Event event) {
		try {
			nicks.get(name).getOutputStream().writeObject(event);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void broadcastEvent(Event event) {
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
	
	public static void main(String[] args) {
		SimpleServer serv = null;
		try {
			serv = new SimpleServer(8888);
		} catch(IOException e) {
			e.printStackTrace();
			return;
		}
		serv.listen();
		serv.close();
	}

}
