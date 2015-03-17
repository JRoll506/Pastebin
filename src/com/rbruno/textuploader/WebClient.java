package com.rbruno.textuploader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class WebClient implements Runnable {

	private Main main;

	private Socket clientSocket;
	private BufferedReader in;
	private PrintWriter out;

	public WebClient(Socket clientSocket, Main main) throws IOException {
		this.main = main;
		this.clientSocket = clientSocket;
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		out = new PrintWriter(clientSocket.getOutputStream(), true);
	}

	@Override
	public void run() {
		ArrayList<String> message = new ArrayList<String>();
		String line;
		String post = "";
		try {
			while ((line = in.readLine()) != null) {
				if (line.length() == 0) {
					for (String current : message) {
						if (current.startsWith("Content-Length")) {
							if (current.split(": ").length >= 2) {
								char[] buffer = new char[Integer.parseInt(current.split(": ")[1])];
								in.read(buffer, 0, Integer.parseInt(current.split(": ")[1]));
								post = new String(buffer);
							}
						}
					}
					break;
				}
				message.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (message != null) {
			if (message.size() > 0)
				main.getWebUI().process(clientSocket, message.get(0), post);
		}

		try {
			out.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
