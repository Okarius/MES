package Services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * @author Nico
 *
 */


class CSVHighscoreEntry {
	public String gameId;
	public String playerName;
	public String highscore;

	public CSVHighscoreEntry(String _gameId, String _playerName, String _highscore) {
		this.gameId = _gameId;
		this.playerName = _playerName;
		this.highscore = _highscore;
	}
}


public class UserHandler {
	private ArrayList<CSVHighscoreEntry> CSVHighscoreEntries;

	public UserHandler() {
		CSVHighscoreEntries = new ArrayList<CSVHighscoreEntry>();
		String path = "highscores.csv";
		String line = "";
		String cvsSplitBy = ";";
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			while ((line = br.readLine()) != null) {
				String[] lineArray = line.split(cvsSplitBy);
				System.out.println(line);
				CSVHighscoreEntries.add(new CSVHighscoreEntry(lineArray[0], lineArray[1], lineArray[2]));
			}
			br.close(); 	//obacht
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String addPlayer(String playerName) {
		
		if(playerName == "") {
			return "no valid player name";
		}
		String gameId = CSVHighscoreEntries.size() + 1+"";
		
		CSVHighscoreEntries.add(new CSVHighscoreEntry(gameId,playerName, "0"));
		writeToCSV(CSVHighscoreEntries);
		
		return "player added";
	}
	
	public String getHighscores() {		
		String numbers = "";
		for (CSVHighscoreEntry e : CSVHighscoreEntries) {
			numbers += e.playerName +" : "+e.highscore +"\n";
		}
		return numbers;
	}
	
	public String increaseHighscore(String gameId) {
		for (CSVHighscoreEntry e : CSVHighscoreEntries) {
			if(e.gameId.equals(gameId)) {
				int highscore = Integer.parseInt(e.highscore);
				highscore++;
				e.highscore = Integer.toString(highscore);
				writeToCSV(CSVHighscoreEntries);
				
				return "highscore increased";
			}
		}
		return "game ID not found";
	}
	
	public void writeToCSV(ArrayList<CSVHighscoreEntry> CSVHighscoreEntries) {
		
		String csvString = "";
		for (CSVHighscoreEntry e : CSVHighscoreEntries) {
			csvString = e.gameId +";"+ e.playerName + ";" + e.highscore + "\n";
		}
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("phoneNumbers.csv"));
			bw.write(csvString);
			bw.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
