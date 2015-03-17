package com.rbruno.textuploader.page;

import java.net.Socket;
import java.util.HashMap;

import com.rbruno.textuploader.Main;

public class Page {

	private String name;
	private Main main;
	private String file;

	private boolean staticPage;

	public Page(String name, Main main) {
		this.name = name;
		this.main = main;
		staticPage = false;
	}

	public Page(String name, String file, Main main) {
		this.name = name;
		this.file = file;
		this.main = main;
		staticPage = true;
	}

	public void called(Socket socket, String args, String post) {
	}

	public String read() {
		return main.getWebUI().read(this.getFile());
	}

	public HashMap<String, String> phraseGet(String args) {
		HashMap<String, String> map = new HashMap<String, String>();
		for (String string : args.split("\\&")) {
			if (string.split("\\=").length >=2){
				map.put(string.split("\\=")[0], string.split("\\=")[1]);
			}
		}
		return map;
	}

	public String normalHeader() {
		return "HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=UTF-8\r\n";
	}

	public Main getMain() {
		return main;
	}

	public String getName() {
		return name;
	}

	public String getFile() {
		return file;
	}

	public Boolean isStatic() {
		return staticPage;
	}

}
