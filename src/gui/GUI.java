package gui;

import javax.swing.*;
import javax.swing.event.*;

import database.Database;

import java.awt.*;
import java.awt.event.*;

public class GUI {
	private Database db;
	private JTabbedPane tabbedPane;

	public GUI(Database db) {
		this.db = db;
		JFrame frame = new JFrame("Cookie Database");
		tabbedPane = new JTabbedPane();

		PalletManagerPane palletManagerPane = new PalletManagerPane(db);
		tabbedPane.addTab("Pallet manager", null, palletManagerPane,
				"Manage pallets");

		PalletCreatorPane palletCreatorPane = new PalletCreatorPane(db);
		tabbedPane.addTab("Pallet creator", null, palletCreatorPane,
				"Simulate pallet creation");

		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		tabbedPane.addChangeListener(new ChangeHandler());

		frame.addWindowListener(new WindowHandler());
		frame.setSize(800, 600);
		frame.setVisible(true);

		palletManagerPane.displayMessage("Connecting to database ...");

		if (db.openConnection("db57", "dA1AbAs")) {
			palletManagerPane.displayMessage("Connected to database");
		} else {
			palletManagerPane.displayMessage("Could not connect to database");
		}
	}

	class ChangeHandler implements ChangeListener {
		@SuppressWarnings("synthetic-access")
		public void stateChanged(ChangeEvent e) {
			BasicPane selectedPane = (BasicPane) tabbedPane
					.getSelectedComponent();
			selectedPane.entryActions();
		}
	}

	class WindowHandler extends WindowAdapter {
		@SuppressWarnings("synthetic-access")
		public void windowClosing(WindowEvent e) {
			db.closeConnection();
			System.exit(0);
		}
	}
}
