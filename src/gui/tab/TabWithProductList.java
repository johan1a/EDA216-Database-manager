package gui.tab;

import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import database.Database;

public abstract class TabWithProductList extends BasicTab {
	protected JTextField[] fields;
	protected JScrollPane listScroller;
	protected String[] productNames;
	protected JList<String> list;

	public TabWithProductList(Database db, JTextArea resultArea) {
		super(db, resultArea);

	}

	@Override
	public void entryActions() {
		// TODO Auto-generated method stub

	}

	protected void showProductNames() {
		productNames = db.getProductNames();
		list.setListData(productNames);
		listScroller.revalidate();
		listScroller.repaint();
	}

	public JScrollPane createProductList() {
		list = new JList<String>();
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);

		listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(250, 80));
		
		
		return listScroller;
	}
}
