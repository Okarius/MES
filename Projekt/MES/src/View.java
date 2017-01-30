import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Container.InternMessage;
import Container.Storage;

enum ShowView {
	CONNECTION, DEVICE
}
/**
 * The View Class Implements Observer.
 * It Observes the Server (Model)
 * And updates itself using the Servers Storage.
 * The View shows Messages it received for every Connection
 * It has a Button for every Connection it currently has to choose from. 
 * The Button "AllConnections" Shows how many connections are currently online. 
 * 
 * @author niki
 *
 */
public class View implements Observer {

	private JFrame frame;
	private JTextArea textField;
	private JScrollPane scrollPane;
	private JButton viewConnectionsButton;
	private JButton[] newButtons;
	private ShowView showView;
	private String viewString;
	private int conId;
	private Controller controller;

	public View(Controller _controller) {
		initialize();
		frame.setVisible(true);
		controller = _controller;
		addController();
		showView = ShowView.CONNECTION;
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(200, 200, 900, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		scrollPane = new JScrollPane();
		textField = new JTextArea();
		scrollPane.setBounds(12, 12, 332, 511);
		textField.setName("");
		scrollPane.setViewportView(textField);

		frame.getContentPane().add(scrollPane);

		viewConnectionsButton = new JButton("Connections");
		viewConnectionsButton.setBounds(499, 182, 117, 25);
		viewConnectionsButton.setName("viewConnectionButton");
		frame.getContentPane().add(viewConnectionsButton);

	}

	/**
	 * This update function gets called from "Server" It gets the whole Storage. This
	 * function has to check first what the View shows right now(showView). Depending
	 * on this will the view update. Also it checks if the connections changed.
	 * Thus its able to update the Dynamic buttons. This function gets the
	 * "Storage" from the server. The storage holds every message the server sent
	 * and received also it contains some debug messages like "New Connection"
	 *  @param Object arg1 This value has always the type Storage.
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		// The storage saves every Message sent and received
		Storage storage = (Storage) arg1;
		if (this.showView == ShowView.CONNECTION) {
			updateConnectionView(storage);
		} else {
			updateDeviceView(storage);
		}

		// Check if the dynamic Button have to change
		if (storage.connectionsChanged()) {
			updateDynamicButtons(storage);
		}
	}

	/**
	 * This function gets called whenever a new connection appears or an old one
	 * disappears. The buttons this function creates are used to decide which
	 * connection will be printed. It creates every Button new.
	 * @param Storage storage holds every Message send and received by the server
	 */
	public void updateDynamicButtons(Storage storage) {
		InternMessage lastMsg = storage.getLastMsg();
		if (lastMsg.lastMsg) {
			for (int i = 0; i < frame.getContentPane().getComponentCount(); i++) {
				if (frame.getContentPane().getComponent(i).getName() != null) {
					if (frame.getContentPane().getComponent(i).getName().compareTo("deviceButton;" + lastMsg.id) == 0) {
						frame.remove(frame.getContentPane().getComponent(i));
					}
				}

			}
		}
		ArrayList<Integer> allIds = storage.getAllIdsFromRunningConnections();
		newButtons = new JButton[allIds.size()];
		for (int i = 0; i < allIds.size(); i++) {
			int id = allIds.get(i);
			JButton button = new JButton("ID: " + id);
			button.addActionListener(this.controller);
			button.setName("deviceButton;" + id);
			button.setBounds(viewConnectionsButton.getX(), viewConnectionsButton.getY() + 50 * (i + 1), 117, 25);
			newButtons[i] = button;
			frame.getContentPane().add(newButtons[i]);
		}
		frame.repaint();
	}

	/**
	 * Adds the last msg to the View if the msg fits to the chosen connection
	 * 
	 */
	private void updateDeviceView(Storage storage) {
		InternMessage lastMsg = storage.getLastMsg();
		if (lastMsg.id == conId) {
			viewString += (lastMsg.msg + "\n");
			textField.setText(viewString);
		}
	}

	private void updateConnectionView(Storage storage) {
		textField.setText("Number of Connections: " + storage.getNumberOfConections());

	}

	public void addController() {
		viewConnectionsButton.addActionListener(controller);
	}

	// *************ButtonsPressed********************//

	/**
	 * Gets called when the buttons created in
	 * updateDynamicButtons(Storage storage) get pressed. The buttons have the
	 * Connection id in their name. Thus this function prints the whole connection of
	 * a certain device/connection. This function gets called from the controller
	 * 
	 * @param connectionId
	 * @param storage 
	 */
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

	/**
	 * Prints how many connections are up right now
	 * 
	 * @param storage
	 */
	public void viewConnection(Storage storage) {
		showView = ShowView.CONNECTION;
		textField.setText("Number of Connections: " + storage.getNumberOfConections());

	}

}
