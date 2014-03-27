package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import se.datadosen.component.RiverLayout;
import database.Database;

public abstract class BasicPane extends JPanel {
	private static final long serialVersionUID = 1;
	protected static final int FIELD_LENGTH = 8;
	protected Database db;
	protected JTextField[] fields;
	private JTextArea resultArea;
	protected JList<String> list;
	private String[] productNames;
	private JScrollPane listScroller;
	protected static final String DATE_FORMAT = "yyyy-mm-dd";

	public BasicPane(Database db) {
		this.db = db;
		setLayout(new BorderLayout());

		resultArea = createResultPanel();
		InputPanels inputPanel = createInputPanel();
		JComponent buttonPanel = createButtonPanel();

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new RiverLayout());
		bottomPanel.add("left", createList());
		bottomPanel.add("", inputPanel);

		bottomPanel.add("left br", buttonPanel);

		add(resultArea, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
	}

	public JScrollPane createList() {
		list = new JList<String>();
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);

		listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(250, 80));
		return listScroller;
	}

	protected void showProductNames() {
		productNames = db.getProductNames();
		list.setListData(productNames);
		listScroller.revalidate();
		listScroller.repaint();
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
