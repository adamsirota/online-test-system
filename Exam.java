package onlineTest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Exam implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int examId;
    private String title;
    private Map<Integer, Question> questions;

    public Exam(int examId, String title) {
        this.examId = examId;
        this.setTitle(title);
        this.questions = new HashMap<>();
    }

    public void addQuestion(Question question) {
        questions.put(question.getQuestionNumber(), question);
    }

    public Question getQuestion(int questionNumber) {
        return questions.get(questionNumber);
    }

    public String getKey() {
        StringBuilder sb = new StringBuilder();
        for (Question question : questions.values()) {
            sb.append("Question Text: ").append(question.getText()).append("\n");
            sb.append("Points: ").append(question.getPoints()).append("\n");
            sb.append("Correct Answer: ").append(question.getCorrectAnswer()).append("\n\n");
        }
        return sb.toString();
    }

    public double getTotalPoints() {
        return questions.values().stream().mapToDouble(Question::getPoints).sum();
    }

    public Map<Integer, Question> getQuestions() {
        return questions;
    }

    public int getExamId() {
        return this.examId;
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}