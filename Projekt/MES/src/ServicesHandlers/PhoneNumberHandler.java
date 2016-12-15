package ServicesHandlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import Services.CSVEntry;


/**
 * 
 * @author root
 *
 */
class PhoneBookEntry implements CSVEntry{
	public String id;
	public String firstName;
	public String number;

	public PhoneBookEntry(String _id, String _firstName, String _number) {
		this.id = _id;
		this.firstName = _firstName;
		this.number = _number;
	}

	@Override
	public String objectToLine() {
		return  this.id + ";" + this.firstName + ";" + this.number + "\n";
		
	}
}

public class PhoneNumberHandler {
	private ArrayList<PhoneBookEntry> CSVEntries;

	public PhoneNumberHandler() {
		CSVEntries = new ArrayList<PhoneBookEntry>();
		String path = "phoneNumbers.csv";
		String line = "";
		String cvsSplitBy = ";";
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			while ((line = br.readLine()) != null) {
				String[] lineArray = line.split(cvsSplitBy);
				CSVEntries.add(new PhoneBookEntry(lineArray[0], lineArray[1], lineArray[2]));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getNumberById(int id) {
		for (PhoneBookEntry e : CSVEntries) {
			if (e.id.equals(Integer.toString(id))) {
				return e.number;
			}
		}
		return "NUMBER NOT FOUND";
	}

	public String getNumberById(String id) {
		for (PhoneBookEntry e : CSVEntries) {
			if (e.id.equals(id)) {
				return e.number;
			}
		}
		return "NUMBER NOT FOUND";
	}


	public String getNumberByFirstName(String firstName) {
		for (PhoneBookEntry e : CSVEntries) {
			if (Arrays.equals(e.firstName.getBytes(), firstName.getBytes())) {
				return e.number;
			}
		}
		return "NUMBER NOT FOUND";
	}
	
	public String getAllNumbers() {		//tut das?
		String numbers = "";
		for (PhoneBookEntry e : CSVEntries) {
			numbers += e.firstName +" : "+e.number +"\n";
		}
		return numbers;
	}
	
	public String addNewNumber(String firstName, String number) {
		
		
		if(firstName == "") {
			return "No valid FIRSTNAME";
		}
		else if (number == "") {
			return "No valid PHONENUMBER";
		}
		else {
			String id = CSVEntries.size() + 1+"";
			
			CSVEntries.add(new PhoneBookEntry(id,firstName, number));
			writeToCSV(CSVEntries);
		}
		
		return "Number added";
	}
	
	public String deletePhoneNumber(String id) {
		for (PhoneBookEntry e : CSVEntries) {
			String checkMe = e.firstName + " : "+e.number;
			if (checkMe.equals(id)) {
				CSVEntries.remove(e);
				writeToCSV(CSVEntries);
				return "Number deleted";
			}
		}
		return "Number not found";
	}
	
	public void writeToCSV(ArrayList<PhoneBookEntry> CSVEntries) {
		
		String csvString = "";
		for (PhoneBookEntry e : CSVEntries) {
			csvString =csvString+ e.id +";"+ e.firstName + ";" + e.number + "\n";
		}
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("phoneNumbers.csv"));
			bw.write(csvString);
			bw.close();
			System.out.println("File geschrieben: " + csvString );

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
