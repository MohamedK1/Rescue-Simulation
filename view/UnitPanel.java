package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;

import model.people.Citizen;
import model.units.Unit;
import model.units.UnitState;

public class UnitPanel extends JPanel {
	private InfoPanel infoPanel;
	private LeftPanel leftPanel;
	private static ArrayList<UnitButton> unitButtons;

	// --------- recently added--------
	private JPanel availableUnits;
	private JPanel respondingUnits;
	private JPanel treatingUnits;
	//------------------------------------------
	private static boolean unitPanelClicked;
	private static Unit  unitSelected;
	


	public static Unit getUnitSelected() {
		return unitSelected;
	}
	public static void setUnitSelected(Unit unitSelected) {
		UnitPanel.unitSelected = unitSelected;
	}
	public static boolean isUnitPanelClicked() {
		return unitPanelClicked;
	}
	public static void setUnitPanelClicked(boolean unitPanelClicked,Unit unit) {
		UnitPanel.unitPanelClicked = unitPanelClicked;
		UnitPanel.unitSelected=unit;
	}


	public UnitPanel( InfoPanel infoPanel,LeftPanel leftPanel) {
		super();
		this.leftPanel=leftPanel;
		this.infoPanel=infoPanel;

		this.setLayout(new GridLayout(0,1));
		unitButtons = new ArrayList<>();

		// --------------------recently added -----------------
		availableUnits= new JPanel();
		availableUnits.setLayout(new GridLayout(0,1));
		String available= "availble units";
		Border border = BorderFactory.createTitledBorder(available);
		availableUnits.setBorder(border);
		availableUnits.setSize(new Dimension(200,100));
		JScrollPane avilableScroll = new JScrollPane(availableUnits,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		avilableScroll.setSize(new Dimension(200,100));

		respondingUnits= new JPanel();
		respondingUnits.setLayout(new GridLayout(0,1));
		String responding= "responding units";
		Border borderReesponding = BorderFactory.createTitledBorder(responding);
		respondingUnits.setBorder(borderReesponding);
		respondingUnits.setSize(new Dimension(200,50));
		JScrollPane respondingScroll = new JScrollPane(respondingUnits,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		respondingScroll.setPreferredSize(new Dimension(200,50));


		treatingUnits = new JPanel();
		treatingUnits.setLayout(new GridLayout(0,1));
		String treatingTitle= "treating units";
		Border borderTreating = BorderFactory.createTitledBorder(treatingTitle);
		treatingUnits.setBorder(borderTreating);
		treatingUnits.setSize(new Dimension(200,100));
		JScrollPane treatingScroll = new JScrollPane(treatingUnits,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		treatingScroll.setPreferredSize(new Dimension(200,100));

		this.add(avilableScroll);
		this.add(respondingScroll);
		this.add(treatingScroll);
		//--------------------- end of recently added----------------------

	}


	public JPanel getTreatingUnits() {
		return treatingUnits;
	}


	public void setTreatingUnits(JPanel treatingUnits) {
		this.treatingUnits = treatingUnits;
	}


	public ArrayList<UnitButton> getUnitButtons() {
		return unitButtons;
	}


	public void setUnitButtons(ArrayList<UnitButton> unitButtons) {
		this.unitButtons = unitButtons;
	}


	private class InfoListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getSource() instanceof UnitButton) {
				UnitButton unitButton = (UnitButton)arg0.getSource();
				TextArea textArea = infoPanel.getTextArea();
				leftPanel.getCitizensPanel().removeAll();
				leftPanel.getCitizensPanel().revalidate();
				leftPanel.getCitizensPanel().repaint();

				textArea.setText(unitButton.getUnit().toString());
				infoPanel.setTextArea(textArea);
			
			if(DownPanel.isRespondClicked()) {	
			
				if(!unitButton.isUnitClicked()) {
					UnitPanel.setUnitPanelClicked(true,unitButton.getUnit());
					Grid.makeGridClickable();
					UnitPanel.makeUnitPanelUnclickable(unitButton);
				}else {
					
					UnitPanel.setUnitPanelClicked(false,null);
					Grid.makeGridUnclickable(null);
					UnitPanel.makeUnitPanelClickable();
				}
			
			
			}
			
			}

		}

	}


	
public void addUnits(ArrayList<Unit> UnitList) {

		InfoListener infoListener = new InfoListener();

		for(Unit c : UnitList) {
			UnitButton oneUnit=new UnitButton(c);
			oneUnit.setPreferredSize(new Dimension(150,60));
		//	oneUnit.setContentAreaFilled(false);
			oneUnit.setBackground(Color.WHITE);
			oneUnit.addActionListener(infoListener);
			unitButtons.add(oneUnit);

			// ---------recently added------
			if(c.getState()==UnitState.IDLE) {
				availableUnits.add(oneUnit);
			}else if (c.getState()== UnitState.RESPONDING) {
				respondingUnits.add(oneUnit);
			}else if(c.getState()==UnitState.TREATING) {
				treatingUnits.add(oneUnit);
			}
			availableUnits.revalidate();
			availableUnits.repaint();

			respondingUnits.revalidate();
			respondingUnits.repaint();

			treatingUnits.revalidate();
			treatingUnits.repaint();

			// ----------end of recently added ------------		


			//this.add(oneUnit);

		}

		revalidate();
		repaint();

		

	}
      
	
	
public static void makeUnitPanelClickable() {
	UnitPanel.setUnitPanelClicked(false,null);	
	for(UnitButton b : unitButtons) {
			b.setUnitClicked(false);
			b.setEnabled(true);
			b.setBackground(Color.WHITE);
		}
		
}
	
public static void makeUnitPanelUnclickable(UnitButton unitButton) {
	UnitPanel.setUnitPanelClicked(true,unitButton.getUnit());	
	for(UnitButton b :unitButtons) {
			if(unitButton!=b) {
				b.setUnitClicked(false);
				b.setEnabled(false);
				b.setBackground(Color.RED);
			
			}else {
				b.setUnitClicked(true);
				b.setBackground(Color.GREEN);
				b.setEnabled(true);
				
			}
			b.revalidate();
			b.repaint();
		}
	}

public void transferToResponding(Unit unit) {
UnitButton target=null;
	for(int i=0;i<unitButtons.size();i++) {
		if(unitButtons.get(i).getUnit()==unit) {
			target =unitButtons.get(i);
			break;
		}
	}
	availableUnits.remove(target);
	treatingUnits.remove(target);
	respondingUnits.add(target);
	revalidate();
	repaint();
}
public void transferToTreating(Unit unit) {
UnitButton target=null;
	for(int i=0;i<unitButtons.size();i++) {
		if(unitButtons.get(i).getUnit()==unit) {
			target =unitButtons.get(i);
			break;
		}
	}
	availableUnits.remove(target);
	respondingUnits.remove(target);
	treatingUnits.add(target);
	revalidate();
	repaint();
}
public void transferToAvailable(Unit unit) {
UnitButton target=null;
	for(int i=0;i<unitButtons.size();i++) {
		if(unitButtons.get(i).getUnit()==unit) {
			target =unitButtons.get(i);
			break;
		}
	}
	treatingUnits.remove(target);
	respondingUnits.remove(target);
	availableUnits.add(target);
	revalidate();
	repaint();
}


}
