package main;

import gui.GUI;
import database.Database;

public class CookieDatabase {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		if (args.length == 3) {
			new GUI(new Database(args[0], args[1], args[2]));
		} else {
			System.out
					.println("usage: cookieDatabase <database url, e.g. localhost:3306> <database username> <database password>");
		}
	}
}
