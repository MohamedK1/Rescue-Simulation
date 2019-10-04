package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.CommandCenter;

public class GameOver extends JPanel {
	Image img ; 
	JFrame frame;
	JButton okButton;
	JPanel downPanel;
	public GameOver(JFrame frame) throws IOException {
		img=ImageIO.read(Grid.class.getResource("gameOver.png"));
		this.frame=frame;
		JLabel casualities = new JLabel();
		String text = "Number of casualties is: "+CommandCenter.getCommandCenter().getEngine().calculateCasualties();
		Font font = new Font("Cooper Black",Font.BOLD,40);
		
		casualities.setText(text);
		casualities.setFont(font);
		casualities.setForeground(Color.green);
		this.okButton = new JButton("End Game");
		//JPanel emptyPanel = new JPanel();
		//emptyPanel.setOpaque(false);
		this.setSize(new Dimension(frame.getContentPane().getWidth(),frame.getContentPane().getHeight()));
		//emptyPanel.setPreferredSize(new Dimension(500,500));
		this.downPanel = new JPanel();
		downPanel.setLayout(new FlowLayout());
		downPanel.add(okButton);
		downPanel.setPreferredSize(new Dimension(100,100));
		//downPanel.setOpaque(false);
		downPanel.setBackground(Color.black);
		downPanel.revalidate();
		downPanel.repaint();
		this.add(casualities,BorderLayout.NORTH);
		
	//	this.add(okButton,BorderLayout.SOUTH);
		
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			
			}
		});
		
		casualities.revalidate();
		casualities.repaint();
		this.revalidate();
		this.repaint();
	}
	@Override 
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0,(int)frame.getContentPane().getWidth(),(int)frame.getContentPane().getHeight(),null);
	}
public JPanel getDownPanel() {
	return downPanel;
}
}
