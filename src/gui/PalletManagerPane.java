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
	private static final int PRODUCT_TYPE = 0, ORDER_ID = 1,
			DATE_INTERVAL_START = 2, DATE_INTERVAL_END = 3, PROD_TIME = 4;

	private static final int SEARCH = 0, BLOCK = 1, UNBLOCK = 2;
	private static final int NBR_FIELDS = 5, NBR_BUTTONS = 3;

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
	public InputPanels createInputPanel() {
		String[] texts = new String[NBR_FIELDS];
		fields = new JTextField[NBR_FIELDS];

		texts[PRODUCT_TYPE] = "Product type: ";
		fields[PRODUCT_TYPE] = new JTextField(FIELD_LENGTH);

		texts[ORDER_ID] = "Order ID: ";
		fields[ORDER_ID] = new JTextField(FIELD_LENGTH);

		texts[DATE_INTERVAL_START] = "Earliest production date: ";
		fields[DATE_INTERVAL_START] = new JTextField(FIELD_LENGTH);

		texts[DATE_INTERVAL_END] = "Latest production date: ";
		fields[DATE_INTERVAL_END] = new JTextField(FIELD_LENGTH);

		texts[PROD_TIME] = "Production time: ";
		fields[PROD_TIME] = new JTextField(FIELD_LENGTH / 2);

		return new InputPanels(texts, fields);
	}

	public void showDateTimeFormat() {
		fields[DATE_INTERVAL_START].setText("yyyy-mm-dd");
		fields[DATE_INTERVAL_END].setText("yyyy-mm-dd");
		fields[PROD_TIME].setText("hh:mm");
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
		showDateTimeFormat();
	}

	public void readInput() {
		productType = fields[PRODUCT_TYPE].getText();
		timeIntervalStart = fields[DATE_INTERVAL_START].getText();
		timeIntervalEnd = fields[DATE_INTERVAL_END].getText();

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
