package nyu.edu.chat.server;

public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ChatServer server = ChatServer.getInstance(8888);
		server.connect();
	}
}
