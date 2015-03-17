package com.rbruno.textuploader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

public class Config {

	private boolean logConnect;
	private int port;

	public Config(String file) {
		try {
			phrase(file);
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
	}

	public void phrase(String file) throws JSONException, IOException {
		JSONObject json = null;

		BufferedReader reader = new BufferedReader(new FileReader(file));
		String jsonText = "";
		while (reader.ready()) {
			String line = reader.readLine();
			jsonText = jsonText + line;
		}

		json = new JSONObject(jsonText);
		port = json.getInt("port");
		logConnect = json.getBoolean("log-connect");
		reader.close();
	}

	public int getPort() {
		return port;
	}

	public boolean isLogConnect() {
		return logConnect;
	}

}

