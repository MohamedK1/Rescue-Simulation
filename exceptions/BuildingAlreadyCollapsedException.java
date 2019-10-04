package exceptions;

import model.disasters.Disaster;

public class BuildingAlreadyCollapsedException extends DisasterException {

	public BuildingAlreadyCollapsedException(Disaster disaster, String msg) {
		super(disaster, msg);
	}

	public BuildingAlreadyCollapsedException(Disaster disaster) {
		super(disaster);
	}

}
