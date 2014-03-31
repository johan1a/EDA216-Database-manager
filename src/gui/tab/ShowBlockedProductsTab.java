package gui.tab;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import database.Database;

public class ShowBlockedProductsTab extends BasicTab {
	private static final long serialVersionUID = 1L;
	private static final int NBR_BUTTONS = 2;
	private static final int LIST_BLOCKED_PRODUCTS = 0,
			LIST_BLOCKED_PALLETS = 1;

	public ShowBlockedProductsTab(Database db, JTextArea resultArea) {
		super(db, resultArea);
		add("", createButtonPanel());
	}

	public JComponent createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		JButton[] buttons = new JButton[NBR_BUTTONS];

		buttons[LIST_BLOCKED_PRODUCTS] = new JButton("List blocked products");
		buttons[LIST_BLOCKED_PALLETS] = new JButton("List blocked pallets");

		buttons[LIST_BLOCKED_PRODUCTS]
				.addActionListener(new ListBlockedProductsHandler());
		buttons[LIST_BLOCKED_PALLETS]
				.addActionListener(new ListBlockedPalletsHandler());

		for (JButton button : buttons) {
			buttonPanel.add(button);
		}

		return buttonPanel;
	}

	@Override
	public void entryActions() {
		resultArea.setText("");
	}

	class ListBlockedProductsHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			resultArea.setText(db.getBlockedProducts());
		}
	}

	class ListBlockedPalletsHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			resultArea.setText(db.getBlockedPallets());
		}
	}
}
