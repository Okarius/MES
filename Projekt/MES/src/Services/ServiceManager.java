package Services;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class Manages every Service. It gets the Message Payload and gives it to
 * the correct Service. The Service returns then the Answer and the
 * ServiceManager passes the answer to the ClientConnection. To implement a new
 * Service add it to the ServiceManagers Constructor.
 * 
 * @author Nikolas
 */
@SuppressWarnings("serial")
public class ServiceManager extends ArrayList<Service> {

	// The debug msg will be printed in the view
	// Dont forget to set it!
	private String debugMsg;

	/**
	 * Add every Service to this Constructor
	 */
	public ServiceManager() {
		this.add(new SecretaryPhoneNumberService(100, "SecretaryPhoneNumberService"));
		this.add(new PhoneNumberService(101, "PhoneNumberService"));
		this.add(new QuizService(102, "QuizService"));
		this.add(new QuizService(103, "PictureService"));
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
	@SuppressWarnings("unused")
	public byte[] getAnswer(String payload) {
		String[] payloadArray = payload.split(";");
		byte[] sendMe = "Invalid Payload".getBytes();
		debugMsg = "Error;Invalid Payload";
		String msg = "";
		// Is the first value a number(id)?
		if (!isFirstValueNumeric(payloadArray[0])) {
			// non numeric and not services -> error
			msg = "Error;NonNumeric ID";
			sendMe = msg.getBytes();
			debugMsg = msg;
		} else {
			int serviceId = Integer.valueOf(payloadArray[0]);
			// Service id 0 is the request to send a list of all
			// available services.
			if (serviceId == 0) {
				msg = this.getServicesString();
				sendMe = msg.getBytes();
				debugMsg = "Services Send\n" + msg;
			} else {
				Service service = getServiceById(serviceId);
				// Check if id is a actuall Service
				if (service != null) {
					// The id exists, so we can use one of our services
					sendMe = service.getAnswer(payload);
					debugMsg = service.debugMsg;
				} else {
					// Invalid ID -> Send Error Message
					msg = "Error;Invalid ID";
					sendMe = msg.getBytes();
					debugMsg = msg;
				}
			}

		}
		byte[] endSymbol = "\0".getBytes();
		sendMe = addElement(sendMe, endSymbol[0]);
		return sendMe;
	}

	static byte[] addElement(byte[] a, byte e) {
		a = Arrays.copyOf(a, a.length + 1);
		a[a.length - 1] = e;
		return a;
	}

	/**
	 * Builds the string the Clients get if they ask which services the Server
	 * Provides
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

	/**
	 * Function returns the service corresponding to the Id received in the
	 * payload. If this Id does not exists it returns null Otherwise the correct
	 * service
	 * 
	 * @param id
	 * @return service if service exists, otherwise null
	 */
	private Service getServiceById(int id) {
		for (int i = 0; i < this.size(); i++) {
			if (this.get(i).id == id)
				return this.get(i);
		}
		return null;
	}

	public boolean pictureRequested(String string) {
		for (int i = 0; i < this.size(); i++) {
			if (this.get(i).name.toLowerCase().contains("picture".toLowerCase()))
				return true;

		}
		return false;
	}
}
