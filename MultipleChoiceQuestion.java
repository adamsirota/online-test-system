package onlineTest;

import java.util.Arrays;

public class MultipleChoiceQuestion implements Question {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int questionNumber;
    private String text;
    private double points;
    private String[] answer;

    public MultipleChoiceQuestion(int questionNumber, String text, double points, String[] answer) {
        this.questionNumber = questionNumber;
        this.text = text;
        this.points = points;
        this.answer = answer;
        Arrays.sort(this.answer);
    }

    @Override
    public int getQuestionNumber() {
        return questionNumber;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public double getPoints() {
        return points;
    }

    @Override
    public String getCorrectAnswer() {
        return Arrays.toString(answer);
    }

    @Override
    public double grade(Object studentAnswer) {
        if (studentAnswer instanceof String[]) {
            String[] studentAnswerArray = (String[]) studentAnswer;
            Arrays.sort(studentAnswerArray);
            return Arrays.equals(answer, studentAnswerArray) ? points : 0;
        }
        return 0;
    }
}
