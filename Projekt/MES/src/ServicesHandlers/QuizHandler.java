package ServicesHandlers;

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


class CSVQuizEntry {
	public String id;
	public String question;
	public String answer;

	public CSVQuizEntry(String _id, String _question, String _answer) {
		this.id = _id;
		this.question = _question;
		this.answer = _answer;
	}
	public CSVQuizEntry(String line) {
		String[] unparsedEntry = line.split(";");
		assert unparsedEntry.length == 7;
		this.id = unparsedEntry[0];
		this.question = unparsedEntry[1];
		this.answer = unparsedEntry[2];
	}
}


public class QuizHandler {
	private ArrayList<CSVQuizEntry> CSVQuizEntries;

	public QuizHandler() {
		CSVQuizEntries = new ArrayList<CSVQuizEntry>();
		String path = "quiz.csv";
		String line = "";
		String cvsSplitBy = ";";
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			while ((line = br.readLine()) != null) {
				String[] lineArray = line.split(cvsSplitBy);
				System.out.println(line);
				CSVQuizEntries.add(new CSVQuizEntry(lineArray[0], lineArray[1], lineArray[2]));
			}
			br.close(); 	//obacht
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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

class quizEntry{
	
}
