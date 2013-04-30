package nyu.edu.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Derek
 *         <p>
 *         ChatClient with two threads: one thread for collecting all the inputs
 *         from other users and one thread for handling user inputs.
 */
public class ChatClient implements Runnable {
	private PrintWriter out = null;
	private BufferedReader in = null;
	private Socket socket = null;
	Thread thread = null;

	public ChatClient(int port) {
		try {
			socket = new Socket("localhost", port);
			thread = new Thread(this);
			thread.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			out.close();
			in.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		ChatClient client = new ChatClient(8888);

		BufferedReader stdin = new BufferedReader(new InputStreamReader(
				System.in));
		try {
			String outString = null;
			while ((outString = stdin.readLine()) != null) {
				if (outString == "bye") {
					break;
				}
				client.out.println(outString);
			}
			stdin.close();
			client.stop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream())), true);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String inString = null;
			while ((inString = in.readLine()) != null) {

				System.out.println(inString);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
