package Services;

public class PhoneNumberService extends Service {

	private PhoneNumberHandler phoneNumberHandler;

	public PhoneNumberService(int _id, String _name) {
		super(_id, _name);
		phoneNumberHandler = new PhoneNumberHandler();

	}

	@Override
	public byte[] getAnswer(String payload) {
		System.out.println("STARTGETANSWERINSERVICE");
		String[] payloadArray = this.getArgumentsArray(payload);
		System.out.println(payloadArray.length);
		String msg = "";
		int numberOfArguments = payloadArray.length;
		switch (numberOfArguments) {
		case 0:
			msg = phoneNumberHandler.getAllNumbers();
			break;
		case 1:
			msg = phoneNumberHandler.deletePhoneNumber(payloadArray[1]);
			break;
		case 2:
			msg = phoneNumberHandler.addNewNumber(payloadArray[1], payloadArray[2]);
			break;
		default:
			msg = "Illegal arguments for Service PhoneNumber";
			break;
		}
		System.out.println(msg);
		debugMsg = msg;
		return msg.getBytes();
	}

}
