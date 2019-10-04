package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.SimulationException;
import model.infrastructure.ResidentialBuilding;

public class GasLeak extends Disaster{
	public GasLeak(int cycle, ResidentialBuilding target) {
		super(cycle,target);
	}
	
	
	@Override
	public void cycleStep() {
		if(this.isActive()) {
			ResidentialBuilding building= (ResidentialBuilding) this.getTarget();
			
			building.setGasLevel(building.getGasLevel()+15);
		
		}
	}
	
	
	@Override
	
	
	public void strike() throws BuildingAlreadyCollapsedException,CitizenAlreadyDeadException {
ResidentialBuilding building= (ResidentialBuilding) this.getTarget();
if (building.getStructuralIntegrity()==0) {
	throw new BuildingAlreadyCollapsedException(this,"the building in location :"+building.getLocation().toString()+" already collapsed !");
}
super.strike();
building.setGasLevel(building.getGasLevel()+10);
building.struckBy(this);

	}
	@Override
	public String getDisasterType() {
		return "Gas Leak";
	}
	
}
