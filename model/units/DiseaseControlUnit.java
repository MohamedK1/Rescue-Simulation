package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import exceptions.SimulationException;
import model.disasters.Disaster;
import model.disasters.Infection;
import model.disasters.Injury;
import model.events.WorldListener;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;

public class DiseaseControlUnit extends MedicalUnit{

	public DiseaseControlUnit(String unitID, Address location, int stepsPerCycle,WorldListener listener) {
		super(unitID, location, stepsPerCycle,listener);

	}


	@Override
	public void respond(Rescuable r) throws IncompatibleTargetException,CannotTreatException{
		if(! (r instanceof Citizen)) {
			throw new IncompatibleTargetException(this, r,"You can't send a disease control unit to treat a building !");
			}
		if( !canTreat(r)) {
			throw new CannotTreatException(this,r,"The target is safe !");
		}else if (!(r.getDisaster() instanceof Infection)) {
			throw new CannotTreatException(this,r,"The disease control unit can't handle injuries !");
					
		}
		
		
		super.respond(r);
	}



	@Override
	public void treat() {
		Citizen citizen = (Citizen)this.getTarget();
		Disaster disaster=citizen.getDisaster();
		//else {
		//	setState(UnitState.TREATING);
		super.treat();
			if (citizen.getHp()==0) {
				jobsDone();
			}else {
			if(citizen.getToxicity()==0) {
				citizen.setState(CitizenState.RESCUED);
				this.heal();

			}
			if(  disaster instanceof Infection) {
				disaster.setActive(false);
				DiseaseControlUnit diseaseControlUnit = (DiseaseControlUnit)this;
				citizen.setToxicity(citizen.getToxicity()-diseaseControlUnit.getTreatmentAmount());
				if (citizen.getToxicity() == 0) // have changed with comparison with solution (but we already handled it in setToxicity but this in case if we changed setToxicity)
					citizen.setState(CitizenState.RESCUED);
			
			}
		}

			
	}
	public String getUnitType() {
		return "Disease Control Unit";
	}

}
