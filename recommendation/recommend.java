package recommendation;

import java.util.ArrayList;
import java.util.Collections;

import com.sun.jndi.cosnaming.IiopUrl.Address;

import controller.CommandCenter;
import model.disasters.Collapse;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.disasters.Infection;
import model.disasters.Injury;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.units.GasControlUnit;
import model.units.Evacuator;
import model.units.FireTruck;
import model.units.Unit;
import model.units.Ambulance;
import model.units.DiseaseControlUnit;;


public class recommend {
	private CommandCenter commandCenter;
	private static ArrayList<ResidentialBuilding> collapseBuildings;
	private static ArrayList<ResidentialBuilding> fireBuildings;
	private static ArrayList<ResidentialBuilding> gasleakBuildings;
	private static ArrayList<Unit> FireTruck;
	private static ArrayList<Unit> Evacuator;
	private static ArrayList<Unit> GasControl;
	private static ArrayList<Unit> Ambulance;
	private static ArrayList<Unit> DiseaseControl;
	private static ArrayList<Citizen>infectedCitizen;
	private static ArrayList<Citizen>injuriedCitizen;
	
	public recommend(CommandCenter commandCenter) {
		this.commandCenter = commandCenter;
		
		
	}
	
	public void setinfectedCitizen() {
		ArrayList<Citizen> c =commandCenter.getVisibleCitizens();
		infectedCitizen=new ArrayList<>();
		for(Citizen x:c) {
			if(x.getDisaster() instanceof Infection && x.getToxicity()>0) {
				infectedCitizen.add(x);
			}
		}
	}
	public void setinjuriedCitizen() {
		ArrayList<Citizen> c =commandCenter.getVisibleCitizens();
		injuriedCitizen=new ArrayList<>();
		for(Citizen x:c) {
			if(x.getDisaster() instanceof Injury && x.getBloodLoss()>0) {
				injuriedCitizen.add(x);
			}
		}
	}
	public void setFireTruck() {
		ArrayList<Unit> B =commandCenter.getEmergencyUnits();
		FireTruck=new ArrayList<>();
		for(Unit x:B) {
			if(x instanceof FireTruck) {
				FireTruck.add(x);
			}
		}
	}
	
	public void setDiseaseControl() {
		ArrayList<Unit> B =commandCenter.getEmergencyUnits();
		DiseaseControl=new ArrayList<>();
		for(Unit x:B) {
			if(x instanceof DiseaseControlUnit) {
				DiseaseControl.add(x);
			}
		}
	}
	public void setAmbulance() {
		ArrayList<Unit> B =commandCenter.getEmergencyUnits();
		Ambulance=new ArrayList<>();
		for(Unit x:B) {
			if(x instanceof Ambulance) {
				Ambulance.add(x);
			}
		}
	}
	public void setEvacuator() {
		ArrayList<Unit> B =commandCenter.getEmergencyUnits();
		Evacuator=new ArrayList<>();
		for(Unit x:B) {
			if(x instanceof Evacuator) {
				Evacuator.add(x);
			}
		}
	}
	public void setGasControl() {
		ArrayList<Unit> B =commandCenter.getEmergencyUnits();
		GasControl=new ArrayList<>();
		for(Unit x:B) {
			if(x instanceof GasControlUnit) {
				GasControl.add(x);
			}
		}
	}
	public void setCollapseBuildings() {
		ArrayList<ResidentialBuilding> B =commandCenter.getVisibleBuildings();
		collapseBuildings=new ArrayList<>();
		for(ResidentialBuilding x:B) {
			if(x.getDisaster() instanceof Collapse ) {
				collapseBuildings.add(x);
			}
		}
	}
	public void setFireBuildings() {
		ArrayList<ResidentialBuilding> B =commandCenter.getVisibleBuildings();
		fireBuildings=new ArrayList<>();
		for(ResidentialBuilding x:B) {
			if(x.getDisaster() instanceof Fire && x.getFireDamage()>0) {
				fireBuildings.add(x);
			}
		}
	}
	public void setGasleakBuildings() {
		ArrayList<ResidentialBuilding> B =commandCenter.getVisibleBuildings();
		gasleakBuildings=new ArrayList<>();
		for(ResidentialBuilding x:B) {
			if(x.getDisaster() instanceof GasLeak && x.getGasLevel()>0) {
				gasleakBuildings.add(x);
			}
		}
	}
	public static String recommendGasControl () {
		String recommend="";
		Collections.sort(gasleakBuildings);
		for(ResidentialBuilding building : gasleakBuildings) {
			if(building.getStructuralIntegrity()==0)continue;

			Unit best=null;
			double time=Integer.MAX_VALUE;
			for(Unit unit:GasControl) {
				double t=getDistance(unit.getLocation(),building.getLocation())*1.0/unit.getStepsPerCycle();
				if(t<time) {
					time=t;
					best=unit;
				}
			}
			if(best!=null) {
				GasControl.remove(best);
				if(best.getTarget()!=building) {

				recommend+="Dispatch Gas Control Unit "+best.getUnitID()+" to the building in location : x="+building.getLocation().getX()+" y="+building.getLocation().getY();
				recommend+="\n";
				}
			}
		}
		return recommend;
		
	}
	public static String recommendFireTruck () {
		String recommend="";
		Collections.sort(fireBuildings);
		for(ResidentialBuilding building : fireBuildings) {
			if(building.getStructuralIntegrity()==0)continue;

			Unit best=null;
			double time=Integer.MAX_VALUE;
			for(Unit unit:FireTruck) {
				double t=getDistance(unit.getLocation(),building.getLocation())*1.0/unit.getStepsPerCycle();
				if(t<time) {
					time=t;
					best=unit;
				}
			}
			if(best!=null) {
				FireTruck.remove(best);
				if(best.getTarget()!=building) {

				recommend+="Dispatch FireTruck "+best.getUnitID()+" to the building in location : x="+building.getLocation().getX()+" y="+building.getLocation().getY();
				recommend+="\n";
				}
			}
		}
		return recommend;
		
	}
	public static String recommendEvacuator () {
		String recommend="";
	
			//System.out.println("FICollapse!!!");
		Collections.sort(collapseBuildings);
		for(ResidentialBuilding building : collapseBuildings) {
			if(building.getStructuralIntegrity()==0)continue;
			Unit best=null;
			double size=0;
			for(Unit unit:Evacuator) {
				Evacuator e=(Evacuator)unit;
				double t=getDistance(e.getLocation(),e.getLocation())*1.0/e.getStepsPerCycle();
				t+=(building.getLocation().getX()+building.getLocation().getY())*2.0/e.getStepsPerCycle()*e.getMaxCapacity();
				if(t>size) {
					size=t;
					best=unit;
				}
			}
			if(best!=null) {
				Evacuator.remove(best);
				if(best.getTarget()!=building) {
				recommend+="Dispatch Evacuator "+best.getUnitID()+" to the building in location : x="+building.getLocation().getX()+" y="+building.getLocation().getY();
				recommend+="\n";
				}
			}
		}
		
		return recommend;
		
	}
	public static String recommendAmbulance () {
		String recommend="";
		Collections.sort(injuriedCitizen);
		for(Citizen c : injuriedCitizen) {
			Unit best=null;
			if(c.getHp()==0) {
				continue;
			}
			double time=Integer.MAX_VALUE;
			for(Unit unit:Ambulance) {
				double t=getDistance(unit.getLocation(),c.getLocation())*1.0/unit.getStepsPerCycle();
				if(t<time) {
					time=t;
					best=unit;
				}
			}
			if(best!=null) {
				Ambulance.remove(best);
				if(best.getTarget()!=c) {
				recommend+="Dispatch Ambulance "+best.getUnitID()+" to the citizin in location : x="+c.getLocation().getX()+" y="+c.getLocation().getY();
				recommend+="\n";
				}
			}
		}
		return recommend;
		
	}
	public static String recommendDiseaseControl () {
		String recommend="";
		Collections.sort(infectedCitizen);
		for(Citizen c : infectedCitizen) {
			if(c.getHp()==0) {
				continue;
			}
			Unit best=null;
			double time=Integer.MAX_VALUE;
			for(Unit unit:DiseaseControl) {
				double t=getDistance(unit.getLocation(),c.getLocation())*1.0/unit.getStepsPerCycle();
				if(t<time) {
					time=t;
					best=unit;
				}
			}
			if(best!=null ) {
				DiseaseControl.remove(best);
				if(best.getTarget()!=c) {
				recommend+="Dispatch DiseaseControlUnit "+best.getUnitID()+" to the citizin in location : x="+c.getLocation().getX()+" y="+c.getLocation().getY();
				recommend+="\n";
				}
			}
		}
		return recommend;
		
	}
	public static int getDistance(simulation.Address a ,simulation.Address b) {
		return Math.abs(a.getX()-b.getX())+Math.abs(a.getY()-b.getY());
	}
}

