package Container;

/**
 * The InternMessage class is used to pass debugMesages and messages sent or
 * received by clients comfortable to the server. It Conatins a lot of important
 * informations. If its the firstMsg thus a new Connection got created The
 * connection Id to pringt the Msgs seperate for every connection If its the
 * lastMsg thus this connection got stopped. Also its contains if its a DebugMsg
 * or a ConnectionMsg. Connection Msgs are msgs the Server Received or Sent to a
 * Client While DebugMsgs are msgs We use to Debug the System. Those are also
 * printed in the view.
 * 
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
	 * @param _from
	 * @param _id
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
	 * Constructor for new Connections
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
	
	public InternMessage connectionGone(){
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
	 * @param headerToSend
	 * @param _from
	 * @param _id
	 */
	public InternMessage(byte[] headerToSend, boolean _from, int _id, int length, int msgID, int checksum,
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
