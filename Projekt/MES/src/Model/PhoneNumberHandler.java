package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * @author root
 *
 */
class CSVEntry {
	public String id;
	public String firstName;
	public String secondName;
	public String number;

	public CSVEntry(String _id, String _firstName, String _secondName, String _number) {
		this.id = _id;
		this.firstName = _firstName;
		this.secondName = _secondName;
		this.number = _number;
	}
}

public class PhoneNumberHandler {
	private ArrayList<CSVEntry> CSVEntries;

	public PhoneNumberHandler() {
		CSVEntries = new ArrayList<CSVEntry>();
		String path = "phoneNumbers.csv";
		String line = "";
		String cvsSplitBy = ";";
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			while ((line = br.readLine()) != null) {
				String[] lineArray = line.split(cvsSplitBy);
				CSVEntries.add(new CSVEntry(lineArray[0], lineArray[1], lineArray[2], lineArray[3]));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getNumberById(int id) {
		for (CSVEntry e : CSVEntries) {
			if (e.id.equals(Integer.toString(id))) {
				return e.number;
			}
		}
		return "NUMBER NOT FOUND";
	}

	public String getNumberById(String id) {
		for (CSVEntry e : CSVEntries) {
			if (e.id.equals(id)) {
				return e.number;
			}
		}
		return "NUMBER NOT FOUND";
	}


	public String getNumberByFirstName(String firstName) {
		for (CSVEntry e : CSVEntries) {
			if (Arrays.equals(e.firstName.getBytes(), firstName.getBytes())) {
				return e.number;
			}
		}
		return "NUMBER NOT FOUND";
	}
}