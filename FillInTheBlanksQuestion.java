package onlineTest;

import java.util.Arrays;

public class FillInTheBlanksQuestion implements Question {
    private int questionNumber;
    private String text;
    private double points;
    private String[] answer;

    public FillInTheBlanksQuestion(int questionNumber, String text, double points, String[] answer) {
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
            int correctAnswers = 0;
            for (String ans : studentAnswerArray) {
                if (Arrays.binarySearch(answer, ans) >= 0) {
                    correctAnswers++;
                }
            }
            return (points / answer.length) * correctAnswers;
        }
        return 0;
    }
}