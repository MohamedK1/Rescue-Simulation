package model.units;

import java.util.ArrayList;

import model.events.WorldListener;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;

abstract public class PoliceUnit extends Unit{
	private ArrayList<Citizen> passengers;
	private int maxCapacity;
	private int distanceToBase;
	
	
	
	
	public PoliceUnit(String unitID, Address location, int stepsPerCycle,WorldListener listener,int maxCapacity) {
		super(unitID, location, stepsPerCycle,listener);
		this.maxCapacity = maxCapacity;
		passengers = new ArrayList<Citizen>();
	}
	
	public int getDistanceToBase() {
		return distanceToBase;
	}
	public void setDistanceToBase(int distanceToBase) {
		this.distanceToBase = distanceToBase;
	}
	public int getMaxCapacity() {
		return maxCapacity;
	}

	public ArrayList<Citizen> getPassengers() {
		return passengers;
	
	}
	

	
}
