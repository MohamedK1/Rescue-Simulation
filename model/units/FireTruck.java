package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import exceptions.SimulationException;
import model.disasters.Fire;
import model.disasters.Injury;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;
import simulation.Rescuable;

public class FireTruck extends FireUnit {

	public FireTruck(String unitID, Address location, int stepsPerCycle,WorldListener listener) {
		super(unitID, location, stepsPerCycle,listener);

	}

	@Override
	public void respond(Rescuable r) throws IncompatibleTargetException,CannotTreatException {
		if(!(r instanceof ResidentialBuilding)) {
			throw new IncompatibleTargetException(this, r,"You can't send a fire truck to a citizen !");
			}
	
		if(!canTreat(r)) {
			throw new CannotTreatException(this,r,"The target is safe !");
		}else if (!(r.getDisaster() instanceof Fire)) {
			throw new CannotTreatException(this,r,"The fire truck can't handle "+r.getDisaster().getDisasterType()+" !");
			
		}
		
		
		
		
		super.respond(r);
	}



	@Override
	public void treat() {
		ResidentialBuilding building =(ResidentialBuilding)this.getTarget();

		super.treat();
		//setState(UnitState.TREATING);
		if(building.getStructuralIntegrity()==0) { //sarah
			jobsDone();
		}
		else {

			if( building.getDisaster() instanceof Fire) {
				Fire fire=(Fire)building.getDisaster();
				fire.setActive(false);
				building.setFireDamage(building.getFireDamage()-10);
			}
			if(building.getFireDamage()==0) { //sarah
				jobsDone();
			}
		}


	}
	public String getUnitType() {
		return "Fire Truck";
	}
}
