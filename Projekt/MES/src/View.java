import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Container.InternMessage;
import Container.InternMessage.WhatMsg;
import Model.Server;

import javax.swing.JLabel;
import javax.swing.JTextArea;

enum ShowView {
	INQUIRED, DEBUGINFOS, CONNECTION, DEVICE
}

public class View implements Observer {

	private JFrame frame;
	private JTextArea textField;
	// private ServerState state;
	private JButton inquiryButton, clearButton, waitButton, viewDebugButton, viewInquiredButton, viewConnectionsButton;
	private JButton[] newButtons;
	private ShowView showView;
	private String viewString;
	private int conId;

	public View() {
		// state.addObserver(this);
		initialize();
		frame.setVisible(true);
		showView = ShowView.DEBUGINFOS;
		viewString = "";
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @param textField_1
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(200, 200, 900, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblFunctions = new JLabel("Functions");
		lblFunctions.setBounds(394, 37, 70, 15);
		frame.getContentPane().add(lblFunctions);

		JLabel lblView = new JLabel("View");
		lblView.setBounds(508, 37, 70, 15);
		frame.getContentPane().add(lblView);

		textField = new JTextArea();
		textField.setBounds(12, 12, 332, 511);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		inquiryButton = new JButton("Inquiry");
		inquiryButton.setBounds(369, 182, 117, 25);
		inquiryButton.setHorizontalAlignment(SwingConstants.RIGHT);
		inquiryButton.setName("inquiryButton");
		frame.getContentPane().add(inquiryButton);

		clearButton = new JButton("ResetServer");
		clearButton.setBounds(369, 86, 117, 25);
		clearButton.setHorizontalAlignment(SwingConstants.RIGHT);
		clearButton.setName("clearButton");
		frame.getContentPane().add(clearButton);

		waitButton = new JButton("Waiting for Connection");
		waitButton.setBounds(369, 134, 117, 25);
		waitButton.setHorizontalAlignment(SwingConstants.RIGHT);
		waitButton.setName("waitButton");
		frame.getContentPane().add(waitButton);

		viewInquiredButton = new JButton("Inquired");
		viewInquiredButton.setBounds(499, 86, 117, 25);
		viewInquiredButton.setName("viewInquiryButton");
		frame.getContentPane().add(viewInquiredButton);

		viewConnectionsButton = new JButton("Connections");
		viewConnectionsButton.setBounds(499, 182, 117, 25);
		viewConnectionsButton.setName("viewConnectionButton");
		frame.getContentPane().add(viewConnectionsButton);

		viewDebugButton = new JButton("DebugInfos");
		viewDebugButton.setBounds(498, 134, 117, 25);
		viewDebugButton.setName("viewDebugButton");
		frame.getContentPane().add(viewDebugButton);

	}

	private void updateInquiredView(Object arg1) {
		ArrayList<String> inquiredDevices = (ArrayList<String>) arg1;
		String text = "";
		for (String i : inquiredDevices) {
			text += i + "\n";
		}
		viewString = text;
		textField.setText(text);

	}

	@Override
	public void update(Observable arg0, Object arg1) {
		switch (showView) {
		case INQUIRED:
			updateInquiredView(arg1);
			break;
		case CONNECTION:
			updateConnectionView(arg1);
		case DEBUGINFOS:
			updateDebuginfosView(arg1);
		case DEVICE:
			updateDeviceView(arg1);
		default:
			break;
		}

	}

	private void updateDeviceView(Object arg1) {
		InternMessage msg = (InternMessage) arg1;
		if (msg.whatMsg == WhatMsg.CONNECTIONMSG) {
			if (msg.id == conId) {
				viewString += msg.msg + "\n";
				textField.setText(viewString);
			}
		}

	}

	private void updateDebuginfosView(Object arg1) {
		InternMessage msg = (InternMessage) arg1;
		if (msg.whatMsg == WhatMsg.DEBUGMSG) {
			viewString += msg.msg + "\n";
			textField.setText(viewString);
		}

	}

	private void updateConnectionView(Object arg1) {
		// textField.setText("Connections");

	}

	public void addController(Controller controller) {
		inquiryButton.addActionListener(controller);
		clearButton.addActionListener(controller);
		waitButton.addActionListener(controller);
		viewConnectionsButton.addActionListener(controller);
		viewDebugButton.addActionListener(controller);
		viewInquiredButton.addActionListener(controller);
	}

	public void viewInquiry(Server server) {
		showView = ShowView.INQUIRED;
		ArrayList<String> inquiredDevices = server.getInquiredDevices();
		if (inquiredDevices.size() > 0) {
			String text = "";
			for (String i : inquiredDevices) {
				text += i + "\n";
			}
			textField.setText(text);
		} else {
			textField.setText("");
		}
	}

	public void viewDeviceConnection(int connectionId, Server server) {
		showView = ShowView.DEVICE;
		System.out.println(connectionId);
		conId = connectionId;
		viewString = "device " + connectionId;
		ArrayList<String> msgs = server.getAllMsgsFromConnectionByID(connectionId);
		String txt = "";
		for (String m : msgs) {
			txt += m + "\n";
		}
		viewString += txt;
		textField.setText(viewString);
	}

	public void viewConnection(Server server, Controller controller) {
		showView = ShowView.DEBUGINFOS;
		textField.setText("Number of Connection: " + server.getNumberOfConections());
		newButtons = new JButton[server.getNumberOfConections()];
		for (int i = 0; i < newButtons.length; i++) {
			JButton button = new JButton("test" + i);
			button.addActionListener(controller);
			button.setName("deviceButton;" + i);
			button.setBounds(viewConnectionsButton.getX(), viewConnectionsButton.getY() + 50 * (i + 1), 117, 25);
			frame.getContentPane().add(button);
			newButtons[i] = button;
		}
	}

	public void viewDebug(Server server) {
		showView = ShowView.DEBUGINFOS;
		viewString = "DebugInfos:  \n";
		ArrayList<String> msgs = server.getAllMsgs();
		String txt = "";
		for (String m : msgs) {
			txt += m + "\n";
		}
		viewString += txt;
		textField.setText(viewString);
	}

}
