package view;

import javax.swing.JButton;

import model.people.Citizen;

public class CitizenButton extends JButton{
	private Citizen citizen;
	public CitizenButton (Citizen citizen) {
		super(citizen.getName());	
		this.citizen=citizen;
  }
	public Citizen getCitizen() {
		return citizen;
	}
	
 
}
