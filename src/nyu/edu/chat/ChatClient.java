package nyu.edu.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {

	public static void main(String[] args) {
		PrintWriter out = null;
		BufferedReader in = null;
		Socket socket = null;
		try {
			socket = new Socket("localhost", 8888);
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream())), true);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			BufferedReader stdin = new BufferedReader(new InputStreamReader(
					System.in));
			String inString = null;
			// Here has a problem. Need two thread, one for updating the inputs
			// from other users and one for writing output.
			while ((inString = in.readLine()) != null) {

				System.out.println(inString);
				String outString = null;
				if ((outString = stdin.readLine()) != null) {
					if (outString == "bye") {
						break;
					}
					out.println(outString);
				}
			}
			out.close();
			in.close();
			stdin.close();
			socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
