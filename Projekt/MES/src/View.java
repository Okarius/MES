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
import Container.Storage;
import Model.Server;

import javax.swing.JLabel;
import javax.swing.JTextArea;

enum ShowView {
	DEBUGINFOS, CONNECTION, DEVICE
}

public class View implements Observer {

	private JFrame frame;
	private JTextArea textField;
	// private ServerState state;
	private JButton clearButton, viewDebugButton, viewConnectionsButton;
	private JButton[] newButtons;
	private ShowView showView;
	private String viewString;
	private int conId;
	private Controller controller;

	public View(Controller _controller) {
		// state.addObserver(this);
		initialize();
		frame.setVisible(true);
		controller = _controller;
		addController();
		showView = ShowView.DEBUGINFOS;
		viewString = "";
	}

	/**
	 * Initialize the contents of the frame.
	 * 
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

		clearButton = new JButton("ResetServer");
		clearButton.setBounds(369, 86, 117, 25);
		clearButton.setHorizontalAlignment(SwingConstants.RIGHT);
		clearButton.setName("clearButton");
		frame.getContentPane().add(clearButton);

		viewConnectionsButton = new JButton("Connections");
		viewConnectionsButton.setBounds(499, 182, 117, 25);
		viewConnectionsButton.setName("viewConnectionButton");
		frame.getContentPane().add(viewConnectionsButton);

		viewDebugButton = new JButton("DebugInfos");
		viewDebugButton.setBounds(498, 134, 117, 25);
		viewDebugButton.setName("viewDebugButton");
		frame.getContentPane().add(viewDebugButton);

	}

	/**
	 * This update fkt gets called from "Server" It gets the whole Storage. This
	 * fkt has to check first what the View shows right now(showView). Depending
	 * on this will the view update.
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		// The storage saves every Message sent and received
		Storage storage = (Storage) arg1;
		switch (showView) {
		case CONNECTION:
			updateConnectionView(storage);
		case DEBUGINFOS:
			updateDebuginfosView(storage);
		case DEVICE:
			updateDeviceView(storage);
		default:
			break;
		}
		if (storage.connectionsChanged()) {
			updateDynamicButtons(storage);
		}
	}

	public void updateDynamicButtons(Storage storage) {
		ArrayList<Integer> allIds = storage.getAllIdsFromRunningConnections();
		newButtons = new JButton[allIds.size()];
		for (int i = 0; i < allIds.size(); i++) {
			int id = allIds.get(i);
			JButton button = new JButton("ID: " + id);
			button.addActionListener(this.controller);
			button.setName("deviceButton;" + id);
			button.setBounds(viewConnectionsButton.getX(), viewConnectionsButton.getY() + 50 * (i + 1), 117, 25);
			frame.getContentPane().add(button);
			newButtons[i] = button;
		}
	}

	private void updateDeviceView(Storage storage) {
		InternMessage lastMsg = storage.getLastDebugMsg();
		if (lastMsg.whatMsg == WhatMsg.CONNECTIONMSG) {
			if (lastMsg.id == conId) {
				viewString += lastMsg.msg + "\n";
				textField.setText(viewString);
			}
		}
	}

	private void updateDebuginfosView(Storage storage) {
		if (storage.getLastDebugMsg().whatMsg == WhatMsg.DEBUGMSG) {
			viewString += storage.getLastDebugMsg().msg + "\n";
			textField.setText(viewString);
		}

	}

	private void updateConnectionView(Storage storage) {
		textField.setText("Number of Connection: " + storage.getNumberOfConections());
	}

	public void addController() {
		clearButton.addActionListener(controller);
		viewConnectionsButton.addActionListener(controller);
		viewDebugButton.addActionListener(controller);
	}

	// *************ButtonsPressed********************//

	public void viewDeviceConnection(int connectionId, Storage storage) {
		showView = ShowView.DEVICE;
		conId = connectionId;
		viewString = "ConnectionId: " + connectionId + "\n";
		ArrayList<String> msgs = storage.getAllMsgsFromConnectionByID(connectionId);
		String txt = "";
		for (String m : msgs) {
			txt += m + "\n";
		}
		viewString += txt;
		textField.setText(viewString);
	}

	public void viewConnection(Storage storage) {
		showView = ShowView.CONNECTION;
		textField.setText("Number of Connection: " + storage.getNumberOfConections());
		updateDynamicButtons(storage);

	}

	public void viewDebug(Storage storage) {
		showView = ShowView.DEBUGINFOS;
		viewString = "DebugInfos:  \n";
		ArrayList<String> msgs = storage.getAllMsgsString();
		String txt = "";
		for (String m : msgs) {
			txt += m + "\n";
		}
		viewString += txt;
		textField.setText(viewString);
	}

}
