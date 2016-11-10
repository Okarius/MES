import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import Model.Server;
/**
 * 
 * @author root
 *
 */
public class Controller implements ActionListener {
	Server server;
	View view;
	public Controller(Server _state, View _inquiry) {
		server = _state;
		view = _inquiry;
		view.addController(this);
		server.addObserver(view);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String name = ((JButton) e.getSource()).getName();

		String[] names=name.split(";");
		switch (names[0]) {
		case "inquiryButton":
			server.inquire();
			break;
		case "clearButton":
			server.reset();
			break;
		case "waitButton":
			server.newConnection();
			break;	
		case "viewInquiryButton":
			view.viewInquiry(server);
			break;	
		case "viewConnectionButton":
			view.viewConnection(server,this);
			break;	
		case "viewDebugButton":
			view.viewDebug(server);
			break;	
		case "deviceButton":
			view.viewDeviceConnection(names[1], server);

		default:
			break;
		}
	}

}
