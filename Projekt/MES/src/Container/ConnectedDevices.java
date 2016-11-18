package Container;

import java.util.ArrayList;

public class ConnectedDevices extends ArrayList<ConnectionStorage> {

	public void newMsgById(InternMessage msg) {
		for (int i = 0; i < this.size(); i++)
			if (this.get(i).getId() == msg.id)
				if (msg.from)
					this.get(i).saveMSGFromDevice(msg.msg);
				else
					this.get(i).saveMSGToDevice(msg.msg);

	}

	public ArrayList<String> getMsgsById(int id) {
		for (ConnectionStorage d : this)
			if (d.getId() == id)
				return d.getMsgs();

		return null;
	}

}