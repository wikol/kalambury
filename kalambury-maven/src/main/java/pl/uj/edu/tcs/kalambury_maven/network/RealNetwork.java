package pl.uj.edu.tcs.kalambury_maven.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pl.uj.edu.tcs.kalambury_maven.controller.AppController;
import pl.uj.edu.tcs.kalambury_maven.event.Event;

public class RealNetwork implements Network {

	private AppController controller;
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private InputListener listener;
	private Thread listenerThread;
	
	private class InputListener implements Runnable {

		@Override
		public void run() {
			while(true) {
				Object ev;
				try {
					ev = input.readObject();
					System.out.println("I actually read something!");
				} catch(ClassNotFoundException | IOException e) {
					e.printStackTrace();
					continue;
				}
				Event event = (Event) ev;
				controller.reactTo(event);
			}
		}
		
	}

	public RealNetwork(String server, String port, AppController c)
			throws UnableToConnectException {
		controller = c;
		int realPort;
		try {
			realPort = Integer.parseInt(port);
		} catch (NumberFormatException ex) {
			throw new UnableToConnectException();
		}
		try {
			socket = new Socket(server, realPort);
		} catch (IOException e) {
			throw new UnableToConnectException();
		}
		try {
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			throw new UnableToConnectException();
		}
		listener = new InputListener();
		listenerThread = new Thread(listener);
		listenerThread.start();
	}

	@Override
	public void sendToServer(Event e) {
		try {
			output.writeObject(e);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
}
