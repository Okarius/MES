package Container;

import java.util.ArrayList;

public class ConnectedDevices extends ArrayList<ConnectionStorage> {

	public void newMsgById(String msg, int id, boolean from) {
		for (int i = 0; i < this.size(); i++)
			if (this.get(i).getId() == id)
				if (from)
					this.get(i).saveMSGFromDevice(msg + "\n");
				else
					this.get(i).saveMSGToDevice(msg + "\n");

	}

	public ArrayList<String> getMsgsById(int id) {
		for (ConnectionStorage d : this)
			if (d.getId() == id)
				return d.getMsgs();

		return null;
	}

}
