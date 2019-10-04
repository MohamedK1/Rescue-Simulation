
package model.people;

import java.awt.Image;

import controller.CommandCenter;
import model.disasters.Disaster;
import model.disasters.Injury;
import model.events.SOSListener;
import model.events.WorldListener;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;
import simulation.Simulator;

public class Citizen implements Rescuable,Simulatable,Comparable<Citizen>{
	private	CitizenState state;
	private	Disaster disaster;
	private Address location;
	private  String nationalID;
	private String name;
	private int age;
	private int hp;
	private	int bloodLoss;
	private	int toxicity;

	private SOSListener emergencyService;
	private WorldListener worldListener;
	private Image img;

	public Image getImg() {
		return img;
	}


	public void setImg(Image img) {
		this.img = img;
	}

 
	public Citizen(Address location, String nationalID, String name, int age,WorldListener worldListener) {

		hp=100;
		bloodLoss=0;
		toxicity=0;
		state=CitizenState.SAFE;
		this.location = location;
		this.nationalID = nationalID;
		this.name = name;
		this.age = age;
		this.worldListener=worldListener;
	}


	public CitizenState getState() {
		return state;
	}

	public void setState(CitizenState state) {
		this.state = state;
	}

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	public int getHp() {
		return hp;
	}



	public void setHp(int hp) {
		if(hp>=100) {
			this.hp=100;
			
		}else if(hp<0) {
			this.hp=0;
		
		}else {
			this.hp = hp;
		}

		if(this.hp==0) {
if(this.getState()!=CitizenState.DECEASED) {
			Simulator.setLogString(Simulator.getLogString()+"~"+this.getName()+" died in location "+this.getLocation().toString()+". \n");
}
			setState(CitizenState.DECEASED);
		
			
		}
		if(this.hp==100) {
			setState(CitizenState.RESCUED);// check for M2
		}
	}


	public int getBloodLoss() {
		return bloodLoss;
	}



	
public void setBloodLoss(int bloodLoss) {
		if(bloodLoss >=100) {
			this.bloodLoss=100;
			setHp(0);
		}else if (bloodLoss<0) {
			this.bloodLoss=0;
		}else {
			this.bloodLoss=bloodLoss;
		}
		
		if(getBloodLoss()==0) {
			this.setState(CitizenState.RESCUED);// check for M2
		}
	}

	

	public int getToxicity() {
		return toxicity;
	}

	
public void setToxicity(int toxicity) {
		if(toxicity>=100) {
			this.toxicity=100;
			setHp(0);
		}else if(toxicity<0) {
			this.toxicity=0;
		}else {
			this.toxicity=toxicity;
		}
		
		if(getToxicity()==0) {
			this.setState(CitizenState.RESCUED);// check for M2
		}
	}

	public Disaster getDisaster() {
		return disaster;
	}
	public String getNationalID() {
		return nationalID;
	}
	public String getName() {
		return name;
	}
	public int getAge() {
		return age;
	}

	public WorldListener getWorldListener() {
		return worldListener;
	}


	public void setWorldListener(WorldListener worldListener) {
		this.worldListener = worldListener;
	}


	public void setEmergencyService(SOSListener emergencyService) {
		this.emergencyService = emergencyService;
	}





	@Override

	public void struckBy(Disaster d) {

		setState(CitizenState.IN_TROUBLE);
		this.disaster=d;
		emergencyService.receiveSOSCall(this);

	}


	@Override
	public void cycleStep() {


			if((bloodLoss>0&&bloodLoss<30)||(toxicity>0&&toxicity<30)){
				setHp(getHp()-5);
			}else if((bloodLoss>=30&&bloodLoss<70)||(toxicity>=30&&toxicity<70)){
				setHp(getHp()-10);
					
			}else if((bloodLoss>=70||(toxicity>=70))) {
				setHp(getHp()-15);
				
			}
	
	}
	public String toString() {
		return "Citizen's Location:"+getLocation().toString()+".\n"+
				"Citizen's Name:"+getName()+".\n"+
				"Citizen's Age :"+getAge()+".\n"+
				"Citizen's National ID :"+getNationalID()+".\n"+
				"Citizen's Hp :"+getHp()+".\n"+
				"Citizen's Bloodloss :"+getBloodLoss()+".\n"+
				"Citizen's Toxicity :"+getToxicity()+".\n"+
				"Citizen's State :"+getState()+".\n"+
				"Citizen's Disaster: "+(getDisaster()==null?"No Disaster.\n":getDisaster().getDisasterType()+"\n");
	}
	

	public String targetType() {
		return "citizen "+this.getName();
	}


	@Override
	public int compareTo(Citizen o) {
		// TODO Auto-generated method stub
		if(o.getDisaster() instanceof Injury)
			return o.getBloodLoss()-bloodLoss;
		else {
			return o.getToxicity()-toxicity;
		}
	}


}
