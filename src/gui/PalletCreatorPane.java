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
	private JTextField[] fields;
	private static final int PRODUCT_TYPE = 0, PROD_DATE = 1,
			PALLET_AMOUNT = 2, LOCATION = 3;
	private static final int NBR_FIELDS = 4;
	private String productType;
	private String prodDate;
	private String result;
	private int nbrPallets;
	private JTextArea resultArea;
	private String palletLocation;

	public PalletCreatorPane(Database db) {
		super(db);
	}

	@Override
	public JTextArea createResultPanel() {
		resultArea = new JTextArea();
		return resultArea;
	}

	@Override
	public JComponent createInputPanel() {
		String[] texts = new String[NBR_FIELDS];
		fields = new JTextField[NBR_FIELDS];
		
		texts[PRODUCT_TYPE] = "Product type: ";
		fields[PRODUCT_TYPE] = new JTextField(20);

		texts[PROD_DATE] = "Production date: ";
		fields[PROD_DATE] = new JTextField(10);
		fields[PROD_DATE].setText("yyyy-mm-dd");

		texts[PALLET_AMOUNT] = "Amount: ";
		fields[PALLET_AMOUNT] = new JTextField(5);

		texts[LOCATION] = "Pallet location: ";
		fields[LOCATION] = new JTextField(5);
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
		fields[PROD_DATE].setText("yyyy-mm-dd");
	}

	public void readInput() {
		productType = fields[PRODUCT_TYPE].getText();
		prodDate = fields[PROD_DATE].getText();
		nbrPallets = Integer.parseInt(fields[PALLET_AMOUNT].getText());
		palletLocation = fields[LOCATION].getText(); //TODO behövs denna?
	}

	class PalletCreationHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			readInput();
			if (Util.isDate(prodDate)) {
				result = db.registerProducedPallets(productType, prodDate,
						nbrPallets);
				resultArea.setText(result);
			}
		}
	}
}
