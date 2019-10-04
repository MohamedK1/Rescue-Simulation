package exceptions;

public abstract class SimulationException extends Exception{
	public SimulationException () {
		super();
	}
	public SimulationException (String msg) {
		super(msg);
	}
}
