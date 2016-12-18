package Services;

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

	public CSVHighscoreEntry(String[] l) {
		this.gameId = l[0];
		this.playerName = l[1];
		this.highscore = l[2];
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

	public String addPlayer(String playerName) {

		if (playerName == "") {
			return "no valid player name";
		}
		String gameId = CSVHighscoreEntries.size() + 1 + "";

		CSVHighscoreEntries.add(new CSVHighscoreEntry(gameId, playerName, "0"));
		csvWorker.writeToCSV(CSVHighscoreEntries);

		return "player added";
	}

	public String getHighscores() {
		String numbers = "";
		for (CSVHighscoreEntry e : CSVHighscoreEntries) {
			numbers += e.playerName + " : " + e.highscore + "\n";
		}
		return numbers;
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
