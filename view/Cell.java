package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sun.xml.internal.messaging.saaj.soap.JpegDataContentHandler;

import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.units.Unit;

public class Cell extends JButton{
	private  ArrayList<ResidentialBuilding> building;
	private ArrayList<Citizen> citizensInCell;
	private ArrayList<Unit> unitsInCell; 
	private boolean cellClicked=false;
	
	public boolean isCellClicked() {
		return cellClicked;
	}
	public void setCellClicked(boolean cellClicked) {
		this.cellClicked = cellClicked;
	}
	public ArrayList<ResidentialBuilding> getBuilding() {
		return building;
	}
	public void setBuilding(ArrayList<ResidentialBuilding> building) {
		this.building = building;
	}
	public ArrayList<Citizen> getCitizensInCell() {
		return citizensInCell;
	}
	public void setCitizensInCell(ArrayList<Citizen> citizensInCell) {
		this.citizensInCell = citizensInCell;
	}
	public ArrayList<Unit> getUnitsInCell() {
		return unitsInCell;
	}
	public void setUnitsInCell(ArrayList<Unit> unitsInCell) {
		this.unitsInCell = unitsInCell;
	}
	public Cell () {
		building=new ArrayList<>();
		citizensInCell=new ArrayList<>();
		unitsInCell=new ArrayList<>();




	}
}
