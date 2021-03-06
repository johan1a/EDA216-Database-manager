package gui;

import gui.pane.BasicPane;
import gui.pane.PalletCreatorPane;
import gui.pane.PalletManagerPane;

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
		PalletCreatorPane palletCreatorPane = new PalletCreatorPane(db);
		
		tabbedPane.addTab("Pallet manager", null, palletManagerPane,
				"Manage pallets");
		tabbedPane.addTab("Pallet creator", null, palletCreatorPane,
				"Simulate pallet creation");

		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		tabbedPane.addChangeListener(new ChangeHandler());

		frame.addWindowListener(new WindowHandler());
		frame.setSize(800, 600);
		frame.setVisible(true);

		palletManagerPane.displayMessage("Connecting to database ...");

		if (db.isConnected()) {
			palletManagerPane.displayMessage("Connected to database");
		} else {
			palletManagerPane.displayMessage("Could not connect to database");
		}

		palletManagerPane.entryActions();
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
