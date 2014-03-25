package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import util.Util;
import database.Database;

@SuppressWarnings("synthetic-access")
public class PalletCreatorPane extends BasicPane {
	private static final long serialVersionUID = 1;
	protected static final int NBR_FIELDS = 5;
	protected static final int PRODUCT_TYPE = 0, ORDER_ID = 1, PROD_DATE = 2,
			PROD_TIME = 3, PALLET_AMOUNT = 4;
	private String productType;
	private String orderID;
	private String prodDate;
	private String prodTime;
	private String result;

	private int nbrPallets;
	private JTextArea resultArea;

	public PalletCreatorPane(Database db) {
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

		texts[PROD_DATE] = "Production date: ";
		fields[PROD_DATE] = new JTextField(FIELD_LENGTH);

		texts[PROD_TIME] = "Production time: ";
		fields[PROD_TIME] = new JTextField(FIELD_LENGTH / 2);

		texts[PALLET_AMOUNT] = "Number of pallets: ";
		fields[PALLET_AMOUNT] = new JTextField(FIELD_LENGTH);
		
		return new InputPanels(texts, fields);
	}

	@Override
	public JComponent createButtonPanel() {
		JPanel buttonPanel = new JPanel();

		JButton button = new JButton("Create pallets");
		button.addActionListener(new PalletCreationHandler());

		buttonPanel.add(button);
		return buttonPanel;
	}

	public void entryActions() {
		clearMessage();
		showDateTimeFormat();
	}

	public void showDateTimeFormat() {
		fields[PROD_DATE].setText("yyyy-mm-dd");
		fields[PROD_TIME].setText("hh:mm");
	}

	public void readInput() {
		productType = fields[PRODUCT_TYPE].getText();
		orderID = fields[ORDER_ID].getText();
		prodDate = fields[PROD_DATE].getText();
		prodTime = fields[PROD_TIME].getText();
		nbrPallets = Integer.parseInt(fields[PALLET_AMOUNT].getText());
	}

	class PalletCreationHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			readInput();
			if (Util.isDate(prodDate)) {
				result = db.registerProducedPallets(productType, orderID,
						prodDate, prodTime, nbrPallets);
				resultArea.setText(result);
			}
		}
	}
}
