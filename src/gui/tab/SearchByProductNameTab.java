package gui.tab;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import se.datadosen.component.RiverLayout;
import util.Util;
import database.Database;

@SuppressWarnings({ "synthetic-access", "serial" })
public class SearchByProductNameTab extends TabWithProductList {
	private static final int DATE_INTERVAL_START = 0, DATE_INTERVAL_END = 1;
	private static final int NBR_FIELDS = 2;
	private static final int SEARCH = 0, BLOCK = 1;
	private static int NBR_BUTTONS = 2;

	private String productType, timeIntervalStart, timeIntervalEnd;
	private boolean foundProductNameInput;
	private JCheckBox filterBlocked;
	private boolean shouldFilterBlocked;

	public SearchByProductNameTab(Database db, JTextArea resultArea) {
		super(db, resultArea);
		add("", createProductNameSearchPanel());
		add("left br", createButtonPanel());
	}

	private JPanel createProductNameSearchPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new RiverLayout());

		panel.add("left", createProductList());
		panel.add("", createInputFieldPanel());

		return panel;
	}

	public JPanel createInputFieldPanel() {
		String[] texts = new String[NBR_FIELDS];
		fields = new JTextField[NBR_FIELDS];
		JPanel panel = new JPanel();
		panel.setLayout(new RiverLayout());

		texts[DATE_INTERVAL_START] = "Earliest production date: ";
		fields[DATE_INTERVAL_START] = new JTextField(FIELD_LENGTH);

		texts[DATE_INTERVAL_END] = "Latest production date: ";
		fields[DATE_INTERVAL_END] = new JTextField(FIELD_LENGTH);

		panel.add("br", new JLabel(texts[DATE_INTERVAL_START]));
		panel.add("tab", fields[DATE_INTERVAL_START]);

		panel.add("br", new JLabel(texts[DATE_INTERVAL_END]));
		panel.add("tab", fields[DATE_INTERVAL_END]);

		return panel;
	}

	public JComponent createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		JButton[] buttons = new JButton[NBR_BUTTONS];

		buttons[SEARCH] = new JButton("Search for pallets");
		buttons[BLOCK] = new JButton("Block pallets");

		buttons[SEARCH].addActionListener(new PalletSearchHandler());
		buttons[BLOCK].addActionListener(new PalletBlockHandler());

		filterBlocked = new JCheckBox("Show blocked pallets only");

		for (JButton button : buttons) {
			buttonPanel.add(button);
		}
		buttonPanel.add(filterBlocked);

		return buttonPanel;
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

		shouldFilterBlocked = filterBlocked.isSelected();

		foundProductNameInput = productType != null;

	}

	public void showDateTimeFormat() {
		fields[DATE_INTERVAL_START].setText(DATE_FORMAT);
		fields[DATE_INTERVAL_END].setText(DATE_FORMAT);
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
								timeIntervalStart, timeIntervalEnd,
								shouldFilterBlocked);
					} else {
						result = db.getPalletInfo(timeIntervalStart,
								timeIntervalEnd, shouldFilterBlocked);
					}
				} else {

					result = "bad input!";
				}
			} else if (foundProductNameInput) {
				result = db.getPalletInfo(productType, shouldFilterBlocked);
			} else {
				result = "bad input!";
			}

			resultArea.setText(result);
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

			resultArea.setText(result);
		}
	}

	@Override
	public void entryActions() {
		showDateTimeFormat();
		showProductNames();
	}
}
