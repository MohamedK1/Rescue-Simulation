package exceptions;

import model.disasters.Disaster;

public abstract class DisasterException extends SimulationException{

	private Disaster disaster;

	public DisasterException(Disaster disaster) {
		super();
		this.disaster = disaster;
	}
	public DisasterException(Disaster disaster,String msg) {
		super(msg);
		this.disaster = disaster;
	}
	public Disaster getDisaster() {
		return disaster;
	}


}
