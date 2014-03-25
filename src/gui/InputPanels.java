package gui;

import java.awt.Container;

import javax.swing.JLabel;
import javax.swing.JTextField;

import se.datadosen.component.RiverLayout;

public class InputPanels extends Container {
	private static final long serialVersionUID = 1;

	public InputPanels(String[] texts, JTextField[] fields) {
		setLayout(new RiverLayout());
		for (int i = 0; i < texts.length; i++) {
			add("br", new JLabel(texts[i]));
			add("tab", fields[i]);
		}
	}
}
