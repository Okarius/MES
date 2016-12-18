package Services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * @author Nico
 *
 */

class CSVQuizEntry implements CSVEntry {
	public String id;
	public String question;
	public String answer;

	public CSVQuizEntry(String _id, String _question, String _answer) {
		this.id = _id;
		this.question = _question;
		this.answer = _answer;
	}

	public CSVQuizEntry(String[] line) {
		this.id = line[0];
		this.question = line[1];
		this.answer = line[2];
	}

	public String objectToLine() {
		return this.id + ";" + this.question + ";" + this.answer;
	}

}

public class QuizHandler {
	private ArrayList<CSVQuizEntry> CSVQuizEntries;
	private CSVWorker csvWorker;

	public QuizHandler() {
		csvWorker = new CSVWorker("quiz.csv");
	}

	public void getCSVEntries() {
		CSVQuizEntries = new ArrayList<>();
		ArrayList<String[]> csvLines = csvWorker.readCSV();
		for (String[] l : csvLines) {
			CSVQuizEntry newEntry = new CSVQuizEntry(l);
			CSVQuizEntries.add(newEntry);
		}
	}

	public String getQuestionById(String id) {
		for (CSVQuizEntry e : CSVQuizEntries) {
			if (e.id.equals(id)) {
				return e.question;
			}
		}
		return "Question does not exist";
	}

	public String getAnswerById(String id) {
		for (CSVQuizEntry e : CSVQuizEntries) {
			if (e.id.equals(id)) {
				return e.answer;
			}
		}
		return "Answer does not exist";
	}

	public String newGame() {
		// TODO Auto-generated method stub
		return "NewGameStarted";
	}

}
