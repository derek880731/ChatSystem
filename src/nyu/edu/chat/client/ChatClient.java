package nyu.edu.chat.client;

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
	PrintWriter out = null;
	BufferedReader in = null;
	private Socket socket = null;
	Thread thread = null;
	volatile boolean connected = true;

	public ChatClient(String host, int port) {
		try {
			socket = new Socket(host, port);
			// this solved the problem!!!!!
			socket.setSoTimeout(1000);
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

	public void disconnect() {
		try {
			out.close();
			in.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// can't close the thread by setting the connected to false;
	// How to stop a thread that is blocked on waiting input from socket????
	// by using socket.setSoTimeout(1000) which a read() call on the InputStream
	// associated with this Socket will block for only this amount of time
	public void stop() {
		connected = false;
	}

	public void write(String string) {
		out.println(string);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream())), true);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String inString = null;
		while (connected) {
			try {
				// looks like the thread is blocked here waiting for input
				// string and it will be closed when an input string comes.
				if ((inString = in.readLine()) != null)
					System.out.println(inString);
			} catch (IOException e) {
				// Have to use this to catch the SocketTimeoutException, the
				// socket will still be valid after this exception.
				Thread.currentThread().interrupt();
			}
		}
		this.disconnect();

	}

}
