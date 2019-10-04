package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Disaster;
import model.disasters.Injury;
import model.events.WorldListener;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;

public class Ambulance extends MedicalUnit {

	public Ambulance(String unitID, Address location, int stepsPerCycle, WorldListener listener)  {
		super(unitID, location, stepsPerCycle, listener);

	}


	@Override
	public void respond(Rescuable r) throws IncompatibleTargetException,CannotTreatException{
		if(! (r instanceof Citizen )) {
			throw new IncompatibleTargetException(this, r,"You can't send an ambulance to treat a building !");
		}
		if(!canTreat(r)) {
			throw new CannotTreatException(this,r,"The target is safe !");
		}else if (!(r.getDisaster() instanceof Injury)) {
			throw new CannotTreatException(this,r,"The ambulance can't handle Infections !");
					
		}

	
		super.respond(r);
	}

	@Override
	public void treat() {
		if(this.getState()!=UnitState.IDLE&&this.getTarget()!=null) {
			//setState(UnitState.TREATING);
			super.treat();
			Citizen citizen = (Citizen)this.getTarget();
			Disaster disaster=citizen.getDisaster();
			if (citizen.getHp()==0) {
				jobsDone();
			}
			else {
				if(citizen.getBloodLoss()==0) {
					citizen.setState(CitizenState.RESCUED);
					this.heal();
				}
				if(disaster instanceof Injury) {
					disaster.setActive(false);
					citizen.setBloodLoss(citizen.getBloodLoss()-this.getTreatmentAmount());
					if (citizen.getBloodLoss() == 0)// have changed with comparison with solution (but we already handled it in setBloodLoss but this in case if we changed setBloodLoss)
						citizen.setState(CitizenState.RESCUED);


				}
			}
		}

	}

public String getUnitType() {
	return "Ambulance";
}

}
