import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class Controller implements ActionListener {
	ServerState state;
	Inquiry inquiry;
	public Controller(ServerState _state, Inquiry _inquiry) {
		state = _state;
		inquiry = _inquiry;
		inquiry.addController(this);
		state.addObserver(inquiry);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String name = ((JButton) e.getSource()).getName();
		System.out.println(name);
		switch (name) {
		case "buttonInquiry":
			state.inquire();
			break;
		case "buttonClear":
			state.reset();
			break;
		case "buttonWait":
			state.echoServer();
			break;	
		default:
			break;
		}
	}

}