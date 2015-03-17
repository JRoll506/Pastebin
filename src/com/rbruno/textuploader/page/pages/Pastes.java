package com.rbruno.textuploader.page.pages;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;

import com.rbruno.textuploader.Main;
import com.rbruno.textuploader.logger.Logger;
import com.rbruno.textuploader.page.Page;

public class Pastes extends Page {

	public Pastes(Main main) {
		super("/pastes", main);
	}

	public void called(Socket socket, String args, String post) {
		PrintWriter out;
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		HashMap<String, String> postMap = this.phraseGet(post);
		File file = new File("data/" + postMap.get("title") + ".txt");
		if (file.exists()) {
			out.println(this.normalHeader());
			out.println();
			out.println("Name already take");
			return;
		}
		try {
			PrintWriter fileOut = new PrintWriter(new BufferedWriter(new FileWriter("data/" + postMap.get("title") + ".txt", true)));
			fileOut.print(URLDecoder.decode(postMap.get("pastebody"), "UTF-8"));
			Logger.log("Has just pasted " + postMap.get("title"), socket);
			fileOut.close();
		} catch (IOException e) {
			out.println("An unknown error has occured");
			return;
		}
		out.println("HTTP/1.1 301 Moved Permanently\r\nLocation: " + postMap.get("title") + "\n\r");
	}

}
