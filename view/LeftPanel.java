package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.TextArea;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;


public class LeftPanel extends JPanel{
 public CitizensPanel getCitizensPanel() {
		return citizensPanel;
	}
private InfoPanel infoPanel;
 private CitizensPanel citizensPanel;
 private ArrayList<Image> citizensImage;
public ArrayList<Image> getCitizensImage() {
	return citizensImage;
}
public LeftPanel() {
	infoPanel = new InfoPanel();
	
	String title= "information panel";
	Border border = BorderFactory.createTitledBorder(title);
	
	infoPanel.setBorder(border);
	infoPanel.setSize(new Dimension(200,100));
	this.setLayout(new GridLayout(0,1));
	TextArea textArea = new TextArea(13,37);
	
	
	textArea.setSize(new Dimension(infoPanel.getWidth(),50));
	textArea.setEditable(false);
	textArea.setBackground(java.awt.Color.WHITE);
	infoPanel.add(textArea,BorderLayout.CENTER);
	this.add(infoPanel,BorderLayout.NORTH);
	infoPanel.setTextArea(textArea);
	
	this.citizensPanel = new CitizensPanel(infoPanel);
	
	Border citizensBorder = BorderFactory.createTitledBorder("Content selection");
	citizensPanel.setBorder(citizensBorder);
	citizensPanel.setSize(new Dimension(200,100));
	
	
	JScrollPane citizensScrollPane = new JScrollPane(citizensPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	this.add(citizensScrollPane);
	
	
	
	
	
}
public InfoPanel getInfoPanel() {
	return infoPanel;
}

}
