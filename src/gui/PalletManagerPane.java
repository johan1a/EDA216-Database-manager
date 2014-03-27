package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import util.Util;
import database.Database;

@SuppressWarnings("synthetic-access")
public class PalletManagerPane extends BasicPane {
	private static final long serialVersionUID = 1;
	private static final int DATE_INTERVAL_START = 0, DATE_INTERVAL_END = 1;
	private static final int NBR_FIELDS = 2;

	private static final int SEARCH = 0, BLOCK = 1;
	private static final int NBR_BUTTONS = 2;

	private JTextField[] fields;
	private String productType;
	private String timeIntervalStart;
	private String timeIntervalEnd;
	private boolean foundProductNameInput;

	public PalletManagerPane(Database db) {
		super(db);
	}

	@Override
	public InputPanels createInputPanel() {
		String[] texts = new String[NBR_FIELDS];
		fields = new JTextField[NBR_FIELDS];

		texts[DATE_INTERVAL_START] = "Earliest production date: ";
		fields[DATE_INTERVAL_START] = new JTextField(FIELD_LENGTH);

		texts[DATE_INTERVAL_END] = "Latest production date: ";
		fields[DATE_INTERVAL_END] = new JTextField(FIELD_LENGTH);

		return new InputPanels(texts, fields);
	}

	public void showDateTimeFormat() {
		fields[DATE_INTERVAL_START].setText(DATE_FORMAT);
		fields[DATE_INTERVAL_END].setText(DATE_FORMAT);
	}

	@Override
	public JComponent createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		JButton[] buttons = new JButton[NBR_BUTTONS];

		buttons[SEARCH] = new JButton("Search for pallets");
		buttons[BLOCK] = new JButton("Block pallets");

		buttons[SEARCH].addActionListener(new PalletSearchHandler());
		buttons[BLOCK].addActionListener(new PalletBlockHandler());

		for (JButton button : buttons) {
			buttonPanel.add(button);
		}
		return buttonPanel;
	}

	public void entryActions() {
		clearMessage();
		showDateTimeFormat();
		showProductNames();
	}

	public void readInput() {
		productType = list.getSelectedValue();
		timeIntervalStart = fields[DATE_INTERVAL_START].getText();
		timeIntervalEnd = fields[DATE_INTERVAL_END].getText();

		if (timeIntervalStart.equals(DATE_FORMAT)) {
			timeIntervalStart = "";
		}
		if (timeIntervalEnd.equals(DATE_FORMAT)) {
			timeIntervalEnd = "";
		}

		foundProductNameInput = !productType.equals("");

	}

	class PalletSearchHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			readInput();
			String result;

			if (timeIntervalStart.length() > 0 || timeIntervalEnd.length() > 0) {
				if (Util.isDate(timeIntervalStart)
						&& Util.isDate(timeIntervalEnd)) {
					if (foundProductNameInput) {
						result = db.getPalletInfo(productType,
								timeIntervalStart, timeIntervalEnd);
					} else {
						result = db.getPalletInfo(timeIntervalStart,
								timeIntervalEnd);
					}
				} else {

					result = "bad input!";
				}
			} else if (foundProductNameInput) {
				result = db.getPalletInfo(productType);
			} else {
				result = "bad input!";
			}

			displayMessage(result);
		}
	}

	class PalletBlockHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			readInput();
			String result;

			if (foundProductNameInput && Util.isDate(timeIntervalStart)
					&& Util.isDate(timeIntervalEnd)) {
				result = db.blockPallets(productType, timeIntervalStart,
						timeIntervalEnd);
			} else {
				result = "bad input!";
			}

			displayMessage(result);
		}
	}
}
