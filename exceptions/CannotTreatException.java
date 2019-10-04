package exceptions;

import model.units.Unit;
import simulation.Rescuable;

public class CannotTreatException extends UnitException {

	public CannotTreatException(Unit unit, Rescuable target, String msg) {
		super(unit, target, msg);
	}

	public CannotTreatException(Unit unit, Rescuable target) {
		super(unit, target);
	}

}
