package exceptions;

import model.disasters.Disaster;

public class CitizenAlreadyDeadException extends DisasterException {

	public CitizenAlreadyDeadException(Disaster disaster, String msg) {
		super(disaster, msg);
	}

	public CitizenAlreadyDeadException(Disaster disaster) {
		super(disaster);
	}

}
