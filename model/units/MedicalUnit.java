package model.units;

import java.io.IOException;

import com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm.WordListener;

import model.disasters.Disaster;
import model.disasters.Infection;
import model.disasters.Injury;
import model.events.WorldListener;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;

abstract public class MedicalUnit extends Unit {

	private  int healingAmount;
	private int treatmentAmount;

	public MedicalUnit(String unitID, Address location, int stepsPerCycle,WorldListener listener)  {
		super(unitID, location, stepsPerCycle,listener);
		healingAmount=10;
		treatmentAmount=10;

	}

	public int getTreatmentAmount() {
		return treatmentAmount;
	}
	
	
	

	
public void heal() {
	Citizen citizen =(Citizen)getTarget();
	

	
	citizen.setHp(citizen.getHp()+healingAmount);
	if(citizen.getHp()==100) {
		jobsDone();
		citizen.setState(CitizenState.RESCUED);
	}


}
	


}
