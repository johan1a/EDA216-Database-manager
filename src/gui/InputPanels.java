package gui;

import javax.swing.*;

import java.awt.*;

/**
 * A GUI panel which contains a number of text fields on top of each other. Each
 * text field has a label.
 */
public class InputPanels extends JPanel {
	private static final long serialVersionUID = 1;

	/**
	 * Create the panel with the specified fields and labels.
	 * 
	 * @param texts
	 *            The labels on the fields.
	 * @param fields
	 *            The text fields.
	 */
	public InputPanels(String[] texts, JTextField[] fields) {
		setLayout(new GridLayout(texts.length, 2));
		JPanel temp = new JPanel();
		temp.setLayout(new GridLayout(1, texts.length));
		for (int i = 0; i < texts.length; i++) {

			System.out.println(i);
			JLabel label = new JLabel(texts[i]);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			temp.add(label);
			temp.add(fields[i]);
			add(temp);
		}
	}
}
