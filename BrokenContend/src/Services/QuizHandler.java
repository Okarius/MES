package Services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * QuizHandler holds questions and answers for a quiz game and returns them
 * if requested.
 * 
 * @author Nico
 *
 */

class CSVQuizEntry implements CSVEntry {
	public String id;
	public String question;
	public String answerA;
	public String answerB;
	public String answerC;
	public String answerD;
	public String correctAnswer;

	public CSVQuizEntry(String _id, String _question, String _answerA,
			String _answerB, String _answerC, String _answerD, String _correctAnswer) {
		this.id = _id;
		this.question = _question;
		this.answerA = _answerA;
		this.answerB = _answerB;
		this.answerC = _answerC;
		this.answerD = _answerD;
		this.correctAnswer = _correctAnswer;
	}

	public CSVQuizEntry(String[] line) {
		this.id = line[0];
		this.question = line[1];
		this.answerA = line[2];
		this.answerB = line[3];
		this.answerC = line[4];
		this.answerD = line[5];
		this.correctAnswer = line[6];
	}

	public String objectToLine() {
		return this.id + ";" + this.question + ";" + this.correctAnswer;
	}

}

public class QuizHandler {
	private ArrayList<CSVQuizEntry> CSVQuizEntries;
	private CSVWorker csvWorker;

	public QuizHandler() {
		csvWorker = new CSVWorker("quiz.csv");
		CSVQuizEntries = new ArrayList<>();
		ArrayList<String[]> csvLines = csvWorker.readCSV();
		
		for (String[] l : csvLines) {
			CSVQuizEntry newEntry = new CSVQuizEntry(l);
			CSVQuizEntries.add(newEntry);
		}
	}

	/**
	 * 
	 * @param id: id of requested question
	 * @return String
	 */
	public String getQuestionById(String id) {
		for (CSVQuizEntry e : CSVQuizEntries) {
			if (e.id.equals(id)) {
				return e.question;
			}
		}
		return "Question does not exist";
	}
	
	/**
	 * 
	 * @param id: specifies which set of answers is to be returned
	 * @return  answers as String
	 */
	public String getPossibleAnswersById(String id) {
		String answers = "";
		for (CSVQuizEntry e : CSVQuizEntries) {
			if (e.id.equals(id)) {
				answers += ";"+e.answerA + ";"+e.answerB + 
						   ";"+e.answerC +";";
				return answers;
			}
		}
		return "Answers not found";
	}
	
	/**
	 * 
	 * @param id: specifies which answer is requested
	 * @return answer as String
	 */
	
	public String getcorrectAnswerById(String id) {
		for (CSVQuizEntry e : CSVQuizEntries) {
			if (e.id.equals(id)) {
				return e.correctAnswer;
			}
		}
		return "Answer does not exist";
	}

	

}
