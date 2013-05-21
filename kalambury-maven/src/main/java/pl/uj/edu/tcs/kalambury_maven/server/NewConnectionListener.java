package pl.uj.edu.tcs.kalambury_maven.server;

import java.io.IOException;
import java.net.Socket;

public class NewConnectionListener implements Runnable {

	SimpleServer server;
	public NewConnectionListener(SimpleServer server) {
		this.server = server;
	}
	@Override
	public void run() {
		while(true) {
			Socket clientSocket = null;
			try {
				clientSocket = server.getSocket().accept();
				server.nextConnection(clientSocket);
				if(Thread.currentThread().isInterrupted())
					return;
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

}
