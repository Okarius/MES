package Services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 
 * @author Nico
 *
 */

public class QuizService extends Service {

	private QuizHandler quizHandler;
	private UserHandler userHandler;

	public int highscore;
	int gameId;
	int currentQuestionId;

	public QuizService(int _id, String _name) {
		super(_id, _name);
		quizHandler = new QuizHandler();
		userHandler = new UserHandler();
		highscore = 0;
		gameId = 0;
		currentQuestionId = 0;
	}

	@Override
	public byte[] getAnswer(String payload) {

		String[] payloadArray = this.getArgumentsArray(payload);
		String msg = "";

		switch (payloadArray[0]) {
		case "0": // answer
			if (payloadArray.length < 2) {
				msg = "No answer";
			} else {
				String correctAnswer = quizHandler.getcorrectAnswerById(Integer.toString(currentQuestionId));
				String userAnswer = payloadArray[1];
				if (correctAnswer.equals(userAnswer)) {
					highscore++;
				}
				msg = correctAnswer;
				currentQuestionId++;
			}
			break;

		case "1": // end game
			if (payloadArray.length < 2) {
				msg = "No username";
			} else {
				msg = userHandler.setGameEntry(highscore, payloadArray[1], gameId);
			}
			break;

		case "2": // request highscore
			msg = userHandler.getHighscoreList();
			break;

		case "3": // new game
			gameId++; // reset values
			highscore = 0;
			currentQuestionId = 0;
			msg = "1";
			break;
		case "4": // get next question
			String question = quizHandler.getQuestionById(Integer.toString(currentQuestionId));
			String possibleAnswers = quizHandler.getPossibleAnswersById(Integer.toString(currentQuestionId));
			msg = question + possibleAnswers;
			break;
		case "5":
			printHighScore();
			msg ="Done";
		default:
			msg = "Illegal arguments for Service Quiz";
			break;

		}
		debugMsg = msg;
		return msg.getBytes();

	}

	private void printHighScore() {
		QRCode codeMaker = new QRCode();
		String qrCodeData = "Hello World!";
		String filePath = "QRCode.png";
		String charset = "UTF-8"; // or "ISO-8859-1"
		Map hintMap = new HashMap();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

		try {
			codeMaker.createQRCode(qrCodeData, filePath, charset, hintMap, 200, 200);
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
