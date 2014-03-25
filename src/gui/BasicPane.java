package gui;

import javax.swing.*;
import javax.swing.border.*;

import database.Database;

import java.awt.*;

/**
 * BasicPane is a pane in the user interface. It consists of two subpanels: the
 * left panel and the right panel. The right panel in turn consist of three
 * panels on top of each other: bottom, middle and top. Subclasses can choose to
 * configure these panels as they wish.
 * <p>
 * The class contains a reference to the database object, so subclasses can
 * communicate with the database.
 */
public class BasicPane extends JPanel {
	private static final long serialVersionUID = 1;
	/**
	 * The database object.
	 */
	protected Database db;
	

	/**
	 * A label which is intended to contain a message text.
	 */

	private JTextArea resultArea;

	/**
	 * Create a BasicPane object.
	 *
	 * @param db
	 *            The database object.
	 */
	public BasicPane(Database db) {
		this.db = db;
		resultArea = createResultPanel();
		
		
		
		setLayout(new BorderLayout());
		JComponent inputPanel = createInputPanel();
		
		JComponent buttonPanel = createButtonPanel();

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(2, 1));
		bottomPanel.add(inputPanel);
		bottomPanel.add(buttonPanel);

		add(resultArea,BorderLayout.CENTER);
		add(bottomPanel,BorderLayout.SOUTH);
	}

	/**
	 * Create the top panel. Should be overridden by subclasses.
	 *
	 * @return An empty panel.
	 */
	public JComponent createInputPanel() {
		return new JPanel();
	}

	/**
	 * Create the middle panel. Should be overridden by subclasses.
	 *
	 * @return An empty panel.
	 */
	public JTextArea createResultPanel() {
		return new JTextArea();
	}

	/**
	 * Create the bottom panel. Should be overridden by subclasses.
	 *
	 * @return An empty panel.
	 */
	public JComponent createButtonPanel() {
		return new JPanel();
	}

	/**
	 * Perform the entry actions of the pane. Empty here, should be overridden
	 * by subclasses.
	 */
	public void entryActions() {
	}

	/**
	 * Display a message.
	 *
	 * @param msg
	 *            The message to display.
	 */
	public void displayMessage(String msg) {
		resultArea.setText(msg);
	}

	/**
	 * Clear the message line.
	 */
	public void clearMessage() {
		resultArea.setText(" ");
	}
}
