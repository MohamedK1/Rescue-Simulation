package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import exceptions.SimulationException;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.disasters.Injury;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import simulation.Address;
import simulation.Rescuable;

public class GasControlUnit extends FireUnit{

	public GasControlUnit(String unitID, Address location, int stepsPerCycle,WorldListener listener) {
		super(unitID, location, stepsPerCycle,listener);
	}

	@Override
	public void respond(Rescuable r) throws IncompatibleTargetException,CannotTreatException {
		if(! (r instanceof ResidentialBuilding)) {
			throw new IncompatibleTargetException(this, r,"You can't send a gas control unit to a citizen !");
			}
		if(!canTreat(r)) {
			throw new CannotTreatException(this,r,"The target is safe !");
		}else if (!(r.getDisaster() instanceof GasLeak)) {
			throw new CannotTreatException(this,r,"The gas control unit can't handle "+r.getDisaster().getDisasterType()+" !");
			
			
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

			if( building.getDisaster() instanceof GasLeak) {
				GasLeak gasLeak=(GasLeak)building.getDisaster();
				gasLeak.setActive(false);
				building.setGasLevel(building.getGasLevel()-10);

			}


			if(building.getGasLevel()==0) { //sarah
				jobsDone();
			}
		}

	}

	public String getUnitType() {
		return "Gas Control Unit";
	}
}