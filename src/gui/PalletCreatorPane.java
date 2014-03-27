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
	protected static final int NBR_FIELDS = 2;
	protected static final int PROD_DATE = 0, PALLET_AMOUNT = 1;
	private String productType;
	private String prodDate;

	private String nbrPallets;

	public PalletCreatorPane(Database db) {
		super(db);
	}

	@Override
	public InputPanels createInputPanel() {
		String[] texts = new String[NBR_FIELDS];
		fields = new JTextField[NBR_FIELDS];

		texts[PROD_DATE] = "Production date: ";
		fields[PROD_DATE] = new JTextField(FIELD_LENGTH);

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
		showProductNames();
	}

	public void showDateTimeFormat() {
		fields[PROD_DATE].setText("yyyy-mm-dd");
	}

	public void readInput() {
		productType = list.getSelectedValue();
		prodDate = fields[PROD_DATE].getText();
		if (prodDate.equals(DATE_FORMAT)) {
			prodDate = "";
		}
		nbrPallets = fields[PALLET_AMOUNT].getText();
	}

	class PalletCreationHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			readInput();
			checkTimeInput();

			if (productType.length() > 0 && nbrPallets.length() > 0) {
				String result;
				try {
					int nbrPalletsInt = Integer.parseInt(nbrPallets);
					result = db.registerProducedPallets(productType, prodDate,
							nbrPalletsInt);
				} catch (NumberFormatException err) {
					err.printStackTrace();
					result = "bad input!";
				}
				displayMessage(result);
			}
		}

		private void checkTimeInput() {
			prodDate = Util.checkDate(prodDate);
		}
	}
}
