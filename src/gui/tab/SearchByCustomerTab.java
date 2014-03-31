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
public class SearchByCustomerTab extends BasicTab {
	private static final int SEARCH = 0;
	private static int NBR_BUTTONS = 1;

	private JCheckBox filterDelivered;
	private JTextField field;
	private String customerName;
	private JButton button;

	public SearchByCustomerTab(Database db, JTextArea resultArea) {
		super(db, resultArea);
		add("", createProductNameSearchPanel());
		add("left br", createButtonPanel());
	}

	private JPanel createProductNameSearchPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new RiverLayout());

		panel.add("", createInputFieldPanel());

		return panel;
	}

	public JPanel createInputFieldPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new RiverLayout());

		field = new JTextField(FIELD_LENGTH);

		panel.add("br", new JLabel("Customer name: "));
		panel.add("tab", field);

		return panel;
	}

	public JComponent createButtonPanel() {
		JPanel buttonPanel = new JPanel();

		button = new JButton("Search for pallets");
		button.addActionListener(new PalletSearchHandler());

		buttonPanel.add(button);

		return buttonPanel;
	}

	public void readInput() {
		customerName = field.getText();
	}

	class PalletSearchHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			readInput();
			String result = db.getPalletInfoByCustomer(customerName);
			resultArea.setText(result);
		}
	}

	@Override
	public void entryActions() {
		resultArea.setText("");
	}
}
