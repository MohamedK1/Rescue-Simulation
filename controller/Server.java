package controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Server extends JFrame implements Runnable {
	private JTextField userText;
	private JTextArea chat;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	private JFrame frame;
	public JFrame getFrame() {
		return frame;
	}

	public Server() {
		frame = new JFrame("a friend help");
		userText= new JTextField();
		userText.setEditable(false);
		userText.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!e.getActionCommand().equals(""))
					sendMessage(e.getActionCommand());
				userText.setText("");

			}
		});
		frame.add(userText,BorderLayout.SOUTH);
		chat = new JTextArea();
		chat.setEditable(false);
		frame.add(new JScrollPane(chat));
		frame.setSize(400,300); 
		frame.setVisible(false);
		
		frame.addWindowListener(new ChatWindowDestroyer(this));
		frame.validate();
		frame.repaint();
	}

	public ObjectOutputStream getOutput() {
		return output;
	}

	public void startRunning() {

		try {
			server =new ServerSocket(6789, 100);
			while(true) {
				try {


					waitForConnection();
					setupStreams();
					whileChatting();
				}catch(EOFException e) {
					showMessage("\n server ended connection");
				}finally {
					closeConnection(); 
				}
			}
		} catch (IOException e) {

		}

	}

	public void waitForConnection() throws IOException{
		showMessage("waiting for connection !\n");
		showMessage("your ip address is "+InetAddress.getLocalHost().getHostAddress()+"\n");
		connection =server.accept();
	
		showMessage("you are currently connected to "+connection.getInetAddress().getHostName()+" successfully");
		showMessage("\nto end the conversation write :END CONNECTION");
		
		frame.setVisible(true);


	}
	public void setupStreams() throws IOException{
		output= new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input= new ObjectInputStream(connection.getInputStream());

	}


	public void whileChatting() throws IOException{
		String message = "\nyou are now connected";
		showMessage(message);
		ableToType(true);
		do {
			try {
				message = (String)input.readObject();
				showMessage("\n"+message);

			}catch(ClassNotFoundException e) {
				showMessage("\n you didn't receive a string");
			}

		}while(!message.equals("-player 2:END CONNECTION"));
	}

	public void closeConnection() {
		showMessage("\n closing conections ... !\n");
		ableToType(false);
		try {
		if(output!=null)
			output.close();
		if(input!=null)
		input.close();
		if(connection!=null)	
		connection.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}


	public void sendMessage(String message) {
		try {
			output.writeObject("-player 1:"+message);
			output.flush();
			showMessage("\n-player 1:"+message);
		}catch(IOException e) {
			chat.append("\n ERROR: CAN'T SEND THAT MESSAGE");
		}
	}
	public void showMessage(String text) {
		// this is because updating GUI need to be in only one thread because asynchronous threads may results in unexpected GUI
		// so invokeLater takes runnable object (for threads) and EventDispatcher thread will handle the updates on thr GUI

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

	}

	
	@Override
	public void run() {
		
		this.startRunning();
	}
}
