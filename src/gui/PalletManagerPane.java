package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import util.Util;
import database.Database;

@SuppressWarnings("synthetic-access")
public class PalletManagerPane extends BasicPane {
	private static final long serialVersionUID = 1;
	private JTextField[] fields;
	private static final int PRODUCT_TYPE = 0, TIME_INTERVAL_START = 1,
			TIME_INTERVAL_END = 2;
	private static final int SEARCH = 0, BLOCK = 1, UNBLOCK = 2;
	private static final int NBR_FIELDS = 3, NBR_BUTTONS = 3;

	private String productType;
	private String timeIntervalStart;
	private String timeIntervalEnd;
	private AbstractButton resultArea;
	private boolean productOK;
	private boolean timeStartOK;
	private boolean timeEndOK;

	public PalletManagerPane(Database db) {
		super(db);
	}

	@Override
	public JTextArea createResultPanel() {
		JTextArea textArea = new JTextArea();
		return textArea;
	}

	@Override
	public JComponent createInputPanel() {
		String[] texts = new String[NBR_FIELDS];
		texts[PRODUCT_TYPE] = "Product type: ";
		fields = new JTextField[NBR_FIELDS];
		fields[PRODUCT_TYPE] = new JTextField(20);

		texts[TIME_INTERVAL_START] = "Produced between: ";
		fields[TIME_INTERVAL_START] = new JTextField(10);

		texts[TIME_INTERVAL_END] = "and ";
		fields[TIME_INTERVAL_END] = new JTextField(10);

		fields[TIME_INTERVAL_START].setText("yyyy-mm-dd");
		fields[TIME_INTERVAL_END].setText("yyyy-mm-dd");
		return new InputPanels(texts, fields);
	}

	@Override
	public JComponent createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		JButton[] buttons = new JButton[NBR_BUTTONS];

		buttons[SEARCH] = new JButton("Search for pallets");
		buttons[BLOCK] = new JButton("Block pallets");
		buttons[UNBLOCK] = new JButton("Unblock pallets");

		buttons[SEARCH].addActionListener(new PalletSearchHandler());
		buttons[BLOCK].addActionListener(new PalletBlockHandler());
		buttons[UNBLOCK].addActionListener(new PalletUnBlockHandler());

		for (JButton button : buttons) {
			buttonPanel.add(button);
		}
		return buttonPanel;
	}

	public void entryActions() {
		clearMessage();
		fields[TIME_INTERVAL_START].setText("yyyy-mm-dd");
		fields[TIME_INTERVAL_END].setText("yyyy-mm-dd");
	}

	public void readInput() {
		productType = fields[PRODUCT_TYPE].getText();
		timeIntervalStart = fields[TIME_INTERVAL_START].getText();
		timeIntervalEnd = fields[TIME_INTERVAL_END].getText();

		productOK = !productType.equals("");
		timeStartOK = Util.isDate(timeIntervalStart);
		timeEndOK = Util.isDate(timeIntervalEnd);
	}

	class PalletSearchHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			readInput();
			String result;
			if (productOK && timeStartOK && timeEndOK) {
				result = db.getPalletInfo(productType, timeIntervalStart,
						timeIntervalEnd);
			} else if (productOK) {
				result = db.getPalletInfo(productType);
			} else if (timeStartOK && timeEndOK) {
				result = db.getPalletInfo(timeIntervalStart, timeIntervalEnd);
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
			if (productOK && timeStartOK && timeEndOK) {
				result = db.blockPallets(productType, timeIntervalStart,
						timeIntervalEnd);
			} else if (productOK) {
				result = db.blockPallets(productType);
			} else {
				result = "bad input!";
			}
			resultArea.setText(result);
		}
	}

	class PalletUnBlockHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			readInput();
			String result;
			if (productOK && timeStartOK && timeEndOK) {
				result = db.unblockPallets(productType, timeIntervalStart,
						timeIntervalEnd);
			} else if (productOK) {
				result = db.unblockPallets(productType);
			} else {
				result = "bad input!";
			}
			resultArea.setText(result);
		}
	}

}
