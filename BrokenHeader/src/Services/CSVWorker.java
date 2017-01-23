package Services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The CSVWorker Reads the CSVFiles and returns a ArrayList of String[]. The
 * String[] is the CSV line splitted by ";" Those String[] are used in the
 * ServiceHandlers. Furthermore is this Class able to write back every possible
 * CSVEntry. Possible CSVEntries are with example QuizEntry, PhoneBookEntries
 * 
 * @author Niki
 */
public class CSVWorker {
	String path;

	public CSVWorker(String _path) {
		path = _path;
	}

	/**
	 * This Function writes every CSVEntry back to its csvFile. the CSVEntries
	 * are used in the Services. They are used to read and work with the
	 * CSV-File.
	 * 
	 * @param CSVEntries
	 *            a CSVEntry is an Interface.
	 */
	public <T> void writeToCSV(ArrayList<T> CSVEntries) {
		String csvString = "";
		for (T e : CSVEntries) {
			csvString += ((CSVEntry) e).objectToLine() + "\n";
		}
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(this.path));
			bw.write(csvString);
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * 
	 * @return String[] list of split rows
	 */
	public ArrayList<String[]> readCSV() {
		ArrayList<String[]> CSVEntries = new ArrayList<>();
		String line = "";
		String cvsSplitBy = ";";
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			while ((line = br.readLine()) != null) {
				String[] lineArray = line.split(cvsSplitBy);
				CSVEntries.add(lineArray);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return CSVEntries;
	}

}
