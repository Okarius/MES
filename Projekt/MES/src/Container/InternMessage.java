package Container;

/**
 * The InternMessage class is used to pass debugMesages and messages sent or
 * received by clients comfortable to the server. It contains a lot of important
 * informations. Those InternMessages are used by the Connections in
 * notifyObservers(); If its the firstMsg thus a new Connection got created The
 * connection Id to print the messages separate for every connection 
 * 
 * @author niki
 * 
 */
public class InternMessage {

	public String msg;
	public boolean firstMsg, from;
	public int id;
	public boolean lastMsg;

	/**
	 * Constructor for Connections
	 * 
	 * @param _msg
	 *            debugMsg
	 * @param _from
	 *            if true string contains "From: " else "To: "
	 * @param _id
	 *            id to find fitting connection
	 */
	public InternMessage(String _msg, boolean _from, int _id) {
		msg = _msg;
		id = _id;
		from = _from;
		firstMsg = false;
		lastMsg = false;

		if (from)
			msg = "From: " + msg;
		else
			msg = "To: " + msg;
	}

	/**
	 * Constructor for new Connections A new connection, thus firstMsg = true
	 * Necessary to update Storage (newDevice) and View(newButton)
	 * 
	 * @param _msg
	 * @param _from
	 * @param _id
	 */
	public InternMessage(String _msg, int _id) {
		msg = _msg;
		id = _id;
		firstMsg = true;
		lastMsg = false;
	}
	/**
	 * A connection shuts down.
	 * lastMsg = true
	 * @return
	 */
	public InternMessage connectionGone() {
		this.lastMsg = true;
		this.firstMsg = false;
		return this;
	}

	public void setAsLastMsg() {
		lastMsg = true;
		firstMsg = false;

	}

	public void setAsFirstMsg() {
		lastMsg = false;
		firstMsg = true;

	}

	/**
	 * Constructor to print headers nicely
	 * 
	 * @param _from
	 * @param _id
	 */
	public InternMessage( boolean _from, int _id, int length, int msgID, int checksum,
			boolean lastMsgCorrect) {
		msg = "Header: \n";
		msg += "Length: " + length + "\n";
		msg += "Checksum: " + checksum + "\n";
		msg += "MsgId: " + msgID + "\n";
		msg += "LastMsgFaulty: " + lastMsgCorrect + "\n";
		id = _id;
		from = _from;
		firstMsg = false;
		lastMsg = false;
		if (from)
			msg = "From: " + msg;
		else
			msg = "To: " + msg;
	}

}
