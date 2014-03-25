package gui;

import javax.swing.*;

import java.awt.*;

public class InputPanels extends JPanel {
	private static final long serialVersionUID = 1;

	public InputPanels(String[] texts, JTextField[] fields) {
		setLayout(new GridLayout(texts.length, 1));
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, texts.length));
		for (int i = 0; i < texts.length; i++) {
			JLabel label = new JLabel(texts[i]);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			panel.add(label);
			panel.add(fields[i]);
			add(panel);
		}
	}
}
