package controller;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.stream.Stream;

import javax.swing.JFrame;


public class ChatWindowDestroyer extends WindowAdapter {
	JFrame frame;
	public ChatWindowDestroyer(JFrame frame) {
		super();
		this.frame=frame;
	}
	@Override 
	public void windowClosing(WindowEvent e) {
		if(frame instanceof Client) {
			Client c = (Client)frame;
			if(c.getOutput()!=null) {
				c.sendMessage("END CONNECTION");
				c.getFrame().dispose();
			}	 
		
		}else if (frame instanceof Server) {
			Server s = (Server)frame;
			if(s.getOutput()!=null) {
				s.sendMessage("END CONNECTION");
			s.getFrame().dispose();
			}
			
		
			
		}
		//Stream.of(Window.getWindows()).forEach(Window::dispose);
		
	} 


	}


