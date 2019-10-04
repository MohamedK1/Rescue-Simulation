package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.ScrollPaneConstants;

import controller.CommandCenter;
import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.units.Ambulance;
import model.units.DiseaseControlUnit;
import model.units.Unit;
import simulation.Address;


public class Grid extends JPanel {
	private static Cell[][] cells ;

	static	private InfoPanel infoPanel;
	static private LeftPanel leftPanel;
	static private PopUpCell popUpCell ;
	static private ArrayList<Image> buildingsImage;
	static private ArrayList<Image> buildingsUnitsImage;
	
	static private ArrayList<Image> citizensImage;
	private Image ripImage;
	private Image destroyBuilding;
	private Image destroyBuildingUnit;
	private static Image ambulanceImg;
	private static Image diseaseControlUnitImg;
	private	static Image evacuatorImg;
	private static Image fireTruckImg;
	private static Image gasControlUnitImg;
	private static boolean gridClicked=false;
	
	private Image  groupImage;
	private Image groupUnitImage;
	private Image commandCentreUnitImg;
	public Image getGroupUnitImage() {
		return groupUnitImage;
	}
	public void setGroupUnitImage(Image groupUnitImage) {
		this.groupUnitImage = groupUnitImage;
	}
	public Image getGroupImage() {
		return groupImage;
	}
	public void setGroupImage(Image groupImage) {
		this.groupImage = groupImage;
	}
	public static ArrayList<Image> getBuildingsUnitsImage() {
		return buildingsUnitsImage;
	}
	public static void setBuildingsUnitsImage(ArrayList<Image> buildingsUnitsImage) {
		Grid.buildingsUnitsImage = buildingsUnitsImage;
	}
	public static ArrayList<Image> getBuildingsImage() {
		return buildingsImage;
	}
	public static void setBuildingsImage(ArrayList<Image> buildingsImage) {
		Grid.buildingsImage = buildingsImage;
	}
	public static Image getAmbulanceImg() {
		return ambulanceImg;
	}
	public static Image getDiseaseControlUnitImg() {
		return diseaseControlUnitImg;
	}
	public static Image getEvacuatorImg() {
		return evacuatorImg;
	}
	public static Image getFireTruckImg() {
		return fireTruckImg;
	}
	public static Image getGasControlUnitImg() {
		return gasControlUnitImg;
	}
	public static boolean isGridClicked() {
		return gridClicked;
	}
	public static void setGridClicked(boolean gridClicked) {
		Grid.gridClicked = gridClicked;
	}
	public Image getDestroyBuilding() {
		return destroyBuilding;
	}
	public Image getRipImage() {
		return ripImage;
	}
	public LeftPanel getLeftPanel() {
		return leftPanel;
	}
	public void setLeftPanel(LeftPanel leftPanel) {
		Grid.leftPanel = leftPanel;
	}


	



public Grid (LeftPanel leftPanel) throws IOException {
		commandCentreUnitImg =ImageIO.read(Grid.class.getResource("commandCentreImgUnit.png")); 
		 groupImage= ImageIO.read(Grid.class.getResource("group.png"));
		 groupUnitImage =ImageIO.read(Grid.class.getResource("groupUnit.png"));
		ripImage= ImageIO.read(Grid.class.getResource("rip.png"));
		destroyBuilding= ImageIO.read(Grid.class.getResource("building.png"));
		destroyBuildingUnit= ImageIO.read(Grid.class.getResource("buildingUnit.png"));
		Grid.leftPanel = leftPanel;
		infoPanel=leftPanel.getInfoPanel();
		this.setLayout(new GridLayout(10, 10,5,5));
		this.setBackground(Color.GRAY);
		cells = new Cell[10][10];

		InfoListenerGrid infoListenerGrid = new InfoListenerGrid();
		for(int i=0;i<10;i++) {
			for(int j=0;j<10;j++) {
				Cell oneCell =new Cell();

				oneCell.addActionListener(infoListenerGrid);
				oneCell.setBackground(Color.white);
				cells[i][j]= oneCell;

				this.add(oneCell);

			}
		}
		
	}

	
	public Image getCommandCentreUnitImg() {
	return commandCentreUnitImg;
}
public void setCommandCentreUnitImg(Image commandCentreUnitImg) {
	this.commandCentreUnitImg = commandCentreUnitImg;
}
	public static void setAmbulanceImg(Image ambulanceImg) {
		Grid.ambulanceImg = ambulanceImg;
	}
	public static void setDiseaseControlUnitImg(Image diseaseControlUnitImg) {
		Grid.diseaseControlUnitImg = diseaseControlUnitImg;
	}
	public static void setEvacuatorImg(Image evacuatorImg) {
		Grid.evacuatorImg = evacuatorImg;
	}
	public static void setFireTruckImg(Image fireTruckImg) {
		Grid.fireTruckImg = fireTruckImg;
	}
	public static void setGasControlUnitImg(Image gasControlUnitImg) {
		Grid.gasControlUnitImg = gasControlUnitImg;
	}
	public Cell[][] getCells() {
		return cells;
	}
	
	


public void addBuilding(ResidentialBuilding building ,int x , int y) {

		Cell cell =cells[x][y];
		Image img = building.getBuildingImage();
		ImageIcon imgIcon = null;
		if(building!=null) {
			cell.getBuilding().add(building);
		}
		if(cell.getUnitsInCell().size()==0) {
			imgIcon=new ImageIcon(img.getScaledInstance(70,40,java.awt.Image.SCALE_SMOOTH));
		}else {
			imgIcon=new ImageIcon(building.getBuildingUnitImage().getScaledInstance(70,40,java.awt.Image.SCALE_SMOOTH));
		}
		
		
		cell.setIcon(imgIcon);
		cell.getCitizensInCell().clear();
		revalidate();
		repaint();



	}
	
public void destroyBuilding(ResidentialBuilding building, int x , int y) {
		Cell cell = cells[x][y];
		ImageIcon imgIcon =null;
		if(cell.getUnitsInCell().size()==0) {
		 imgIcon = new ImageIcon(destroyBuilding.getScaledInstance(70,40,java.awt.Image.SCALE_SMOOTH));
		}else {
			imgIcon = new ImageIcon(destroyBuildingUnit.getScaledInstance(70,40,java.awt.Image.SCALE_SMOOTH));
		}
		cell.setIcon(null);
		cell.setIcon(imgIcon);
		revalidate();
		repaint();

	}

	

public void addCitizen(Citizen citizen ,int x , int y)  {
		Cell cell =cells[x][y];
		if(cell.getCitizensInCell().size()==0) {
			Image img = citizen.getImg();
			ImageIcon imgIcon = new ImageIcon(img.getScaledInstance(100,40,java.awt.Image.SCALE_SMOOTH));
			if(citizen!=null) {
				cell.getCitizensInCell().add(citizen);
			}
			cell.setIcon(imgIcon);
		}else {
			if(cell.getUnitsInCell().size()==0) {
			cell.setIcon(null);
			ImageIcon imgIcon = new ImageIcon(groupImage.getScaledInstance(70,40,java.awt.Image.SCALE_SMOOTH));
			cell.setIcon(imgIcon);
		
			}
			
			if(citizen!=null) {
				cell.getCitizensInCell().add(citizen);
			}
		

		}
		revalidate();
		repaint();
	}

public void killCitizen (Citizen citizen, int x , int y) throws IOException {
		Cell cell = cells[x][y];
		ripImage = this.getRipImage();
		ImageIcon imgIcon = new ImageIcon(ripImage.getScaledInstance(70,40,java.awt.Image.SCALE_SMOOTH));
		citizen.setImg(ripImage);
		if(cell.getCitizensInCell().size()==1) {
			cell.setIcon(null);
			cell.setIcon(imgIcon);

		}
		leftPanel.getCitizensPanel().revalidate();
		leftPanel.getCitizensPanel().repaint();
		revalidate();
		repaint();

	}


	

public void addUnit(Unit unit ,int x , int y) {
		Cell cell =cells[x][y];
		if(!cell.getUnitsInCell().contains(unit)) {
				
			ImageIcon imgIcon=null;
			if(unit!=null) {
			cell.getUnitsInCell().add(unit);
			}
			if(x!=0&&y!=0) {
			if(cell.getBuilding().size()>0) {
				imgIcon = new ImageIcon(cell.getBuilding().get(0).getBuildingUnitImage().getScaledInstance(70,40,java.awt.Image.SCALE_SMOOTH));
			}else if(cell.getCitizensInCell().size()>0) {
				imgIcon = new ImageIcon(groupUnitImage.getScaledInstance(70,40,java.awt.Image.SCALE_SMOOTH));
			}
			cell.setIcon(null);
			cell.setIcon(imgIcon);

		}
		revalidate();
		repaint();
	}
}
public void removeUnit(Unit unit , int x , int y) {
		Cell cell = cells[x][y];
	if(cell.getUnitsInCell().contains(unit)) {
		cell.getUnitsInCell().remove(unit);
		if(x!=0&&y!=0) {
			ImageIcon imgIcon=null;
			if((cell.getUnitsInCell().size()==0)) {
				if(cell.getBuilding().size()>0) {
					imgIcon = new ImageIcon(cell.getBuilding().get(0).getBuildingImage().getScaledInstance(70,40,java.awt.Image.SCALE_SMOOTH));
				
				}else if(cell.getCitizensInCell().size()>1) {
					imgIcon=new ImageIcon(groupImage.getScaledInstance(70,40,java.awt.Image.SCALE_SMOOTH));
				}else if(cell.getCitizensInCell().size()==1){
					imgIcon=new ImageIcon(cell.getCitizensInCell().get(0).getImg().getScaledInstance(70,40,java.awt.Image.SCALE_SMOOTH));
							
				}
				cell.setIcon(null);
				cell.setIcon(imgIcon);
			}
		}
		revalidate();
		repaint();

}	
	}

	public static void makeGridClickable() {
		Grid.setGridClicked(false);
		for(int i=0;i<10;i++) {
			for(int j=0;j<10;j++ ) {
				Cell c = cells[i][j];
				c.setEnabled(true);
				c.setCellClicked(false);
				c.setBackground(Color.WHITE);
			}
		}
	}
	
public static void makeGridUnclickable(Cell c) {

		for(int i=0;i<10;i++) {
			for(int j=0;j<10;j++) {
				Cell cell =cells[i][j];
				if(cell!=c) {
					cell.setEnabled(false);
					cell.setCellClicked(false);
					cell.setBackground(Color.RED);
				}else {
					cell.setCellClicked(true);
					cell.setEnabled(true);
					cell.setBackground(Color.GREEN);
					Grid.setGridClicked(true);
				}


			}
		}
	}



private class InfoListenerGrid implements ActionListener{

	




		@Override
		
public void actionPerformed(ActionEvent arg0) {

			if (arg0.getSource() instanceof Cell) {

				Cell cell = (Cell)arg0.getSource();
				CitizensPanel p = getLeftPanel().getCitizensPanel();
				TextArea textArea = infoPanel.getTextArea();
				
				if(DownPanel.isRespondClicked()&&UnitPanel.isUnitPanelClicked()) {
					if(!Grid.isGridClicked()) {
						Grid.makeGridUnclickable(cell);
						if(cell.getBuilding().size()>0) {
							PopUpRespond popUpRespond = new PopUpRespond(cell);
							popUpRespond.getPopWindow().setVisible(true);
						}else if(cell.getCitizensInCell().size()>0) {
							p.removeAll();
								getLeftPanel().getCitizensPanel().addCitizens(cell.getCitizensInCell());
							p.revalidate();
							
						}else {
							JOptionPane.showMessageDialog(null, "This cell is empty, please select another target !","Empty cell",JOptionPane.INFORMATION_MESSAGE);
							Grid.makeGridClickable();
							Grid.setGridClicked(false);
						}









					}else {
						Grid.makeGridClickable();
						p.removeAll();
						textArea.setText("");
						p.revalidate();
						p.repaint();
						
					}





				}else {

					if(cell.getBuilding().size()==1&&cell.getUnitsInCell().size()==0&&cell.getCitizensInCell().size()==0) { //case of one building only
						textArea.setText(cell.getBuilding().get(0).toString());
						p.removeAll();
						p.addCitizens(cell.getBuilding().get(0).getOccupants());

					}else if(cell.getBuilding().size()==0&&cell.getCitizensInCell().size()>0&&cell.getUnitsInCell().size()==0) {//case of only citizens
						textArea.setText(cell.getCitizensInCell().get(0).toString());
						p.removeAll();
						p.addCitizens(cell.getCitizensInCell());


					}else if(cell.getBuilding().size()==0&&cell.getUnitsInCell().size()==0&&cell.getCitizensInCell().size()==0) {
						p.removeAll();
						textArea.setText("Empty cell");

					}else {
						p.removeAll();
						if(cell.getBuilding().size()!=0) {
							textArea.setText(cell.getBuilding().get(0).toString());
							p.addCitizens(cell.getBuilding().get(0).getOccupants());

						}
						//					p.removeAll();

						PopUpCell popUp = new PopUpCell(cell);
						popUp.getPopWindow().setVisible(true);

					}
					p.revalidate();
					p.repaint();

					//--------------------------------------




				}

			}
		}
	}







	private  class PopUpCell  extends JFrame {
		private JFrame popWindow;
		private JButton unitButton;
		private JButton citizenButton;
		private Cell currCell;
		private JButton neither;
		public Cell getCurrCell() {
			return currCell;
		}




		public PopUpCell(Cell curCell) {
			this.currCell = curCell;
			popWindow = new JFrame("Content selection");
			popWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			JLabel message  = new JLabel("please choose from the options below !");
			popWindow.setMinimumSize(new Dimension(500,100));
			popWindow.setMaximumSize(new Dimension(500,100));

			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

			popWindow.setLocation((int)(screenSize.getWidth()/2)-popWindow.getWidth()/2,(int)(screenSize.getHeight()/2)-popWindow.getHeight()/2);
			citizenButton = new JButton("Citizens");
			unitButton = new JButton("Units");
			neither = new JButton("neither");
			citizenButton.setPreferredSize(new Dimension(100,30));
			unitButton.setPreferredSize(new Dimension(100,30));
			neither.setPreferredSize(new Dimension(100,30));

			popWindow.setLayout(new FlowLayout());
			popWindow.add(citizenButton);
			popWindow.add(unitButton);
			popWindow.add(neither);
			neither.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					popWindow.dispose();

				}
			});

			

citizenButton.addActionListener(new ActionListener() {

				@Override

				
public void actionPerformed(ActionEvent arg0) {
					TextArea textArea = infoPanel.getTextArea();
					CitizensPanel p = getLeftPanel().getCitizensPanel();

					if(currCell.getCitizensInCell().size()>0) {
						textArea.setText(currCell.getCitizensInCell().get(0).toString());

						p.removeAll();

						getLeftPanel().getCitizensPanel().addCitizens(currCell.getCitizensInCell());
					}else {
						textArea.setText("there are no citizens in this cell !");
						p.removeAll();
					}
					p.revalidate();
					p.repaint();
					popWindow.setVisible(false);
					popWindow.dispose();	

				}
			});
			
unitButton.addActionListener(new ActionListener() {

				@Override

				
public void actionPerformed(ActionEvent arg0) {
					TextArea textArea = infoPanel.getTextArea();
					CitizensPanel p = getLeftPanel().getCitizensPanel();

					if(currCell.getUnitsInCell().size()>0) {
						textArea.setText(currCell.getUnitsInCell().get(0).toString());

						p.removeAll();

						getLeftPanel().getCitizensPanel().addUnits(currCell.getUnitsInCell());


					}else {
						textArea.setText("there are no Units in this cell !");
						p.removeAll();

					}
					p.revalidate();
					popWindow.setVisible(false);
					popWindow.dispose();	

				}
			});

			popWindow.revalidate();
		}



		public JFrame getPopWindow() {
			return popWindow;
		}




	}


	
private  class PopUpRespond  extends JFrame {
		private JFrame popWindow;
		private JButton buildingButton;
		private JButton citizenButton;
		private  final Cell currCell;
		private JButton neither;
		public Cell getCurrCell() {
			return currCell;
		}





		

		
public PopUpRespond(Cell curCell) {
			this.currCell = curCell;
			popWindow = new JFrame("Target selection");
			popWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			popWindow.setMinimumSize(new Dimension(500,100));
			popWindow.setMaximumSize(new Dimension(500,100));

			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

			popWindow.setLocation((int)(screenSize.getWidth()/2)-popWindow.getWidth()/2,(int)(screenSize.getHeight()/2)-popWindow.getHeight()/2);
			citizenButton = new JButton("Citizens");
			buildingButton = new JButton("Building");
			neither = new JButton("neither");
			citizenButton.setPreferredSize(new Dimension(100,30));
			buildingButton.setPreferredSize(new Dimension(100,30));
			neither.setPreferredSize(new Dimension(100,30));

			popWindow.setLayout(new FlowLayout());
			popWindow.add(citizenButton);
			popWindow.add(buildingButton);
			popWindow.add(neither);
			
neither.addActionListener(new ActionListener() {

				@Override

				public void actionPerformed(ActionEvent arg0) {
					CommandCenter.getView().getLeftPanel().getInfoPanel().getTextArea().setText("");
					DownPanel.getRespondButton().setBackground(null);
					DownPanel.setRespondClicked(false);
					UnitPanel.setUnitPanelClicked(false,null);
					Grid.setGridClicked(false);
					UnitPanel.makeUnitPanelClickable();
					Grid.makeGridClickable();
					popWindow.dispose();

				}
			});

			
			

citizenButton.addActionListener(new ActionListener() {

				@Override

				


public void actionPerformed(ActionEvent arg0) {
					TextArea textArea = infoPanel.getTextArea();
					CitizensPanel p = getLeftPanel().getCitizensPanel();

					if(currCell.getCitizensInCell().size()>0||currCell.getBuilding().get(0).getOccupants().size()>0) {
						ArrayList<Citizen> allCitizens=null;
						if(currCell.getBuilding().get(0).getOccupants().size()>0) {
							allCitizens=currCell.getBuilding().get(0).getOccupants();
						}else {
							allCitizens=currCell.getCitizensInCell();
						}
						textArea.setText(allCitizens.get(0).toString());
						getLeftPanel().getCitizensPanel().addCitizens(allCitizens);


					}else {
						JOptionPane.showMessageDialog(null,"There are no citizens in this cell to select from, please select another target!","No citizens",JOptionPane.INFORMATION_MESSAGE);
						Grid.makeGridClickable();
						p.removeAll();
						
					}
					p.revalidate();
					popWindow.setVisible(false);
					popWindow.dispose();	

				}
			});

			buildingButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					
					TextArea textArea = infoPanel.getTextArea();
					CitizensPanel p = getLeftPanel().getCitizensPanel();
					p.removeAll();
					if(currCell.getBuilding().size()!=0) {
						textArea.setText(currCell.getBuilding().get(0).toString());
						p.addCitizens(currCell.getBuilding().get(0).getOccupants());

					}


					// responding	

					try {
						UnitPanel.getUnitSelected().respond(curCell.getBuilding().get(0));
						CommandCenter.getView().getUnitPanel().transferToResponding(UnitPanel.getUnitSelected());
					} catch (IncompatibleTargetException|CannotTreatException e) {
						JOptionPane.showMessageDialog(null,e.getMessage(), "Error",JOptionPane.ERROR_MESSAGE);

					}
					
					DownPanel.getRespondButton().setBackground(null);
					DownPanel.setRespondClicked(false);
					UnitPanel.setUnitPanelClicked(false,null);
					Grid.setGridClicked(false);
					UnitPanel.makeUnitPanelClickable();
					Grid.makeGridClickable();
					textArea.setText("");
					p.removeAll();
					p.revalidate();
					p.repaint();
				
					popWindow.setVisible(false);
					popWindow.dispose();	

				}
			});

			popWindow.revalidate();
		}



		public JFrame getPopWindow() {
			return popWindow;
		}




	}



	
//public static void main(String[] args) throws IOException {
//		JFrame frame = new JFrame();
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//
//
//
//		// ******************** part to test the final GUI *****************		
//		leftPanel = new LeftPanel();
//
//
//		// creating arraylist of Cititzens 
//		ResidentialBuilding building = new ResidentialBuilding(new Address(3, 2));
//
//		ArrayList<Citizen> citizensList = new ArrayList();
//		for(int i=0;i<100;i++) {
//			Citizen citizen = new Citizen(new Address(3,4), "0"+i, "citizen"+i , i, null);
//			citizen.setImg(ImageIO.read(Grid.class.getResource("citizen0.png")));
//			citizensList.add(citizen);
//			building.getOccupants().add(citizen);
//		}
//
//		//leftPanel.setPreferredSize(new Dimension(200,50));
//		Grid g = new Grid(leftPanel);
//		//-------to add buildings in random places
//		for(int i=0;i<3;i++) {
//			int x = (int)(Math.random()*9);
//
//			int y = (int)(Math.random()*9);
//			g.addBuilding(building,x, y);
//
//			Citizen c = new Citizen(new Address(x+1, y+1),"qd111", "tatata", (x+y), null);
//			c.setImg(ImageIO.read(Grid.class.getResource("citizen0.png")));
//
//			g.addCitizen(c, x+1, y+1);
//			g.addCitizen(c, x+1, y+1);
//
//			if(i==2) {
//				//g.addCitizen(c, x+1, y+1);
//				g.killCitizen(c, x+1, y+1 );
//			}
//			if(i==2) {
//				ResidentialBuilding b = new ResidentialBuilding(new Address(0,0));
//
//				g.addBuilding(b, 0, 0);
//
//			}
//
//		}
//		ArrayList<Unit> emergencyUnits = new ArrayList<>();
//		emergencyUnits.add(new Ambulance("dada", new Address(3, 2), 3, null));
//		emergencyUnits.add(new DiseaseControlUnit("tata", new Address(3, 3), 5, null));
//		for(int i=0;i<50;i++) {
//			emergencyUnits.add(new DiseaseControlUnit("tata", new Address(3, 3), 5, null));
//
//		}
//
//		UnitPanel panel = new UnitPanel(leftPanel.getInfoPanel(),leftPanel);
//		panel.addUnits(emergencyUnits);
//		panel.setSize(new Dimension(200,50));
//
//		frame.add(g,BorderLayout.CENTER);
//
//		leftPanel.setPreferredSize(new Dimension(200,50));
//		frame.add(leftPanel,BorderLayout.WEST);
//
//		//		TextArea texAreaTest = new TextArea();
//		//		texAreaTest.setPreferredSize(new Dimension(frame.getWidth(),50));
//		//		frame.add(texAreaTest,BorderLayout.SOUTH);
//		//JScrollPane unitScroll = new JScrollPane(panel,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//		frame.add(panel,BorderLayout.EAST);
//		panel.validate();
//		panel.repaint();
//
//		//		JScrollPane leftScroll = new JScrollPane(leftPanel,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//		//
//		//		frame.add(leftScroll,BorderLayout.WEST);
//
//
//
//		DownPanel downPanel = new DownPanel();
//		downPanel.setPreferredSize(new Dimension(frame.getWidth(),200));
//		//downPanel.getActionsPanel().setPreferredSize(new Dimension(frame.getWidth(),50));
//		//frame.add(downPanel.getLogPanel(),BorderLayout.SOUTH);
//		//frame.add(downPanel.getActionsPanel(),BorderLayout.NORTH);
//		frame.add(downPanel,BorderLayout.SOUTH);
//
//		frame.setVisible(true);
//		//frame.pack();
//		frame.validate();
//		frame.repaint();
//
//	}

	public static ArrayList<Image> getCitizensImage() {
		return citizensImage;
	}
	


}
