package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import controller.Client;
import controller.CommandCenter;
import recommendation.recommend;

public class DownPanel extends JPanel {
	private JPanel logPanel ;
	private JPanel actionsPanel;
	private JButton nextCyleButton ;
	private static JButton respondButton ;
	private JButton ExitButton;
	private JButton recommendButton;
	private TextArea textArea;
	private static Boolean respondClicked=false;
	static private JLabel currCycle ;
	static private JLabel casualities;
	private static recommend recommendation;
	//------------ networking---------
		private static JButton callFriendButton;

	public JButton getCallFriendButton() {
		return callFriendButton;
	}
		//------------end networking---------

	
	public static JButton getRespondButton() {
		return respondButton;
	}
	public TextArea getTextArea() {
		return textArea;
	}
	public JButton getNextCyleButton() {
		return nextCyleButton;
	}
	public JPanel getLogPanel() {
		return logPanel;
	}
	public JPanel getActionsPanel() {
		return actionsPanel;
	}
	

	public static Boolean isRespondClicked() {
		return respondClicked;
	}
	public static void setRespondClicked(Boolean respondClicked) {
		DownPanel.respondClicked = respondClicked;
	}
	
	public DownPanel(CommandCenter commandCenter) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	 int ScreenWidth =(int)screenSize.getWidth();
	 int ScreenHeight =(int)screenSize.getHeight();
		ExitButton = new JButton("Exit");
		recommendButton= new JButton("Recommend");
		recommendation =new recommend(commandCenter);
	 	ExitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
		});
	 	recommendButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				recommendation.setEvacuator();
				recommendation.setCollapseBuildings();
				recommendation.setFireBuildings();
				recommendation.setFireTruck();
				recommendation.setGasControl();
				recommendation.setGasleakBuildings();
				recommendation.setAmbulance();
				recommendation.setinjuriedCitizen();
				recommendation.setDiseaseControl();
				recommendation.setinfectedCitizen();
				String msg="";
				
				msg+="Evacuators-----------\n";
				msg+=recommend.recommendEvacuator();
				msg+="FireTruck-------------\n";
				msg+=recommend.recommendFireTruck();
				msg+="GasControlUnit------\n";
				msg+=recommend.recommendGasControl();
				msg+="Ambulance----------\n";
				msg+=recommend.recommendAmbulance();
				msg+="DiseaseControlUnit---\n";
				msg+=recommend.recommendDiseaseControl();
				JOptionPane.showMessageDialog(null, msg,"Recommendations",JOptionPane.INFORMATION_MESSAGE);

				
				
			}
		});
	 	actionsPanel = new JPanel();
        actionsPanel.setLayout(new FlowLayout());
      
        nextCyleButton= new JButton("Next Cycle");
        respondButton = new JButton("Respond");
        
        
        currCycle = new JLabel();
        currCycle.setText("   Current cycle : 0");       // 3 spaces
        casualities = new JLabel() ;
        casualities.setText("   casualities :0");        // 3 spaces
        actionsPanel.add(nextCyleButton);
        actionsPanel.add(respondButton);
        actionsPanel.add(currCycle);
        actionsPanel.add(casualities);
        actionsPanel.add(recommendButton);

		//------------ networking---------
		callFriendButton = new JButton("call a friend");
		callFriendButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Client c ;
				String IPAdress = JOptionPane.showInputDialog("please enter server IP address");
				c =new Client(IPAdress);
				Thread clientThread = new Thread(c);
				clientThread.start();
				
			}
		});
		
		actionsPanel.add(callFriendButton);
		
		//------------end networking---------

        actionsPanel.add(ExitButton);
        
        actionsPanel.setPreferredSize(new Dimension(ScreenWidth,50));
        
        // part of log Panel
		logPanel = new JPanel();
		logPanel.setPreferredSize(new Dimension(ScreenWidth-100,150));

this.textArea = new TextArea();

        
        textArea.setPreferredSize(new Dimension(ScreenWidth-120,120));
    	textArea.setEditable(false);
    	textArea.setBackground(java.awt.Color.WHITE);
    	textArea.setForeground(Color.RED);
    	
    	logPanel.add(textArea,BorderLayout.NORTH);
		String LogTitle= "Log panel";
		Border border = BorderFactory.createTitledBorder(LogTitle);
		logPanel.setBorder(border);
        
		this.add(logPanel,BorderLayout.SOUTH);
		this.add(actionsPanel,BorderLayout.CENTER);
	actionsPanel.revalidate();
	actionsPanel.repaint();


	}
	public static JLabel getCurrCycle() {
		return currCycle;
	}
	public static void setCurrCycle(JLabel currCycle) {
		DownPanel.currCycle = currCycle;
	}
	public static JLabel getCasualities() {
		return casualities;
	}
	public static void setCasualities(JLabel casualities) {
		DownPanel.casualities = casualities;
	}


	
	
}
