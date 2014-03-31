package gui.tab;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import database.Database;
import se.datadosen.component.RiverLayout;
import util.Util;

@SuppressWarnings("synthetic-access")
public class SearchByPalletNumberTab extends BasicTab {
	private static final long serialVersionUID = 1L;
	private static final int SEARCH = 0, BLOCK = 1;
	private final static int FIELD_LENGTH = 8;
	private static int NBR_BUTTONS = 2;
	private static JTextField field;
	private int barcodeID;

	public SearchByPalletNumberTab(Database db, JTextArea resultArea) {
		super(db, resultArea);

		add("", createPalletNumberSearchPanel());

		add("left br", createButtonPanel());

	}

	private static JPanel createPalletNumberSearchPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new RiverLayout());

		field = new JTextField(FIELD_LENGTH);

		panel.add("br", new JLabel("Pallet number: "));
		panel.add("tab", field);

		return panel;
	}

	public JComponent createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		JButton[] buttons = new JButton[NBR_BUTTONS];

		buttons[SEARCH] = new JButton("Search for pallet");
		buttons[BLOCK] = new JButton("Block pallet");

		buttons[SEARCH].addActionListener(new PalletNumberSearchHandler());
		buttons[BLOCK].addActionListener(new PalletNumberBlockHandler());

		for (JButton button : buttons) {
			buttonPanel.add(button);
		}
		return buttonPanel;
	}

	@Override
	public void entryActions() {

	}

	private void readInput() {
		try {
			String temp = field.getText();
			if (temp != null) {
				barcodeID = Integer.parseInt(temp);
			} else {
				barcodeID = -1;
			}

		} catch (NumberFormatException err) {
			resultArea.setText("Bad input.");
			barcodeID = -1;
		}
	}

	class PalletNumberSearchHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			readInput();
			String result;
			if (barcodeID == -1) {
				result = "Bad input.";
			} else {
				result = db.getPalletInfo(barcodeID);
			}
			resultArea.setText(result);
		}

	}

	class PalletNumberBlockHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			readInput();
			String result = db.blockPallet(barcodeID);
			resultArea.setText(result);
		}
	}
}
