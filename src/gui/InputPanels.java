package gui;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import se.datadosen.component.RiverLayout;

public class InputPanels extends Container {
	private static final long serialVersionUID = 1;

	public InputPanels(String[] texts, JTextField[] fields) {

		setLayout(new RiverLayout());
		for (int i = 0; i < texts.length; i++) {
			JLabel label = new JLabel(texts[i]);
			add("br",label);
			add("tab",fields[i]);
			
		}
	}
}
