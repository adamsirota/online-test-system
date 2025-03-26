package onlineTest;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Student implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
    private Map<Integer, Map<Integer, Object>> answers;

    public Student(String name) {
        this.setName(name);
        this.answers = new HashMap<>();
    }

    public void answerQuestion(int examId, int questionNumber, Object answer) {
        answers.computeIfAbsent(examId, k -> new HashMap<>()).put(questionNumber, answer);
    }

    public double getExamScore(Exam exam) {
        Map<Integer, Object> examAnswers = answers.get(exam.getExamId());
        if (examAnswers == null) {
            return 0;
        }

        double score = 0;
        for (Question question : exam.getQuestions().values()) {
            Object answer = examAnswers.get(question.getQuestionNumber());
            if (answer != null) {
                score += question.grade(answer);
            }
        }
        return score;
    }

    public String getGradingReport(Exam exam) {
        StringBuilder sb = new StringBuilder();
        Map<Integer, Object> examAnswers = answers.get(exam.getExamId());
        if (examAnswers == null) {
            return "";
        }

        double totalScore = 0;
        double totalPoints = 0;
        for (Question question : exam.getQuestions().values()) {
            int questionNumber = question.getQuestionNumber();
            Object answer = examAnswers.get(questionNumber);
            double score = (answer != null) ? question.grade(answer) : 0;
            totalScore += score;
            totalPoints += question.getPoints();

            sb.append(String.format("Question #%d %.1f points out of %.1f\n",
                    questionNumber, score, question.getPoints()));
        }

        sb.append(String.format("Final Score: %.1f out of %.1f", totalScore, totalPoints));
        return sb.toString();
    }

    public double getCourseNumericGrade(Collection<Exam> exams) {
        double totalScore = 0;
        double totalPossible = 0;
        for (Exam exam : exams) {
            totalScore += getExamScore(exam);
            totalPossible += exam.getTotalPoints();
        }
        return (totalPossible > 0) ? (totalScore / totalPossible) * 100 : 0;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}