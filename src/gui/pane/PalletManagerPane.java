package gui.pane;

import gui.tab.BasicTab;
import gui.tab.SearchByCustomerTab;
import gui.tab.SearchByPalletNumberTab;
import gui.tab.SearchByProductNameTab;
import gui.tab.ShowBlockedProductsTab;
import database.Database;

public class PalletManagerPane extends BasicPane {
	private static final long serialVersionUID = 1;

	public PalletManagerPane(Database db) {
		super(db);
	}

	@Override
	protected void addTabs(Database db) {
		tabbedPane.add(new SearchByProductNameTab(db, resultArea),
				"Search by product name");
		tabbedPane.add(new SearchByPalletNumberTab(db, resultArea),
				"Search by pallet number");
		tabbedPane.add(new SearchByCustomerTab(db, resultArea),
				"Search by customer");
		tabbedPane.add(new ShowBlockedProductsTab(db, resultArea),
				"Show blocked products and pallets");
		
	}
}
