package onlineTest;

import java.io.Serializable;

public interface Question extends Serializable {
    int getQuestionNumber();
    String getText();
    double getPoints();
    String getCorrectAnswer();
    double grade(Object answer);
}