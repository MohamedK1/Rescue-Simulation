package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.SimulationException;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import simulation.Rescuable;
import simulation.Simulatable;

abstract public class Disaster implements Simulatable {
	private int startCycle;
	private Rescuable target;
	private boolean active;

	public Disaster(int startCycle, Rescuable target) {
		this.startCycle = startCycle;
		this.target = target;
		this.active=false;
	}


	public  void strike() throws BuildingAlreadyCollapsedException,CitizenAlreadyDeadException {
		this.setActive(true);
	}

	
	public abstract void cycleStep() ;
		

	public boolean isActive() {

		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	public int getStartCycle() {
		return startCycle;
	}
	public Rescuable getTarget() {
		return target;
	} 
	public String getDisasterType() {
		String s=getClass()+"";
		return s;
	}



}
