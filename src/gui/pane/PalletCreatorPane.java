package gui.pane;

import gui.tab.CreatePalletTab;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import se.datadosen.component.RiverLayout;
import util.Util;
import database.Database;

@SuppressWarnings("synthetic-access")
public class PalletCreatorPane extends BasicPane {
	private static final long serialVersionUID = 1;

	public PalletCreatorPane(Database db) {
		super(db);
	}

	@Override
	protected void addTabs(Database db) {
		tabbedPane.add(new CreatePalletTab(db, resultArea), "Create Pallets");
	}
}
