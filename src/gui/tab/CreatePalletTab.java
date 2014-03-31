package gui.tab;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import se.datadosen.component.RiverLayout;
import util.Util;
import database.Database;

public class CreatePalletTab extends TabWithProductList {
	private static final long serialVersionUID = 1L;
	protected static final int NBR_FIELDS = 2;
	protected static final int PROD_DATE = 0, PALLET_AMOUNT = 1;

	private String productType, prodDate, nbrPallets;
	private boolean foundProductNameInput;

	public CreatePalletTab(Database db, JTextArea resultArea) {
		super(db, resultArea);
		add("", createPalletNumberSearchPanel());
		add("left br", createButtonPanel());

	}

	private JPanel createPalletNumberSearchPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new RiverLayout());

		panel.add("left", createProductList());
		panel.add(createInputFieldPanel());
		return panel;
	}

	public JPanel createInputFieldPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new RiverLayout());
		String[] texts = new String[NBR_FIELDS];
		fields = new JTextField[NBR_FIELDS];

		texts[PROD_DATE] = "Production date: ";
		fields[PROD_DATE] = new JTextField(FIELD_LENGTH);

		texts[PALLET_AMOUNT] = "Number of pallets: ";
		fields[PALLET_AMOUNT] = new JTextField(FIELD_LENGTH);

		panel.add("br", new JLabel(texts[PROD_DATE]));
		panel.add("tab", fields[PROD_DATE]);

		panel.add("br", new JLabel(texts[PALLET_AMOUNT]));
		panel.add("tab", fields[PALLET_AMOUNT]);

		return panel;
	}

	public JComponent createButtonPanel() {
		JPanel buttonPanel = new JPanel();

		JButton button = new JButton("Create pallets");
		button.addActionListener(new PalletCreationHandler());

		buttonPanel.add(button);
		return buttonPanel;
	}

	public void entryActions() {
		showDateTimeFormat();
		showProductNames();
	}

	public void readInput() {
		productType = list.getSelectedValue();
		prodDate = fields[PROD_DATE].getText();
		if (prodDate.equals(DATE_FORMAT)) {
			prodDate = "";
		}
		nbrPallets = fields[PALLET_AMOUNT].getText();

		foundProductNameInput = productType != null;
	}

	class PalletCreationHandler implements ActionListener {
		@SuppressWarnings("synthetic-access")
		public void actionPerformed(ActionEvent e) {
			readInput();
			checkTimeInput();

			if (foundProductNameInput && nbrPallets.length() > 0
					&& productType.length() > 0) {
				String result;
				try {
					int nbrPalletsInt = Integer.parseInt(nbrPallets);
					result = db.registerProducedPallets(productType, prodDate,
							nbrPalletsInt);
				} catch (NumberFormatException err) {
					err.printStackTrace();
					result = "bad input!";
				}
				resultArea.setText(result);
			}
		}

		private void checkTimeInput() {
			prodDate = Util.checkDate(prodDate);
		}
	}

	private void showDateTimeFormat() {
		fields[PROD_DATE].setText("yyyy-mm-dd");
	}
}
