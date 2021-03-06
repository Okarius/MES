import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import Model.Server;

/**
 * This implements an ActionListener. It is the ActionListener for the Views
 * Buttons. It controlls both Server and View.
 * 
 * @author Nikolas+Nico
 *
 */
public class Controller implements ActionListener {
	Server server;
	View view;

	public Controller() {
		server = new Server();
		view = new View(this);
		server.addObserver(view);
		server.newConnection();
	}

	/**
	 * A Butto got pressed, Decide what to do.
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
