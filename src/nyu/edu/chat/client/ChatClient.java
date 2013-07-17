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
	boolean connected = true;

	public ChatClient(String host, int port) {
		try {
			// initialized input and output.
			socket = new Socket(host, port);
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream())), true);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			thread = new Thread(this);
			thread.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.err.println("Unknown host!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("IO Exception");
		}
	}


	/*
	 * Can't close the thread by setting the connected to false; 
	 * How to stop a thread that is blocked on waiting input from socket???? 
	 * Old solution:
	 * Using socket.setSoTimeout(1000) which a read() call on the InputStream
	 * associated with this Socket will block for only this amount of time.
	 * New solution: 
	 * Using socket.shutdownInput to wake up the thread that is
	 * blocked on reading input stream.
	 */
	public void stop() {
		// wake the thread.
		connected = false;
		try {
			socket.shutdownInput();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				out.close();
				in.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void write(String string) {
		out.println(string);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String inString = null;
		while (connected) {
			// The thread is blocked here waiting for input
			// string and it will wake up when an input string comes.
			// when the user want to exit, we need to wake up this thread first.
			try {
				if ((inString = in.readLine()) != null)
					System.out.println(inString);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
