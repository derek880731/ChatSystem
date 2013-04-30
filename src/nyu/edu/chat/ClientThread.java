package nyu.edu.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread implements Observer, Runnable {

	Observable subject = null;
	Socket socket = null;
	Thread thread = null;
	String name = null;
	PrintWriter out = null;
	BufferedReader in = null;

	public ClientThread(Observable subject, Socket socket) {
		this.subject = subject;
		this.socket = socket;
		thread = new Thread(this);
		thread.start();

	}

	@Override
	public void update(String string) {
		// TODO Auto-generated method stub
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
			out.println("Please type your name:");
			String s = null;
			while ((s = in.readLine()) != null) {
				if (name == null) {
					name = s;
					subject.notifyObserver(this, name
							+ " just joined us. Welcome !!!");
					subject.registerObserver(this, "Welcome, " + name);
				} else {
					subject.notifyObserver(this, name + ": " + s);
				}
				if (s == "bye") {
					subject.removeObserver(this, name + " just left the room.");
					break;
				}
			}
			out.close();
			in.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
