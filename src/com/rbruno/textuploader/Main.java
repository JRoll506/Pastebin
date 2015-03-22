package com.rbruno.textuploader;

import java.io.IOException;

public class Main {

	private WebUI webUI;
	private Config config;

	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		config = new Config("config.txt");
		try {
			webUI = new WebUI(config.getPort() , this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public WebUI getWebUI() {
		return webUI;
	}

	public Config getConfig() {
		return config;
	}

}
