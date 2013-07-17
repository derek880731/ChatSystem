package nyu.edu.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import nyu.edu.chat.Observable;
import nyu.edu.chat.Observer;

public class ChatServer implements Observable {

	// no need to make synchronize on this
	private static ChatServer INSTANCE;
	private ServerSocket server = null;
	private boolean connected = false;
	private int port = 0;
	// the access to the clients list should be synchronized as it is shared by
	// client threads.
	private List<Observer> clients = null;

	private ChatServer(int port) {
		this.port = port;
		// Better for cases where traversal is the dominant operations.
		clients = new CopyOnWriteArrayList<Observer>();
	}

	// Singleton and Factory pattern, no need to synchronize this method as
	// server will not be created by several threads.
	public static ChatServer getInstance(int port) {
		if (INSTANCE == null) {
			INSTANCE = new ChatServer(port);
		}
		return INSTANCE;

	}

	public void disconnect() {
		if (connected) {
			connected = false;
			for (Observer observer : clients) {
				ClientThread client = (ClientThread) observer;
				client.disconnect();
				clients.remove(observer);
			}
		}
		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void connect() {
		if (connected) {
			return;
		}
		try {
			server = new ServerSocket(port);
			connected = true;
			while (server.isBound() && connected) {
				Socket clientSocket = server.accept();
				new ClientThread(this, clientSocket);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void registerObserver(Observer observer, String string) {
		clients.add(observer);
		observer.update(string);
	}

	@Override
	public void removeObserver(Observer observer, String string) {
		ClientThread client = (ClientThread) observer;
		observer.update("Bye, " + client.name + ", have a nice day!!!");
		clients.remove(observer);
		for (Observer ob : clients) {
			ob.update(string);
		}
	}

	@Override
	public void notifyObserver(Observer observer, String string) {
		for (Observer ob : clients) {
			if (ob != observer)
				ob.update(string);
		}
	}

}
