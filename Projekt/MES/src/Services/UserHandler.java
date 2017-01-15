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
		CSVHighscoreEntries
				.add(new CSVHighscoreEntry(Integer.toString(gameId), playerName, Integer.toString(highscore)));
		sortCSVEntries();
		return getHighscoreList();
	}

	private void sortCSVEntries() {
		int n = CSVHighscoreEntries.size();
		CSVHighscoreEntry temp;
		for (int i = 0; i < n; i++) {
			for (int j = 1; j < (n - i); j++) {
				if (Integer.getInteger(CSVHighscoreEntries.get(j - 1).highscore) > Integer
						.getInteger(CSVHighscoreEntries.get(j).highscore)) {
					temp = CSVHighscoreEntries.get(j - 1);
					CSVHighscoreEntries.set(j - 1, CSVHighscoreEntries.get(j));
					CSVHighscoreEntries.set(j, temp);
				}
			}
		}
	}

	public String getHighscore(int gameId) {
		for (CSVHighscoreEntry e : CSVHighscoreEntries) {
			if (e.gameId.equals(Integer.toString(gameId))) {
				return e.highscore;
			}
		}
		return "highscore not found for this game id";
	}

	public String getHighscoreList() {
		String highscoreList = "";
		for (int i = 1; i < CSVHighscoreEntries.size(); i++) {
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
