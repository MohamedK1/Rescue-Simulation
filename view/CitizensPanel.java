package view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.CommandCenter;
import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.people.Citizen;
import model.units.Unit;

public class CitizensPanel extends JPanel {
 private InfoPanel infoPanel;
 public InfoPanel getInfoPanel() {
	return infoPanel;
}


 public CitizensPanel(InfoPanel infoPanel) {
	this.infoPanel= infoPanel;
	 
	 this.setLayout(new GridLayout(0,1));
	 
 }
 public void addCitizens(ArrayList<Citizen> citizensList) {
	 this.removeAll();
	 for(Citizen c : citizensList) {
     CitizenButton oneCitizen = new CitizenButton(c);
     oneCitizen.setPreferredSize(new Dimension(100,50));
     oneCitizen.setContentAreaFilled(false);
     oneCitizen.addActionListener(new InfoListener());
 	

	
     Image img = c.getImg();
		ImageIcon imgIcon = new ImageIcon(img.getScaledInstance(100,40,java.awt.Image.SCALE_SMOOTH));
	
		oneCitizen.setIcon(imgIcon);
     
     this.add(oneCitizen);
     
	 }
	 revalidate();
	 repaint();
 }



public void addUnits(ArrayList<Unit> unitsList) {
	 this.removeAll();
	 for(Unit u : unitsList) {
		 UnitButton oneUnit = new UnitButton(u);
		 oneUnit.setPreferredSize(new Dimension(100,50));
		 oneUnit.setContentAreaFilled(false);
		 oneUnit.addActionListener(new InfoListener());
		 this.add(oneUnit);
	 }
	 revalidate();
 }

private class InfoListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
	 if (arg0.getSource() instanceof CitizenButton) {
		 CitizenButton citizenButton = (CitizenButton)arg0.getSource();
			TextArea textArea = infoPanel.getTextArea();
		
			
	if(Grid.isGridClicked()&&DownPanel.isRespondClicked()&&UnitPanel.isUnitPanelClicked()) {
		try {
			UnitPanel.getUnitSelected().respond(citizenButton.getCitizen());
			CommandCenter.getView().getUnitPanel().transferToResponding(UnitPanel.getUnitSelected());
		} catch (IncompatibleTargetException | CannotTreatException e) {
			JOptionPane.showMessageDialog(null,e.getMessage(), "Error",JOptionPane.ERROR_MESSAGE);

		}
		CitizensPanel p = CommandCenter.getView().getLeftPanel().getCitizensPanel();
		DownPanel.getRespondButton().setBackground(null);
		DownPanel.setRespondClicked(false);
		UnitPanel.setUnitPanelClicked(false,null);
		Grid.setGridClicked(false);
		UnitPanel.makeUnitPanelClickable();
		Grid.makeGridClickable();
		textArea.setText("");
		p.removeAll();
		p.revalidate();
		p.repaint();
	
		
	} else {
			textArea.setText(citizenButton.getCitizen().toString());
			infoPanel.setTextArea(textArea);
	}
	}else if(arg0.getSource() instanceof UnitButton) {
			UnitButton unitButton = (UnitButton) arg0.getSource();
			TextArea textArea = infoPanel.getTextArea();
			
			textArea.setText(unitButton.getUnit().toString());
		
		}
	 	
	}

}

	
}
