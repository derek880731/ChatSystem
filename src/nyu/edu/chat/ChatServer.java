package nyu.edu.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ChatServer implements Observable {

	ServerSocket server = null;
	List<Observer> clients = new LinkedList<Observer>();

	public ChatServer(int port) {
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to start the sever at port: " + port);
		}
	}

	public void connect() {
		while (true) {
			Socket clientSocket = null;
			try {
				clientSocket = server.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new ClientThread(this, clientSocket);
		}

	}

	@Override
	public void registerObserver(Observer observer, String string) {
		// TODO Auto-generated method stub
		clients.add(observer);
		observer.update(string);
	}

	@Override
	public void removeObserver(Observer observer, String string) {
		// TODO Auto-generated method stub
		ClientThread client = (ClientThread) observer;
		observer.update("Bye, " + client.name + ", have a nice day!!!");
		clients.remove(observer);
		for (Observer ob : clients) {
			ob.update(string);
		}
	}

	@Override
	public void notifyObserver(Observer observer, String string) {
		// TODO Auto-generated method stub
		for (Observer ob : clients) {
			if (ob != observer)
				ob.update(string);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ChatServer server = new ChatServer(8888);
		server.connect();
	}

}
