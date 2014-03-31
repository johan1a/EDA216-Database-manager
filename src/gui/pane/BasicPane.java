package gui.pane;

import gui.tab.BasicTab;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import se.datadosen.component.RiverLayout;
import database.Database;

public abstract class BasicPane extends JPanel {
	private static final long serialVersionUID = 1;
	protected static final int FIELD_LENGTH = 8;
	protected static final String DATE_FORMAT = "yyyy-mm-dd";
	protected JTextArea resultArea;
	protected JTabbedPane tabbedPane;

	public BasicPane(Database db) {
		setLayout(new BorderLayout());

		tabbedPane = new JTabbedPane();
		tabbedPane.addChangeListener(new ChangeHandler());

		resultArea = new JTextArea();

		addTabs(db);

		add(resultArea, BorderLayout.CENTER);
		add(tabbedPane, BorderLayout.SOUTH);
	}

	protected abstract void addTabs(Database db);

	public void entryActions() {
		BasicTab pane = (BasicTab) tabbedPane.getSelectedComponent();
		pane.clearMessage();
		pane.entryActions();
	}

	public void displayMessage(String msg) {
		resultArea.setText(msg);
	}

	class ChangeHandler implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			try {
				BasicPane selectedPane = (BasicPane) tabbedPane
						.getSelectedComponent();
				selectedPane.entryActions();
			} catch (ClassCastException err) {
			}
		}
	}
}
