package nyu.edu.chat.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import nyu.edu.chat.Observable;
import nyu.edu.chat.Observer;

public class ClientThread implements Observer, Runnable {

	private Observable subject = null;
	private Socket socket = null;
	private Thread thread = null;
	String name = null;
	private PrintWriter out = null;
	private BufferedReader in = null;

	public ClientThread(Observable subject, Socket socket) {
		this.subject = subject;
		this.socket = socket;
		try {
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream())), true);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		thread = new Thread(this);
		thread.start();

	}

	@Override
	public void update(String string) {
		// TODO Auto-generated method stub
		out.println(string);
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

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			
			out.println("Please type your name:");
			String s = null;
			while ((s = in.readLine()) != null) {
				if (s.equals("bye")) {
					subject.removeObserver(this, name + " just left the room.");
					break;
				}
				if (name == null) {
					name = s;
					subject.notifyObserver(this, name
							+ " just joined us. Welcome !!!");
					subject.registerObserver(this, "Welcome, " + name);
				} else {
					subject.notifyObserver(this, name + ": " + s);
				}
				
			}
			this.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
