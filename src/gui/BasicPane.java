package gui;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import se.datadosen.component.RiverLayout;
import database.Database;

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
