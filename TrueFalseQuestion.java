package onlineTest;

public class TrueFalseQuestion implements Question {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int questionNumber;
    private String text;
    private double points;
    private boolean answer;

    public TrueFalseQuestion(int questionNumber, String text, double points, boolean answer) {
        this.questionNumber = questionNumber;
        this.text = text;
        this.points = points;
        this.answer = answer;
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
        return answer ? "True" : "False";
    }

    @Override
    public double grade(Object studentAnswer) {
        if (studentAnswer instanceof Boolean) {
            return ((Boolean) studentAnswer) == answer ? points : 0;
        }
        return 0;
    }
}
