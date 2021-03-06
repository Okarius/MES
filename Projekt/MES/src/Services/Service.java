package Services;

/**
 * This abstract class is used to implement every Service. Every Service should
 * have specific methods those are specified here. Every Service gets an id and
 * name. The id correspondence to their position in an ArrayList created in the
 * ServiceManager. The id and name will be send to the client so it knows which
 * services are available. Every Implemented Service has to extend this class
 * "Service". The Services get the MSGs Payload and decide what to answer. If
 * the Service gets wrong arguments they will return an ErrorMsg
 * 
 * @author Nikolas
 *
 */
public abstract class Service {
	protected int id;
	protected String name;
	/*
	 * The debugMsg gets send to the View It is important to set it! Otherwise
	 * the view will print "debugMsgNotSet!". The client will not see the
	 * debugMsg!!
	 */
	protected String debugMsg;

	public Service(int _id, String _name) {
		this.id = _id;
		this.name = _name;
		debugMsg = "debugMsgNotSet!";
	}

	/**
	 * getAnswer(String) returns the Payload the Client will receive. The
	 * Services decide on their own how to handle requests. The debugMsg has to
	 * be set here
	 * 
	 * @param payload
	 * @return payload answer to send the client
	 */
	public abstract byte[] getAnswer(String payload);

	/**
	 * Gets the Payload and removes the Services id. Gives the service an Array
	 * of every Argument.
	 * 
	 * @param payload
	 * @return ArgumentsArray without the Services Id.
	 */
	protected String[] getArgumentsArray(String payload) {
		String[] payloadArray = payload.split(";");
		String[] payloadArrayWithoutID = new String[payloadArray.length - 1];
		for (int i = 1; i < payloadArray.length; i++) {
			payloadArrayWithoutID[i - 1] = payloadArray[i];
		}
		return payloadArrayWithoutID;
	}

	public String getDebugMessage() {
		return debugMsg;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
