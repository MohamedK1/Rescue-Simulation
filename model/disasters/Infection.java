package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.SimulationException;
import model.people.Citizen;
import model.people.CitizenState;

public class Infection extends Disaster {
	public Infection(int cycle, Citizen target) {
		super(cycle,target);
	}
	
	@Override
	public void cycleStep() {
		if(this.isActive()) {
			Citizen citizen = (Citizen) this.getTarget();
			citizen.setToxicity(citizen.getToxicity()+15);

		}
	}
	@Override
	public void strike() throws BuildingAlreadyCollapsedException,CitizenAlreadyDeadException {
		Citizen citizen = (Citizen) this.getTarget();
		if (citizen.getState()==CitizenState.DECEASED) {
			throw new CitizenAlreadyDeadException(this,"Citizen :"+citizen.getName()+" in location :"+citizen.getLocation().toString()+" is already deceased!");
		}
		citizen.setToxicity(citizen.getToxicity()+25);
		citizen.struckBy(this);
		super.strike();



	}
	@Override
	public String getDisasterType() {
		return "Infection";
	}
}
