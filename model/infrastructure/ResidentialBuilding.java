package model.infrastructure;

import java.awt.Image;
import java.util.ArrayList;

import model.disasters.Collapse;
import model.disasters.Disaster;
import model.disasters.Fire;
import model.events.SOSListener;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

public class ResidentialBuilding implements Rescuable,Simulatable,Comparable<ResidentialBuilding> {

	private Address location;
	private int structuralIntegrity;
	private int fireDamage;
	private	int gasLevel;
	private int foundationDamage;
	private ArrayList<Citizen> occupants;
	private Disaster disaster;
	private Image buildingImage;
	private Image buildingUnitImage;
	private int stillalive;
	public void setStillalive() {
		stillalive=0;
		for(Citizen c : occupants) {
			if(c.getState()!=CitizenState.DECEASED) {
				stillalive++;
			}
		}
	}
	public Image getBuildingImage() {
		return buildingImage;
	}


	public void setBuildingImage(Image buildingImage) {
		this.buildingImage = buildingImage;
	}


	public Image getBuildingUnitImage() {
		return buildingUnitImage;
	}


	public void setBuildingUnitImage(Image buildingUnitImage) {
		this.buildingUnitImage = buildingUnitImage;
	}
	private SOSListener emergencyService;


	public void setEmergencyService(SOSListener emergencyService) {
		this.emergencyService = emergencyService;
	}


	public ResidentialBuilding(Address location) {
		this.location = location;
		structuralIntegrity=100;
		fireDamage=0;
		gasLevel=0;
		foundationDamage=0;
		occupants=new ArrayList<Citizen>();

	}


	public int getStructuralIntegrity() {
		return structuralIntegrity;
	}

	public void setStructuralIntegrity(int structuralIntegrity) {
		if(structuralIntegrity<=0) {
			this.structuralIntegrity=0;
			for(Citizen c : occupants) {
				c.setHp(0);
			}
			
		}else {
			this.structuralIntegrity = structuralIntegrity;
		}
	}

	
public int getFireDamage() {
		return fireDamage;
	}


	public void setFireDamage(int fireDamage) {
		if(fireDamage<=0) {
			this.fireDamage=0;
		}else if(fireDamage>100) {
			this.fireDamage=100;
		}else {

			this.fireDamage = fireDamage;
		}
	}
	
public int getGasLevel() {
		return gasLevel;
	}



	
public void setGasLevel(int gasLevel) {

		if(gasLevel>=100) {
			this.gasLevel=100;
			for(Citizen c : occupants) {
				c.setHp(0);
			}
		}else if(gasLevel<0) {
			this.gasLevel=0;
		}else {

			this.gasLevel = gasLevel;
		}
	}



	public int getFoundationDamage() {
		return foundationDamage;
	}

	public void setFoundationDamage(int foundationDamage) {

		if(foundationDamage>=100) {
			setStructuralIntegrity(0);
	this.foundationDamage=100;
		}else {
		this.foundationDamage=foundationDamage;
		}
	}



	public Address getLocation() {
		return location;
	}
	public ArrayList<Citizen> getOccupants() {
		return occupants;
	}

	public Disaster getDisaster() {
		return disaster;
	}



	public void addCitizen(Citizen citizen) {
		occupants.add(citizen);
	}


	@Override
	public void struckBy(Disaster d) {
		if(disaster!=null) // have changed with comparison with the solution (but it's redundant we handled it in nextCycle)
			disaster.setActive(false);
		
		this.disaster=d;
		emergencyService.receiveSOSCall(this);


	}
	public boolean allDead()
	{
		
		for(int i=0;i<occupants.size();i++) {
			if(occupants.get(i).getState()!=CitizenState.DECEASED)return false;
			
		}
		return true;
	}
	
	@Override
	
public void cycleStep() {
		if(getFoundationDamage()>0) {
			int decrease = ((int)(Math.random()*6))+5;
			setStructuralIntegrity(getStructuralIntegrity()-decrease);
		}

		if(getFireDamage()>0&&getFireDamage()<30) {
			setStructuralIntegrity(getStructuralIntegrity()-3);
		}else if(getFireDamage()>=30&&getFireDamage()<70) {
			setStructuralIntegrity(getStructuralIntegrity()-5);
		}else if(getFireDamage()>=70) {
			setStructuralIntegrity(getStructuralIntegrity()-7);

		}
	}
	public String targetType() {
		return "building";
	}
	public String toString() {
		return  "Building's Location:"+getLocation().toString()+".\n"+
				"Building's Structural Integrity :"+getStructuralIntegrity()+".\n"+
				"Building's Fire Damage :"+getFireDamage()+".\n"+
				"Building's Gas Level :"+getGasLevel()+".\n"+
				"Building's Foundation Damage :"+getFoundationDamage()+".\n"+
				"Number of Citizens in the Building :"+getOccupants().size()+".\n"+
				"Building's Disaster: "+(getDisaster()==null?"No Disaster.\n":getDisaster().getDisasterType()+"\n");
	}


	@Override
	public int compareTo(ResidentialBuilding o) {
		if(o.getDisaster() instanceof Collapse)
			return o.stillalive-stillalive;
		else if(o.getDisaster() instanceof Fire){
			return o.fireDamage-fireDamage;
		}else {
			return o.gasLevel-gasLevel;
		}
	}

}
