package model.units;

import java.awt.Image;

import controller.CommandCenter;
import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Collapse;
import model.events.SOSResponder;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Unit implements Simulatable,SOSResponder {
	private String unitID;
	private UnitState state;	
	private Address location;
	private Rescuable target;
	private int distanceToTarget;
	private int stepsPerCycle;
	private Image unitImage;
	

	public Image getUnitImage() {
		return unitImage;
	}

	public void setUnitImage(Image unitImage) {
		this.unitImage = unitImage;
	}
	private WorldListener  worldListener;




	public int calculateDistance(Address location ) {
		return Math.abs(getLocation().getX()-location.getX())+Math.abs(getLocation().getY()-location.getY());
	}

	public Unit(String unitID, Address location, int stepsPerCycle,WorldListener worldListener)  {

		this.unitID = unitID;
		this.location = location;
		this.stepsPerCycle = stepsPerCycle;
		this.state=UnitState.IDLE;
		this.worldListener= worldListener;
		
	}





	public WorldListener getWorldListener() {
		return worldListener;
	}

	public void setWorldListener(WorldListener worldListener) {
		this.worldListener = worldListener;
	}



	public UnitState getState() {
		return state;
	}
	public void setState(UnitState state) {
		this.state = state;
	}
	public Address getLocation() {
		return location;
	}
	public void setLocation(Address location) {
		this.location = location;
	}
	public String getUnitID() {
		return unitID;
	}
	public Rescuable getTarget() {
		return target;
	}
	public int getStepsPerCycle() {
		return stepsPerCycle;
	}





	public void setDistanceToTarget(int distanceToTarget) {
		this.distanceToTarget = distanceToTarget;
	}



	@Override 
	public  void cycleStep() 
	{
		if(this.getState()!=UnitState.IDLE && target!=null) {
			if(distanceToTarget>0) {
				distanceToTarget-=stepsPerCycle;
				if(distanceToTarget<=0) {
					distanceToTarget=0;
					worldListener.assignAddress(this,target.getLocation().getX(), target.getLocation().getY());
				}
			}else {
			worldListener.assignAddress(this,target.getLocation().getX(), target.getLocation().getY());
			
			if(!(this.getTarget().getDisaster() instanceof Collapse)) {
			this.getTarget().getDisaster().setActive(false);
			}
			this.setState(UnitState.TREATING);
			CommandCenter.getView().getUnitPanel().transferToTreating(this);
			this.treat();
		
			
			}
		}
	}
	

	
	public void treat() {
		this.setState(UnitState.TREATING);
		
		if(!(this.getTarget().getDisaster() instanceof Collapse)) {
			
		this.getTarget().getDisaster().setActive(false);
		}
	}
	
	
public void jobsDone() {
	this.setState(UnitState.IDLE);
		target=null;
	CommandCenter.getView().getUnitPanel().transferToAvailable(this);
}

@Override
public void respond(Rescuable r) throws IncompatibleTargetException,CannotTreatException  {
	
	
	
	
	if(target!=null) {
		if((! (this instanceof MedicalUnit))&&(getState()==UnitState.TREATING)) {
		target.getDisaster().setActive(true);
		}else {
			Citizen citizen =(Citizen)this.getTarget();
			
			if(citizen.getBloodLoss()!=0||citizen.getToxicity()!=0) {
				if(getState()==UnitState.TREATING)// have changed with comparison with the solution( but it's redundant because if the state not treating so it will be IDle and the target will be null or responding and the disaster is still active)
				citizen.getDisaster().setActive(true);
			}
		}
		
		
	}
	
	distanceToTarget=calculateDistance(r.getLocation());
	if (this instanceof Evacuator) {
		Evacuator evacuator=(Evacuator)this;
		evacuator.setDistanceToBase(0);
	}
	
	this.setState(UnitState.RESPONDING);
		target=r;
}

public  boolean canTreat(Rescuable r) {
	
	if(r instanceof ResidentialBuilding ) {
	 ResidentialBuilding building = (ResidentialBuilding)r;
	 if(building.getFireDamage()==0&&building.getGasLevel()==0&&building.getFoundationDamage()==0) {
		 return false;
	 }else {
		 return true;
	 }
	}else {
		Citizen citizen = (Citizen)r;
		if(citizen.getState()==CitizenState.SAFE||citizen.getState()==CitizenState.RESCUED ) {
			return false;
		}
		return true;
	}
	
	
	
	
	
	
	
}
public String getUnitType() {
	String s = this.getClass().toString();
	String[] list = s.split(".");
	
	return list[list.length-1];
}
public String toString() {
	
	return "Unit ID: "+getUnitID()+".\n"+
			"Unit Type: "+getUnitType()+".\n"+
			"Unit State: "+getState()+".\n"+
			"Unit Location: "+getLocation().toString()+".\n"+
			"Unit's Steps per Cycle: "+getStepsPerCycle()+".\n"+
			"Unit's Target: "+(target==null?"No target.\n":target instanceof Citizen? "Citizen. "+ "\nCitizen Location:"+target.getLocation().toString()+".\n":"Residential Building\n"+"Building's Location: "+target.getLocation().toString()+".\n");
			
	
	
	
	
	
	
	
}
}
