package view;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import model.units.Unit;

public class UnitButton extends JButton {
	private Unit unit;
	private boolean unitClicked=false;
	
	public boolean isUnitClicked() {
		return unitClicked;
	}

	public void setUnitClicked(boolean unitClicked) {
		this.unitClicked = unitClicked;
	}

	public UnitButton(Unit unit) {
		super(unit.getUnitType());
		this.unit =unit;
		ImageIcon imgIcon = new ImageIcon(unit.getUnitImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
		this.setIcon(imgIcon);
	}

	public Unit getUnit() {
	return unit;
}
public void setUnit(Unit unit) {
	this.unit = unit;
}


}
