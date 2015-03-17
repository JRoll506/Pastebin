package com.rbruno.textuploader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.rbruno.textuploader.logger.Logger;
import com.rbruno.textuploader.page.Page;
import com.rbruno.textuploader.page.PageManager;

public class WebUI implements Runnable {

	private int port;
	private Main main;
	private ServerSocket socket;
	private Thread run;
	private PageManager pageManager;

	public WebUI(int port, Main main) throws IOException {
		this.port = port;
		this.main = main;

		socket = new ServerSocket(port);
		Logger.log("Started webUI on port: " + port);

		pageManager = new PageManager(main);

		run = new Thread(this, "WebServer");
		run.start();
	}

	public void run() {
		while (true) {
			try {
				Socket clientSocket = socket.accept();
				Logger.log("Web Socket created " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
				Thread client = new Thread(new WebClient(clientSocket, main), "WebClient");
				client.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void process(Socket socket, String data, String post) {
		PrintWriter out;
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		data = data.split(" ")[1];
		String get = "";
		if (data.contains("?")) {
			if (data.split("\\?").length >= 2) {
				get = data.split("\\?")[1];
			}
			data = data.split("\\?")[0];

		}

		for (Page page : this.getPageManager().pages) {
			if (page.getName().equals(data)) {
				if (page.isStatic()) {
					out.println(page.normalHeader());
					out.println();
					out.println(page.read());
				} else {
					page.called(socket, get, post);
				}
				return;
			}
		}

		File file = new File("data/" + data + ".txt");
		if (file.exists()) {
			out.print("HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=UTF-8\r\n\r\n");
			out.println("<html>\n<head>\n<title>"+ data +"</title>\n</head>\n<body>\n<pre style=\"word-wrap: break-word; white-space: pre-wrap;\">");
			out.print(read("data/" + data + ".txt").substring(1).replace("<", "&#60;").replace(">", "&#62;"));
			out.println("</pre>\n</body>\n</html>");
			return;
		}

		out.println("HTTP/1.1 404 UNFOUND");
		out.println();
		out.println("<head><title>404 NOT FOUND</title></head>");
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.close();
	}

	public String read(String file) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(file)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String html = "";
		try {
			while (reader.ready()) {
				String line = reader.readLine();
				html = html + "\n" + line;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return html;
	}

	public int getPort() {
		return port;
	}

	public Main getMain() {
		return main;
	}

	public PageManager getPageManager() {
		return pageManager;
	}
}
