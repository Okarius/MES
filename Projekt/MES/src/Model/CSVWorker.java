package Model;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Services.CSVEntry;

public class CSVWorker {
	String path;

	public CSVWorker(String _path) {
		path = _path;
	}

	public void writeToCSV(ArrayList<CSVEntry> CSVEntries) {
		String csvString = "";
		for (CSVEntry e : CSVEntries) {
			csvString += e.objectToLine() + "\n";
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
}
