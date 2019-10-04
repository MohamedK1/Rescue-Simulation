package controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Client extends JFrame implements Runnable {

	private JTextField userText;
	private JTextArea chat;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String serverIP ;
	private String message="";
	private Socket connection;
	private JFrame frame;
	public Client(String host) {
		frame = new JFrame("Calling for Help");
		serverIP=host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sendMessage(arg0.getActionCommand());
				userText.setText("");

			}
		});
		frame.add(userText,BorderLayout.SOUTH);
		chat= new JTextArea();
		chat.setEditable(false);
		frame.add(new JScrollPane(chat));
		frame.setSize(400,300); 
		frame.setVisible(true);
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new ChatWindowDestroyer(this));
	frame.validate();
	frame.repaint();
	}

	public JFrame getFrame() {
		return frame;
	}

	public ObjectOutputStream getOutput() {
		return output;
	}

	public void startRunning() {
		try {
			connectToServer();
			setupStreams();
			whileChatting();

		}catch(EOFException e) {
			showMessage("\nclient terminated connection");
		}catch(IOException e) {
		//e.printStackTrace();
			showMessage("\nserver not found");
		}finally {
			closeConnection();	
		}
	}
	public void connectToServer() throws IOException{
		showMessage("trying to connect to "+ serverIP +"... !");
		connection= new Socket(InetAddress.getByName(serverIP),6789);

	}
	public void setupStreams() throws IOException{
		output= new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input= new ObjectInputStream(connection.getInputStream());
		

	}

	public void whileChatting() throws IOException {
		ableToType(true);
		showMessage("\nyou are now connected");
		showMessage("\nto end the conversation write :END CONNECTION");
		
		String firstMessage="- units information :\n"+CommandCenter.getCommandCenter().getUnitInfo();
		firstMessage+="log info :\n"+CommandCenter.getView().getDownPanel().getTextArea().getText();
		sendMessage(firstMessage);
		do {
			try {
				message = (String)input.readObject();
				showMessage("\n"+ message);
			}catch(ClassNotFoundException e) {
				showMessage("\n you didn't receive a string");
			}

		}while(!message.equals("-player 1:END CONNECTION"));
	}
	public void closeConnection() {
		showMessage("\n closing conections ... !");
		ableToType(false);
		try {
			
			if(output!=null)
			output.close();
			if(input!=null)
			input.close();
			if(connection!=null)
			connection.close();
		}catch(IOException e){

		}
	}
	
	public void sendMessage(String message) {
		try {
			output.writeObject("-player 2:"+message);
			output.flush();
			showMessage("\n-player 2:"+message);
		}catch(IOException e) {
			chat.append("\n ERROR: CAN'T SEND THAT MESSAGE");
		}
	}
	
	public void showMessage(String text) {
		// this is because updating GUI need to be in only one thread because asynchronous threads may results in unexpected GUI
		// so invokeLater takes runnable object (for threads) and EventDispatcher thread will handle the updates on thr GUI
//
		SwingUtilities.invokeLater(
				new Runnable() {
					@Override
					public void run() {
						chat.append(text);
					}
				});
//		chat.append(text);
//		chat.revalidate();
//		chat.repaint();
	}

	public void ableToType(boolean editability) {
		SwingUtilities.invokeLater(
				new Runnable() {
					@Override
					public void run() {
						userText.setEditable(editability);	
					}
				});

//				userText.setEditable(editability);	
//				userText.revalidate();
//				userText.repaint();
//		

	}

@Override
public void run() {
	this.startRunning();
}
}
