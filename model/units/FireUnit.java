package model.units;

import model.events.WorldListener;
import simulation.Address;

abstract public class FireUnit extends Unit {

	public FireUnit(String unitID, Address location, int stepsPerCycle,WorldListener listener) {
		super(unitID, location, stepsPerCycle,listener);
		}

	

	
}
