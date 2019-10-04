package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.SimulationException;
import model.infrastructure.ResidentialBuilding;

public class Fire  extends Disaster{
	public Fire(int cycle, ResidentialBuilding target) {
		super(cycle,target);
	}
	
	@Override
	public void cycleStep() {
		if(this.isActive()) {
			ResidentialBuilding building= (ResidentialBuilding) this.getTarget();
			building.setFireDamage(building.getFireDamage()+10);
	
		}
		
	}
	@Override
	public void strike() throws BuildingAlreadyCollapsedException,CitizenAlreadyDeadException {
		ResidentialBuilding building= (ResidentialBuilding) this.getTarget();
		if (building.getStructuralIntegrity()==0) {
			throw new BuildingAlreadyCollapsedException(this,"the building in location :"+building.getLocation().toString()+" already collapsed !");
		}
		building.setFireDamage(building.getFireDamage()+10);
		building.struckBy(this);
		super.strike();

	}
	@Override
	public String getDisasterType() {
		return "Fire";
	}
}
