package Services;

import java.util.ArrayList;

/**
 * The PhoneNumberHandler manages the telephone book. It is possible to add and delete
 * numbers etc.
 * 
 * @author nico
 *
 */
class PhoneBookEntry implements CSVEntry {
	public String firstName;
	public String number;
	public String id;

	public PhoneBookEntry(int i, String _firstName, String _number) {
		this.id = i + "";
		this.firstName = _firstName;
		this.number = _number;
	}

	public PhoneBookEntry(String[] line) {
		this.id = line[0];
		this.firstName = line[1];
		this.number = line[2];
	}

	public String objectToLine() {
		return this.id + ";" + this.firstName + ";" + this.number;
	}

}

public class PhoneNumberHandler {
	private ArrayList<PhoneBookEntry> CSVEntries;
	private CSVWorker csvWorker;

	public PhoneNumberHandler() {
		csvWorker = new CSVWorker("phoneNumbers.csv");
		CSVEntries = new ArrayList<>();
		for (String[] l : csvWorker.readCSV()){
			CSVEntries.add(new PhoneBookEntry(l));			
		}


	}

	/**
	 *
	 * @return all phone numbers as String
	 */
	
	public String getAllNumbers() { 
		String numbers = "";
		for (int i = 1; i < CSVEntries.size();i++) {
			numbers += CSVEntries.get(i).objectToLine() +"&";
		}
		return numbers;
	}

	/**
	 * 
	 * @param firstName: name to be added
	 * @param number: number to be added
	 * @return NewNumber;3; and added entry
	 */
	public String addNewNumber(String firstName, String number) {
		if (firstName == "") {
			return "No valid FIRSTNAME";
		} else if (number == "") {
			return "No valid PHONENUMBER";
		} 
		else if(duplicateNumber(number)) {
			return "This number already exists";
		}
		
		else {
			CSVEntries.add(new PhoneBookEntry(CSVEntries.size() + 1, firstName, number));
			csvWorker.writeToCSV(CSVEntries);
			return "NewNumber;3;" + firstName + ";" + number;
		}
	}
	
	/**
	 * 
	 * @param number: number that may already exist
	 *
	 */
	public boolean duplicateNumber(String number) {
		for(PhoneBookEntry e : CSVEntries) {
			if(e.number.equals(number)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param id: id of the phonebook entry to be deleted
	 * @return String, success information
	 */
	public String deletePhoneNumber(String id) {
		for (PhoneBookEntry e : CSVEntries) {
			String checkMe = e.firstName;
			if (checkMe.equals(id)) {
				CSVEntries.remove(e);
				csvWorker.writeToCSV(CSVEntries);
				return "Number deleted";
			}
		}
		return "Number not found";
	}
}
