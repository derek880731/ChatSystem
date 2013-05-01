package nyu.edu.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import nyu.edu.chat.Observable;
import nyu.edu.chat.Observer;

public class ChatServer implements Observable {

	// making it volatile so that every thread can see the latest state of it.
	// the Double-Checked Locking idiom can be made to work by declaring the
	// field to be volatile
	private static volatile ChatServer INSTANCE;
	private ServerSocket server = null;
	private volatile boolean connected = false;
	private int port = 0;
	private List<Observer> clients = Collections
			.synchronizedList(new LinkedList<Observer>());

	private ChatServer(int port) {
		this.port = port;
	}

	// Singleton and Factory pattern
	public static ChatServer getInstance(int port) {
		// By using double check, we only synchronize the critical section, it
		// avoid synchronization after the instance is allocated.
		// which has better performance than making the whole method
		// synchronzied.
		if (INSTANCE == null) {
			synchronized (ChatServer.class) {
				if (INSTANCE == null) {
					INSTANCE = new ChatServer(port);
				}
			}
		}
		return INSTANCE;

	}

	public void disconnect() {
		if (connected) {
			// need using synchronized block when iterating through the list.
			synchronized (clients) {
				for (Observer observer : clients) {
					clients.remove(observer);
				}
			}
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
		synchronized (clients) {
			for (Observer ob : clients) {
				ob.update(string);
			}
		}
	}

	@Override
	public void notifyObserver(Observer observer, String string) {
		synchronized (clients) {
			for (Observer ob : clients) {
				if (ob != observer)
					ob.update(string);
			}
		}
	}

}
