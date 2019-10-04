package controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import model.events.SOSListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import model.units.Unit;
import recommendation.recommend;
import simulation.Rescuable;
import simulation.Simulator;
import view.DownPanel;
import view.GameOver;
import view.Grid;
import view.UnitPanel;
import view.View;

public class CommandCenter implements SOSListener {

	private	Simulator engine;
	private ArrayList<ResidentialBuilding> visibleBuildings;
	private ArrayList<ResidentialBuilding> addedBuildings;
	private ArrayList<Citizen> visibleCitizens;
	private ArrayList<Unit> emergencyUnits;
	private static View view;
	private static CommandCenter commandCenter;
	private static recommend recommendation;
	//------------ networking---------
		private static Server server ;
		private  static Client client;	
		//------------end networking---------
		
	public CommandCenter() throws IOException {
		commandCenter=this;



		engine = new Simulator(this);
		visibleBuildings = new ArrayList<ResidentialBuilding>();
		visibleCitizens = new ArrayList<Citizen>();
		emergencyUnits = engine.getEmergencyUnits();
		addedBuildings = new ArrayList<>();

	}



	@Override

	public void receiveSOSCall(Rescuable r) {
		if(r instanceof Citizen) {
			Citizen citizen = (Citizen)r;
			if(!visibleCitizens.contains(citizen))
				visibleCitizens.add(citizen);
		}else if (r instanceof ResidentialBuilding) {
			ResidentialBuilding building = (ResidentialBuilding)r;
			if(!visibleBuildings.contains(building))
				visibleBuildings.add(building);
		}



	}



	
public static void main(String[] args) throws IOException {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
	
		}

		
		CommandCenter command = new CommandCenter();
		view = new View(commandCenter);
		view.getUnitPanel().addUnits(commandCenter.getEmergencyUnits());
		
		recommendation =new recommend(commandCenter);
		
		for(Unit u : commandCenter.getEmergencyUnits()) {
			view.getGrid().addUnit(u,u.getLocation().getX(),u.getLocation().getY());
		}
		for(Citizen c : commandCenter.getVisibleCitizens()) {
			view.getGrid().addCitizen(c,c.getLocation().getX(),c.getLocation().getY());
		}

		view.getGrid().getCells()[0][0].setIcon(new ImageIcon(view.getGrid().getCommandCentreUnitImg().getScaledInstance(70, 40, Image.SCALE_SMOOTH)));
		view.getGrid().revalidate();
		view.getGrid().repaint();


		NextCycleListener nextCycleListener = new NextCycleListener();
		RespondListener respondListener = new RespondListener();
		view.getDownPanel().getNextCyleButton().addActionListener(nextCycleListener);
		DownPanel.getRespondButton().addActionListener(respondListener);
		
		//------------ networking---------

		CommandCenter.server= new Server();

	//	server.getFrame().setVisible(true);
		Thread serverThread = new Thread(server);
		serverThread.start();

		//------------end networking---------


	}

//------------ networking---------

public String getUnitInfo() {
	String info="";
	for(Unit u : emergencyUnits) {
		if(u.getTarget()!=null) {
		info+="-"+u.getUnitType()+" "+u.getUnitID()+" at location "+u.getLocation()+" with state "+u.getState()+" "+" has a target At location "
				+u.getTarget().getLocation().toString()+"\n";
		}else {
			info+="-"+u.getUnitType()+" "+u.getUnitID()+" at location "+u.getLocation()+" with state "+u.getState()+"\n";
				
		}
	}
return info;
}

//------------end networking---------

	public Simulator getEngine() {
		return engine;
	}



	public void setEngine(Simulator engine) {
		this.engine = engine;
	}



	public ArrayList<ResidentialBuilding> getVisibleBuildings() {
		return visibleBuildings;
	}



	public void setVisibleBuildings(ArrayList<ResidentialBuilding> visibleBuildings) {
		this.visibleBuildings = visibleBuildings;
	}



	public ArrayList<Citizen> getVisibleCitizens() {
		return visibleCitizens;
	}

	public void setVisibleCitizens(ArrayList<Citizen> visibleCitizens) {
		this.visibleCitizens = visibleCitizens;
	}
	public ArrayList<Unit> getEmergencyUnits() {
		return emergencyUnits;
	}
	public void setEmergencyUnits(ArrayList<Unit> emergencyUnits) {
		this.emergencyUnits = emergencyUnits;
	}
	public static View getView() {
		return view;
	}
	public static void setView(View view) {
		CommandCenter.view = view;
	}


	
private  static class NextCycleListener implements ActionListener{

		@Override

		public void actionPerformed(ActionEvent e) {  // added
//			view.getGrid().getCells()[0][0].setIcon(new ImageIcon(view.getGrid().getCommandCentreUnitImg().getScaledInstance(70, 40, Image.SCALE_SMOOTH)));
//			view.getGrid().revalidate();
//			view.getGrid().repaint();
			DownPanel.setRespondClicked(false);
			UnitPanel.setUnitPanelClicked(false,null);
			Grid.setGridClicked(false);
			UnitPanel.makeUnitPanelClickable();
			Grid.makeGridClickable();
			DownPanel.getRespondButton().setBackground(null);
			

			
			CommandCenter.commandCenter.getEngine().nextCycle();
			//testing recommendation
//			recommendation.setEvacuator();
//			recommendation.setCollapseBuildings();
//			recommendation.setFireBuildings();
//			recommendation.setFireTruck();
//			recommendation.setGasControl();
//			recommendation.setGasleakBuildings();
//			recommendation.setAmbulance();
//			recommendation.setinjuriedCitizen();
//			recommendation.setDiseaseControl();
//			recommendation.setinfectedCitizen();
//			//
//			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//			System.out.println("Evacuators-----------");
//			System.out.print(recommend.recommendEvacuator());
//			System.out.println("FireTruck------------");
//			System.out.print(recommend.recommendFireTruck());
//			System.out.println("GAsControlUnit-------");
//			System.out.print(recommend.recommendGasControl());
//			System.out.println("Ambulance------------");
//			System.out.print(recommend.recommendAmbulance());
//			System.out.println("DiseaseControlUnit---");
//			System.out.print(recommend.recommendDiseaseControl());
//			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//
//			//
			DownPanel.getCurrCycle().setText("  Current cycle: "+commandCenter.getEngine().getCurrentCycle());
			DownPanel.getCasualities().setText("   Casualities :"+commandCenter.getEngine().calculateCasualties());
			view.getDownPanel().getTextArea().setText(commandCenter.getEngine().getLogString());
			try {
				CommandCenter.commandCenter.citizensHandle();
				CommandCenter.commandCenter.BuildingsHandle();

				// TODO make unitsHandle
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "image files not found", "error" , JOptionPane.ERROR_MESSAGE);

			}



			CommandCenter.view.getLeftPanel().getCitizensPanel().removeAll();
			CommandCenter.view.getLeftPanel().getInfoPanel().getTextArea().setText("");
			CommandCenter.view.getLeftPanel().getCitizensPanel().revalidate();
			CommandCenter.view.getLeftPanel().getCitizensPanel().repaint();

			if(commandCenter.getEngine().checkGameOver()) {
			JFrame frame=	CommandCenter.view.getFrame();
				try {
				frame.getContentPane().removeAll();
				frame.revalidate();
				frame.repaint();
				GameOver g = new GameOver(frame);
				g.setSize(new Dimension(1500,400));
				frame.setBackground(Color.black);
				
				frame.add(g);
				frame.add(g.getDownPanel(),BorderLayout.SOUTH);
				frame.validate();
					frame.repaint();
					
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null,"Game over image file does not exist ","File Not found",JOptionPane.ERROR_MESSAGE);
				}
				

			}



		}

	}

	public static CommandCenter getCommandCenter() {
	return commandCenter;
}



	
private void citizensHandle() throws IOException {
		for(int i=0;i<commandCenter.getVisibleCitizens().size();i++) {
			Citizen citizen = commandCenter.getVisibleCitizens().get(i);
			if((view.getGrid().getCells()[citizen.getLocation().getX()][citizen.getLocation().getY()].getBuilding().size()==0)) {
				if(citizen.getDisaster().getStartCycle()==commandCenter.getEngine().getCurrentCycle()) {
					CommandCenter.view.getGrid().addCitizen(citizen, citizen.getLocation().getX(), citizen.getLocation().getY());

					//if we have time add something that reveals the type of the disaster
				}
			}
			if(citizen.getState()==CitizenState.DECEASED) {
				CommandCenter.view.getGrid().killCitizen(citizen, citizen.getLocation().getX(), citizen.getLocation().getY());
			}

		}
		
		

	}
	private void BuildingsHandle() throws IOException {

		boolean add=true;


		for(int i=0;i<commandCenter.getVisibleBuildings().size();i++) {
			ResidentialBuilding building = commandCenter.getVisibleBuildings().get(i);
			if(building.getDisaster().getStartCycle()==commandCenter.getEngine().getCurrentCycle()) {

				if(!addedBuildings.contains(building))
					view.getGrid().addBuilding(building, building.getLocation().getX(),building.getLocation().getY());

				addedBuildings.add(building);

			}
			if(building.getStructuralIntegrity()==0||building.getGasLevel()==100) {
				for(Citizen c : building.getOccupants()) {

					view.getGrid().killCitizen(c,c.getLocation().getX(),c.getLocation().getY());
				}
				if(building.getStructuralIntegrity()==0) {
				view.getGrid().destroyBuilding(building, building.getLocation().getX(), building.getLocation().getY());
				}
			}
		}

	}

	private static class RespondListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
//			view.getGrid().getCells()[0][0].setIcon(new ImageIcon(view.getGrid().getCommandCentreUnitImg().getScaledInstance(70, 40, Image.SCALE_SMOOTH)));
//			view.getGrid().revalidate();
//			view.getGrid().repaint(); //added
			CommandCenter.view.getLeftPanel().getCitizensPanel().removeAll();
			CommandCenter.view.getLeftPanel().getInfoPanel().getTextArea().setText("");
			CommandCenter.view.getLeftPanel().getCitizensPanel().revalidate();
			CommandCenter.view.getLeftPanel().getCitizensPanel().repaint();

			JButton respondButton = (JButton) (e.getSource());
			if(!DownPanel.isRespondClicked()) {
			DownPanel.setRespondClicked(true);
			respondButton.setBackground(Color.GREEN);
			Grid.makeGridUnclickable(null);
			
			
		}else {
			respondButton.setBackground(null);
			DownPanel.setRespondClicked(false);
			UnitPanel.setUnitPanelClicked(false,null);
			Grid.setGridClicked(false);
			UnitPanel.makeUnitPanelClickable();
			Grid.makeGridClickable();

		}
			
		
		
		
		
		}
		
	}


}
