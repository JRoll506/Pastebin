package com.rbruno.textuploader.page;

import java.util.ArrayList;

import com.rbruno.textuploader.Main;
import com.rbruno.textuploader.page.pages.Pastes;

public class PageManager {

	public ArrayList<Page> pages = new ArrayList<Page>();

	public PageManager(Main main) {
		this.pages.add(new Page("/", "index.txt", main));
		this.pages.add(new Pastes(main));
	}
}
