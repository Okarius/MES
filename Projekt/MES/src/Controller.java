import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import Model.Server;

/**
 * The Controller Class implements an ActionListener. It is the ActionListener for the Views
 * Buttons. 
 * It holds the Server(Model) and View as Attributes. 
 * The Controller updates the View using the Server.
 * @author Nikolas
 *
 */
public class Controller implements ActionListener {
	Server server;
	View view;
	/**
	 * This Constructed initializes the Server and View.
	 * The Server receive ready. And receives the View as an Observer
	 */
	public Controller() {
		server = new Server();
		view = new View(this);
		server.addObserver(view);
		server.newConnection();
	}

	/**
	 * A Button got clicked, update View.
	 * @param  ActionEvent e -> ButtonClicked.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String name = ((JButton) e.getSource()).getName();
		String[] names = name.split(";");
		switch (names[0]) {
		case "viewConnectionButton":
			view.viewConnection(server.getStorage());
			break;
		case "deviceButton":
			view.viewDeviceConnection(Integer.valueOf(names[1]), server.getStorage());
		default:
			break;
		}
	}

}
