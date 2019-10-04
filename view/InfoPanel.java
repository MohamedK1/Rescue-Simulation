package view;

import java.awt.TextArea;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class InfoPanel extends JPanel {
	 private TextArea textArea;

	public TextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(TextArea textArea) {
		this.textArea = textArea;
	}

}
