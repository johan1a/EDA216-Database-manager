package gui;

import javax.swing.*;
import javax.swing.border.*;

import se.datadosen.component.RiverLayout;
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
public abstract class BasicPane extends JPanel {
	private static final long serialVersionUID = 1;
	protected static final int FIELD_LENGTH = 8;
	protected Database db;
	protected JTextField[] fields;
	private JTextArea resultArea;

	public BasicPane(Database db) {
		this.db = db;
		setLayout(new BorderLayout());

		resultArea = createResultPanel();
		InputPanels inputPanel = createInputPanel();
		JComponent buttonPanel = createButtonPanel();

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new RiverLayout());
		bottomPanel.add(inputPanel);
		bottomPanel.add("br", buttonPanel);

		add(resultArea, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
	}

	public abstract InputPanels createInputPanel();

	public static JTextArea createResultPanel() {
		return new JTextArea();
	}

	public abstract JComponent createButtonPanel();

	public abstract void entryActions();

	public void displayMessage(String msg) {
		resultArea.setText(msg);
	}

	public void clearMessage() {
		resultArea.setText(" ");
	}
}
