package Services;

import java.util.ArrayList;

/**
 * 
 * @author Nico
 *
 */

class CSVHighscoreEntry implements CSVEntry {
	public String gameId;
	public String playerName;
	public String highscore;

	public CSVHighscoreEntry(String _gameId, String _playerName, String _highscore) {
		this.gameId = _gameId;
		this.playerName = _playerName;
		this.highscore = _highscore;
	}

	public CSVHighscoreEntry(String[] l) {

		this.gameId = l[0];
		this.playerName = l[1];
		this.highscore = l[2];
	}

	@Override
	public String objectToLine() {
		return this.gameId + ";" + this.playerName + ";" + this.highscore;
	}
}

public class UserHandler {
	private ArrayList<CSVHighscoreEntry> CSVHighscoreEntries;
	private CSVWorker csvWorker;

	public UserHandler() {
		CSVHighscoreEntries = new ArrayList<CSVHighscoreEntry>();
		csvWorker = new CSVWorker("highscores.csv");
		CSVHighscoreEntries = new ArrayList<>();
		for (String[] l : csvWorker.readCSV())
			CSVHighscoreEntries.add(new CSVHighscoreEntry(l));

	}

	public String setGameEntry(int highscore, String playerName, int gameId) {
		if (playerName == "") {
			return "no valid player name";
		}

		CSVHighscoreEntry thisEntry = insertScore(
				new CSVHighscoreEntry(Integer.toString(gameId), playerName, Integer.toString(highscore)));
		csvWorker.writeToCSV(CSVHighscoreEntries);
		return getHighscoreList() + thisEntry.playerName + ":" + thisEntry.highscore + "&";
	}

	public CSVHighscoreEntry insertScore(CSVHighscoreEntry mscore) {
		// find insert point
		ArrayList<CSVHighscoreEntry> before = new ArrayList<CSVHighscoreEntry>();
		ArrayList<CSVHighscoreEntry> after = new ArrayList<CSVHighscoreEntry>();
		before.add(CSVHighscoreEntries.get(0));
		for (int i = 1; i < CSVHighscoreEntries.size(); i++)
			if (Integer.parseInt(CSVHighscoreEntries.get(i).highscore) > Integer.parseInt(mscore.highscore)) {
				before.add(CSVHighscoreEntries.get(i));
			} else {
				after.add(CSVHighscoreEntries.get(i));
			}
		CSVHighscoreEntries = new ArrayList<>();
		// over-write values and reorder
		for (int i = 0; i < before.size(); i++)
			CSVHighscoreEntries.add(before.get(i));
		mscore.gameId = CSVHighscoreEntries.size()-1 + "";
		CSVHighscoreEntries.add(mscore);
		for (int i = 0; i < after.size(); i++)
			CSVHighscoreEntries.add(after.get(i));
		return mscore;

	}

	public CSVHighscoreEntry getHighscore(int gameId) {
		for (CSVHighscoreEntry e : CSVHighscoreEntries) {
			if (e.gameId.equals(Integer.toString(gameId))) {
				return e;
			}
		}
		return null;
	}

	public String getHighscoreList() {
		String highscoreList = "";
		for (int i = 1; i <= 10; i++) {
			highscoreList += CSVHighscoreEntries.get(i).playerName + ":" + CSVHighscoreEntries.get(i).highscore + "&";
		}
		return highscoreList;
	}

	public String increaseHighscore(String gameId) {
		for (CSVHighscoreEntry e : CSVHighscoreEntries) {
			if (e.gameId.equals(gameId)) {
				int highscore = Integer.parseInt(e.highscore);
				highscore++;
				e.highscore = Integer.toString(highscore);
				csvWorker.writeToCSV(CSVHighscoreEntries);
				return "highscore increased";
			}
		}
		return "game ID not found";
	}

}
