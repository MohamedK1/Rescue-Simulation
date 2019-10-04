package exceptions;

import model.units.Unit;
import simulation.Rescuable;

public abstract class UnitException extends SimulationException{
	private Unit unit;
	private Rescuable target;

	public UnitException(Unit unit, Rescuable target) {
		super();
		this.unit = unit;
		this.target = target;
	}
	public UnitException(Unit unit, Rescuable target,String msg) {
		super(msg);
		this.unit = unit;
		this.target = target;
	}
	public Unit getUnit() {
		return unit;
	}
	public Rescuable getTarget() {
		return target;
	}
}
