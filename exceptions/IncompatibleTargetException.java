package exceptions;

import model.units.Unit;
import simulation.Rescuable;

public class IncompatibleTargetException extends UnitException {

	public IncompatibleTargetException(Unit unit, Rescuable target, String msg) {
		super(unit, target, msg);
	}

	public IncompatibleTargetException(Unit unit, Rescuable target) {
		super(unit, target);
	}


}
