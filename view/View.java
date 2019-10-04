package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import controller.CommandCenter;
import recommendation.recommend;

public class View  {
	private  JFrame frame;
	private  Grid grid;
	private  LeftPanel leftPanel;
	private  UnitPanel unitPanel;
	private  DownPanel downPanel;
	private CommandCenter commandCenter;
	public View (CommandCenter commandCenter) throws IOException {
		frame = new JFrame();
	 	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		leftPanel = new LeftPanel();
		this.commandCenter=commandCenter;
		 grid = new Grid(leftPanel);
		 unitPanel = new UnitPanel(leftPanel.getInfoPanel(),leftPanel);
		 unitPanel.setPreferredSize(new Dimension(300,50));
		 frame.add(grid,BorderLayout.CENTER);
		 leftPanel.setPreferredSize(new Dimension(300,50));
		 frame.add(leftPanel,BorderLayout.WEST);
		// ---------- the below is commented to make the recently added work ------
//		 JScrollPane unitScroll = new JScrollPane(unitPanel,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//		 unitScroll.setPreferredSize(new Dimension(200,50));
//			
//		 frame.add(unitScroll,BorderLayout.EAST);
	//-------------------------------------------------------------------
		 frame.add(unitPanel,BorderLayout.EAST);
		 downPanel = new DownPanel(commandCenter);
		  downPanel.setPreferredSize(new Dimension(frame.getWidth(),200));
		  frame.add(downPanel,BorderLayout.SOUTH);
		  
		  frame.setVisible(true);
		  frame.validate();
			frame.repaint();

  

	}
public JFrame getFrame() {
		return frame;
	}
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
	public Grid getGrid() {
		return grid;
	}
	public void setGrid(Grid grid) {
		this.grid = grid;
	}
	public LeftPanel getLeftPanel() {
		return leftPanel;
	}
	public void setLeftPanel(LeftPanel leftPanel) {
		this.leftPanel = leftPanel;
	}
	public UnitPanel getUnitPanel() {
		return unitPanel;
	}
	public void setUnitPanel(UnitPanel unitPanel) {
		this.unitPanel = unitPanel;
	}
	public DownPanel getDownPanel() {
		return downPanel;
	}
	public void setDownPanel(DownPanel downPanel) {
		this.downPanel = downPanel;
	}

}
