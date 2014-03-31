package gui.tab;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import se.datadosen.component.RiverLayout;
import database.Database;

public abstract class BasicTab extends JPanel {
	protected static final long serialVersionUID = 1L;
	protected static final String DATE_FORMAT = "yyyy-mm-dd";
	protected static final int FIELD_LENGTH = 8;
	protected JTextArea resultArea;
	Database db;
	
	public BasicTab(Database db, JTextArea resultArea) {
		this.resultArea = resultArea;
		this.db = db;
		setLayout(new RiverLayout());
	}
	
	
	public abstract void entryActions();
	
	public void clearMessage() {
		resultArea.setText(" ");
	}

}
