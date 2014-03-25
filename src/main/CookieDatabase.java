package main;

import gui.GUI;
import database.Database;

public class CookieDatabase {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		new GUI(new Database());
	}
}
