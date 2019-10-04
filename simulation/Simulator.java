package simulation;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import controller.CommandCenter;
import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import model.disasters.Collapse;
import model.disasters.Disaster;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.disasters.Infection;
import model.disasters.Injury;
import model.events.SOSListener;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import model.units.Ambulance;
import model.units.DiseaseControlUnit;
import model.units.Evacuator;
import model.units.FireTruck;
import model.units.GasControlUnit;
import model.units.Unit;
import model.units.UnitState;
import view.Grid;


public class Simulator implements WorldListener{
	private int currentCycle;
	private ArrayList<ResidentialBuilding> buildings;
	private ArrayList<Citizen> citizens;
	private ArrayList<Unit> emergencyUnits;
	private ArrayList<Disaster> plannedDisasters;
	private ArrayList<Disaster> executedDisasters;
	private Address[][]world;
	private SOSListener emergencyService;
	private ArrayList<Image> citizensImage;
	private static String logString ;



	public static String getLogString() {
		return logString;
	}



	public static void setLogString(String logString) {
		Simulator.logString = logString;
	}



	public ArrayList<Unit> getEmergencyUnits() {
		return emergencyUnits;
	}



	public void setEmergencyService(SOSListener emergencyService) {
		this.emergencyService = emergencyService;
	}




	public Simulator(SOSListener emergencyService) throws IOException {

		Simulator.logString="";
		setEmergencyService(emergencyService);
		world = new Address[10][10];

		//		cycleMapping = new HashMap<>();
		//		buildingMapping = new HashMap<Address, ResidentialBuilding>();
		//		 citizenMapping = new HashMap<String, Citizen>();


		buildings=new ArrayList<ResidentialBuilding>();
		citizens = new ArrayList<Citizen>();
		emergencyUnits = new ArrayList<Unit>();
		plannedDisasters = new ArrayList<Disaster>();
		executedDisasters = new ArrayList<Disaster>();
		for(int i=0;i<10;i++) {
			for(int j=0;j<10;j++) {
				world[i][j]=new Address(i,j);
			}
		}


		loadBuildings("buildings.csv");
		loadCitizens("citizens.csv");
		loadUnits("units.csv");
		loadDisasters("disasters.csv");

	}



	public static ArrayList<String> readFile(String path) throws IOException{
		String currentLine = "";
		FileReader fileReader= new FileReader(path);
		ArrayList<String> arr = new ArrayList<String>();
		BufferedReader br = new BufferedReader(fileReader);
		while ((currentLine = br.readLine()) != null) {
			arr.add(currentLine);
		}
		return arr;
	}


	public void loadUnits() throws IOException {

		Grid.setEvacuatorImg(ImageIO.read(Grid.class.getResource("evacuator.png")));

		Grid.setGasControlUnitImg( ImageIO.read(Grid.class.getResource("gasControlUnit.png")));
		Grid.setFireTruckImg( ImageIO.read(Grid.class.getResource("fireTruck.png")));
		Grid.setAmbulanceImg( ImageIO.read(Grid.class.getResource("ambulance.png")));
		Grid.setDiseaseControlUnitImg( ImageIO.read(Grid.class.getResource("diseaseControlUnit.png")));


	}



	private void loadUnits(String filePath) throws IOException{

		loadUnits();

		ArrayList<String>unitsArray=readFile(filePath);
		for(String s: unitsArray) {
			String[] input=s.split(",");
			switch(input[0]) {
			case"AMB" :

				Ambulance ambulance = new Ambulance(input[1],world[0][0],Integer.parseInt(input[2]),this);
				ambulance.setUnitImage(Grid.getAmbulanceImg());
				emergencyUnits.add(ambulance);
				break;
			case "DCU":

				DiseaseControlUnit diseaseControlUnit = new DiseaseControlUnit(input[1],world[0][0],Integer.parseInt(input[2]),this);
				diseaseControlUnit.setUnitImage(Grid.getDiseaseControlUnitImg());
				emergencyUnits.add(diseaseControlUnit);
				break;
			case "EVC":

				Evacuator evacuator = new Evacuator(input[1],world[0][0],Integer.parseInt(input[2]),this,Integer.parseInt(input[3]));
				evacuator.setUnitImage(Grid.getEvacuatorImg());
				emergencyUnits.add(evacuator);
				break;
			case "FTK":
				FireTruck fireTruck = new FireTruck(input[1],world[0][0],Integer.parseInt(input[2]),this);
				fireTruck.setUnitImage(Grid.getFireTruckImg());
				emergencyUnits.add(fireTruck);
				break;
			case "GCU":

				GasControlUnit gasControlUnit = new GasControlUnit(input[1],world[0][0],Integer.parseInt(input[2]),this);
				gasControlUnit.setUnitImage(Grid.getGasControlUnitImg());
				emergencyUnits.add(gasControlUnit);
				break;

			}
		}


	}
	public void loadBuildings() throws IOException {
		Grid.setBuildingsImage( new ArrayList<>());
		Grid.setBuildingsUnitsImage(new ArrayList<>());
		for(int i=0;i<8;i++) {
			String path="building"+i+".png";
			Image img = ImageIO.read(Grid.class.getResource(path));
			Grid.getBuildingsImage().add( img);
			String pathUnit="building"+i+"Unit.png";
			Image img2 = ImageIO.read(Grid.class.getResource(pathUnit));
			Grid.getBuildingsUnitsImage().add(img2);


		}
	}



	private void loadBuildings(String filePath) throws IOException {

		loadBuildings();

		ArrayList<String>buildingsArray=readFile(filePath);
		for(int i=0;i<buildingsArray.size();i++) {
			String input[]=buildingsArray.get(i).split(",");
			int x =Integer.parseInt(input[0]);
			int y =Integer.parseInt(input[1]);
			ResidentialBuilding currBuilding =new ResidentialBuilding(world[x][y]);

			//--------------------------------------
			currBuilding.setBuildingImage(Grid.getBuildingsImage().get(0));
			Grid.getBuildingsImage().add(Grid.getBuildingsImage().remove(0));
			currBuilding.setBuildingUnitImage(Grid.getBuildingsUnitsImage().get(0));
			Grid.getBuildingsUnitsImage().add(Grid.getBuildingsUnitsImage().remove(0));
			//--------------------------------------------------------	
		
			
			currBuilding.setEmergencyService(emergencyService);
			buildings.add(currBuilding);

		}

	}


	public void loadCitizens() throws IOException {
		citizensImage = new ArrayList<>();
		for(int i=0;i<36;i++) {
			String path="citizen"+i+".png";
			Image img = ImageIO.read(Grid.class.getResource(path));
			citizensImage.add( img);
		}
	}


	private void loadCitizens(String filePath) throws IOException {



		ArrayList<String>citizensArray=readFile(filePath);
		try {
			loadCitizens();
		}catch(IOException e) {
			JOptionPane.showMessageDialog(null, "image files not found", "error" , JOptionPane.ERROR_MESSAGE);
		}

		for(String s :citizensArray) {
			String[] input = s.split(",");
			int x = Integer.parseInt(input[0]);
			int y = Integer.parseInt(input[1]);
			Address address = world[x][y];
			int age = Integer.parseInt(input[4]);
			String id =input[2];
			String name =input[3];
			Citizen currCitizen =new Citizen(address, id, name, age,this);

			//*********** newly added****************
			currCitizen.setImg(citizensImage.get(0));
			citizensImage.add(citizensImage.remove(0));
			//---------------------------------			

			currCitizen.setEmergencyService(emergencyService);
			citizens.add(currCitizen);

			ResidentialBuilding currBuilding=getBuildingByAddress(address);
			if(currBuilding!=null) {
				currBuilding.addCitizen(currCitizen);
			}
		}



	}





	private void loadDisasters(String filePath) throws IOException {



		ArrayList<String>disastersArray=readFile(filePath);
		for(int i=0;i<disastersArray.size();i++) {
			String s = disastersArray.get(i);
			String[] input = s.split(",");
			int startCycle = Integer.parseInt(input[0]);
			switch(input[1]) {
			case"INJ":
			{
				String idInj = input[2];

				Citizen currCitizen= getCitizenByID(idInj);

				if(currCitizen!=null) {
					Injury currInjury = new Injury(startCycle, currCitizen);
					plannedDisasters.add(currInjury);

				}
				break;

			}
			case"INF":
			{
				String idInf = input[2];
				Citizen currCitizen= getCitizenByID(idInf);

				if(currCitizen!=null) {
					Infection currInfection = new Infection(startCycle, currCitizen);
					plannedDisasters.add(currInfection);
				}
				break;	
			}
			case "FIR":

			{
				int xFire = Integer.parseInt(input[2]);
				int yFire = Integer.parseInt(input[3]);
				Address currAddress = world[xFire][yFire];
				ResidentialBuilding currBuilding  = getBuildingByAddress(currAddress);

				if(currBuilding!=null)
				{
					Fire currFire= new Fire(startCycle,currBuilding);
					plannedDisasters.add(currFire);
				}

				break;
			}
			case "GLK":
			{
				int xGas = Integer.parseInt(input[2]);
				int yGas = Integer.parseInt(input[3]);
				Address currAddressGas = world[xGas][yGas];
				ResidentialBuilding currBuilding  = getBuildingByAddress(currAddressGas);

				if(currBuilding!=null)
				{
					GasLeak currGasLeak= new GasLeak(startCycle,currBuilding);
					plannedDisasters.add(currGasLeak);
				}
				break;
			}
			}
		}



	}


	private Citizen getCitizenByID(String id ) {
		for(int i=0;i<citizens.size();i++) {
			Citizen tmp =citizens.get(i);
			if(tmp.getNationalID().equals(id)) {
				return tmp;
			}
		}
		return null;
	}


	private ResidentialBuilding getBuildingByAddress(Address address) {
		for(int i=0;i<buildings.size();i++) {
			ResidentialBuilding tmp = buildings.get(i);
			if(tmp.getLocation()==address) {
				return tmp;
			}
		}
		return null;
	}

	public void assignAddress(Simulatable sim, int x , int y) 
	{
		if(sim instanceof Citizen) {
			Citizen citizen = (Citizen) sim;
			citizen.setLocation(world[x][y]);
			
		}else if (sim instanceof Unit) {
			Unit unit =(Unit) sim;
			if( unit.getLocation().getX()!=x&&unit.getLocation().getY()!=y) {
				
			CommandCenter.getView().getGrid().removeUnit(unit, unit.getLocation().getX(),unit.getLocation().getY());
			
			CommandCenter.getView().getGrid().addUnit(unit, x,y);
			CommandCenter.getView().getGrid().revalidate();
			CommandCenter.getView().getGrid().repaint();
			
			}
			unit.setLocation(world[x][y]);
			
				
		}
	}

	public boolean checkGameOver() {
		return plannedDisasters.isEmpty()&&!notIdle()&&!stillActive();
	}



	private boolean notIdle() {
		for(Unit u:getEmergencyUnits()) {
			if (u.getState()!=UnitState.IDLE) {
				return true;
			}
		}
		return false;
	}


	private boolean stillActive() {
		for(Disaster d: executedDisasters ) {

			if (d.isActive()) {
				if (d.getTarget() instanceof Citizen) {
					Citizen citizen= (Citizen) d.getTarget();
					if (citizen.getState()!=CitizenState.DECEASED) {
						return true;
					}
				}
				else {
					if (d.getTarget() instanceof ResidentialBuilding) {
						ResidentialBuilding building= (ResidentialBuilding) d.getTarget();
						if (building.getStructuralIntegrity()!=0) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}


	public int calculateCasualties() {
		int Casualities=0;

		for( Citizen c:citizens) {
			if (c.getState()==CitizenState.DECEASED) {
				Casualities++;
			}
		}
		return Casualities;
	}






	public void nextCycle() 
	{
		currentCycle++;
		logString+="~~~~~~~~ Cycle : "+currentCycle+"  ~~~~~~~~\n";

		for(int i=0;i<plannedDisasters.size();i++) {
			Disaster disaster=plannedDisasters.get(i);
			if(disaster.getStartCycle()==currentCycle) {
				Disaster toBeStrike=disaster;
				//removeIndex.add(i);
				plannedDisasters.remove(i);
				i--;

				if(disaster instanceof Fire) {
					if(disaster.getTarget().getDisaster()!=null&&disaster.getTarget().getDisaster() instanceof GasLeak) {
						GasLeak gasLeak=(GasLeak)disaster.getTarget().getDisaster();
						gasLeak.setActive(false);
						ResidentialBuilding building = (ResidentialBuilding)disaster.getTarget();
						if(building.getGasLevel()==0) {
							toBeStrike=disaster;
							executedDisasters.add(toBeStrike);
							try {
								toBeStrike.strike();
								logString+="~ Disaster "+toBeStrike.getDisasterType()+" hit "+toBeStrike.getTarget().targetType()+"at location :"+
										toBeStrike.getTarget().getLocation().toString()+"\n";
							}catch (CitizenAlreadyDeadException|BuildingAlreadyCollapsedException e1) {
								JOptionPane.showMessageDialog(null,e1.getMessage() ,"Warning" ,JOptionPane.WARNING_MESSAGE);

							}

						}
						else if(building.getGasLevel()>0&&building.getGasLevel()<70) {

							toBeStrike= new Collapse(currentCycle,(ResidentialBuilding) disaster.getTarget());


							disaster.setActive(false);

							executedDisasters.add(toBeStrike);
							((ResidentialBuilding) disaster.getTarget()).setFireDamage(0);
							try {
								toBeStrike.strike();
								logString+="~ Disaster "+toBeStrike.getDisasterType()+" hit "+toBeStrike.getTarget().targetType()+" at location :"+
										toBeStrike.getTarget().getLocation().toString()+"\n";

							}catch (CitizenAlreadyDeadException|BuildingAlreadyCollapsedException e1) {
								JOptionPane.showMessageDialog(null,e1.getMessage() ,"Warning" ,JOptionPane.WARNING_MESSAGE);

							}

						}else if(building.getGasLevel()>=70) {
							disaster.setActive(false);

							building.setStructuralIntegrity(0); 

						}


					}else {
						executedDisasters.add(disaster);
						try {
							disaster.strike();
							logString+="~ Disaster "+disaster.getDisasterType()+" hit "+disaster.getTarget().targetType()+" at location :"+
									disaster.getTarget().getLocation().toString()+"\n";

						}catch (CitizenAlreadyDeadException|BuildingAlreadyCollapsedException e1) {
							JOptionPane.showMessageDialog(null,e1.getMessage() ,"Warning" ,JOptionPane.WARNING_MESSAGE);

						}
					}

				}else if(disaster instanceof GasLeak) {

					if(disaster.getTarget().getDisaster()!=null&&disaster.getTarget().getDisaster() instanceof Fire) {
						disaster.getTarget().getDisaster().setActive(false);
						Collapse collapse = new Collapse(currentCycle, (ResidentialBuilding)disaster.getTarget());
						executedDisasters.add(collapse);

						disaster.setActive(false);

						try {
							collapse.strike();
							logString+="~ Disaster "+collapse.getDisasterType()+" hit "+collapse.getTarget().targetType()+" at location :"+
									collapse.getTarget().getLocation().toString()+"\n";

						}catch (CitizenAlreadyDeadException|BuildingAlreadyCollapsedException e1) {
							JOptionPane.showMessageDialog(null,e1.getMessage() ,"Warning" ,JOptionPane.WARNING_MESSAGE);

						}
						ResidentialBuilding building = (ResidentialBuilding)disaster.getTarget();
						building.setFireDamage(0);


					}else {
						try {
							disaster.strike();
							executedDisasters.add(disaster);
							logString+="~ Disaster "+disaster.getDisasterType()+" hit "+disaster.getTarget().targetType()+" at location :"+
									disaster.getTarget().getLocation().toString()+"\n";

						}catch (CitizenAlreadyDeadException|BuildingAlreadyCollapsedException e1) {
							JOptionPane.showMessageDialog(null,e1.getMessage() ,"Warning" ,JOptionPane.WARNING_MESSAGE);

						}

					}
				}else {
					if( disaster instanceof Collapse&&disaster.getTarget().getDisaster()!=null) {
						disaster.getTarget().getDisaster().setActive(false);
						ResidentialBuilding residentialBuilding=(ResidentialBuilding) disaster.getTarget();
						residentialBuilding.setFireDamage(0);

					}
					try {
						disaster.strike();
						executedDisasters.add(disaster);
						logString+="~ Disaster "+disaster.getDisasterType()+" hit "+disaster.getTarget().targetType()+" at location :"+
								disaster.getTarget().getLocation().toString()+"\n";

					}catch (CitizenAlreadyDeadException|BuildingAlreadyCollapsedException e1) {
						JOptionPane.showMessageDialog(null,e1.getMessage() ,"Warning" ,JOptionPane.WARNING_MESSAGE);

					}
				}



			}
		}






		for(int i=0;i<buildings.size();i++) {
			ResidentialBuilding building = buildings.get(i);
			if(building.getFireDamage()==100) {
				building.getDisaster().setActive(false);
				Collapse collapse = new Collapse(currentCycle,building);
				try {
					collapse.strike();

					logString+="~ Disaster "+collapse.getDisasterType()+" hit "+collapse.getTarget().targetType()+" at location :"+
							collapse.getTarget().getLocation().toString()+"\n";

					executedDisasters.add(collapse);
					building.setFireDamage(0);

				}catch (CitizenAlreadyDeadException|BuildingAlreadyCollapsedException e1) {
					JOptionPane.showMessageDialog(null,e1.getMessage() ,"Warning" ,JOptionPane.WARNING_MESSAGE);

				}

			}
		}



		//looping on the units 
		for(int i=0;i<emergencyUnits.size();i++) {
			emergencyUnits.get(i).cycleStep();

		}



		//looping on executed disasters
		for(int i=0;i<executedDisasters.size();i++) {
			Disaster disaster =executedDisasters.get(i);
			if(disaster.getStartCycle()<currentCycle&& disaster.isActive()) {
				disaster.cycleStep();
			}
		}



		//looping on buildings and citizens
		for(int i=0;i<buildings.size();i++) {
			buildings.get(i).cycleStep();
		}
		for(int i=0;i<citizens.size();i++) {
			citizens.get(i).cycleStep();
		}


//----------------------
		logString+="~ Active disasters : \n";
		boolean addedAtLeastOnce=false;
		for(int i=0;i<executedDisasters.size();i++) {
			if(executedDisasters.get(i).isActive()) {
				logString+=executedDisasters.get(i).getDisasterType()+" is affecting "+executedDisasters.get(i).getTarget().targetType()+" at location "
					+executedDisasters.get(i).getTarget().getLocation().toString()+". \n";
				addedAtLeastOnce=true;
			}
		}
if(!addedAtLeastOnce) {
	logString+="No active disasters at the moment. \n";
}

	}



	public int getCurrentCycle() {
		return currentCycle;
	}


}