package nyu.edu.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) {

		ChatClient client = new ChatClient("localhost", 8888);

		BufferedReader stdin = new BufferedReader(new InputStreamReader(
				System.in));
		try {
			String outString = null;
			while ((outString = stdin.readLine()) != null) {
				client.write(outString);
				if (outString.equals("bye")) {
					//need to wake up the blocking thread first.
					break;
				}
			}
			client.stop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				stdin.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
