package Services;

import java.util.ArrayList;

/**
 * This class Manages every Service. It gets the Message Payload and gives it to
 * the correct Service. The Service returns then the Answer and the
 * ServiceManager passes the answer to the ClientConnection. To implement a new
 * Service add it to the ServiceManagers Constructor.
 * 
 * @author Nikolas
 */
public class ServiceManager extends ArrayList<Service> {

	//The debug msg will be printed in the view
	//Dont forget to set it!
	private String debugMsg;
	
	/**
	 * Add every Service to this Constructor
	 */
	public ServiceManager() {
		this.add(new SecretaryPhoneNumberService(this.size(), "SecretaryPhoneNumber"));
	}

	/**
	 * getAnswer(String) returns the Payload which will be send to the Client.
	 * The fnkt gets called in the ClientConnection Runnable whenever the Server
	 * Receives a MSG. GetAnswer(String) checks if the Payload of the MSG if
	 * valid Otherwise it creates a fitting Errormessage.
	 * 
	 * @param payload
	 * @return byte[] to send it to the client
	 */
	public byte[] getAnswer(String payload) {
		String[] payloadArray = payload.split(";");
		byte[] sendMe = "Invalid Payload".getBytes();
		debugMsg = "Error;Invalid Payload";

		// Is the first value a number(id)?
		if (!isFirstValueNumeric(payloadArray[0])) {
			// if non numeric first Letter it has to be Servies
			if (payloadArray[0].compareTo("SERVICES") == 0) {
				String servicesList = this.getServicesString();
				sendMe = servicesList.getBytes();
				debugMsg = "Services Send\n" + servicesList;
			} else {
				// non numeric and not services -> error
				String msg = "Error;NonNumeric ID and not SERVICES";
				sendMe = msg.getBytes();
				debugMsg = msg;
			}
			// First value is a Number thus maybe an ID
		} else {
			int serviceId = Integer.valueOf(payloadArray[0]);
			// can this ID exist?
			if (validServiceId(serviceId)) {
				// The id exists, so we can use one of our services
				sendMe = this.get(serviceId).getAnswer(payload);
				debugMsg = this.get(serviceId).debugMsg;
			} else {
				// Invalid ID -> Send Error Message
				String msg = "Error;Invalid ID";
				sendMe = msg.getBytes();
				debugMsg = msg;
			}
		}
		return sendMe;
	}

	/**
	 * Builds the string the Clients get if they ask which services the Server
	 * Provides
	 * 
	 * @return List oft All Services as string.
	 */
	private String getServicesString() {
		String sendMe = "";
		for (int i = 0; i < this.size(); i++) {
			Service service = this.get(i);
			sendMe += service.id + ";" + service.name + "\n";
		}
		return sendMe;
	}

	/**
	 * Funktion to check if an Id is Valid. Valid IDs are those which are
	 * between 0 and the NumberOfServices.
	 * 
	 * @param serviceId
	 * @return true if this id exists
	 */
	private boolean validServiceId(int serviceId) {
		if (serviceId < 0)
			return false;
		if (serviceId >= this.size())
			return false;
		return true;
	}

	public String getLastMsgSend() {
		return debugMsg;
	}

	/**
	 * This Function checks if the payloads first value is actually a Integer.
	 * Otherwise we got a bad Msg. (Exception is SERVICES)
	 * 
	 * @param str
	 * @return if the str is a number true
	 */
	private static boolean isFirstValueNumeric(String str) {
		try {
			int id = Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
}
