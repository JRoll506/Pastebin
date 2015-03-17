package com.rbruno.textuploader;

import java.io.IOException;

public class Main {
	
	private WebUI webUI;

	public static void main(String[] args) {
		new Main();
	}


	public Main(){
		try {
			webUI = new WebUI(1345, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public WebUI getWebUI() {
		return webUI;
	}

}
