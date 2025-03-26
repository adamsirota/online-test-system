package onlineTest;

import java.io.*;
import java.util.*;

public class SystemManager implements Manager, Serializable {
    private static final long serialVersionUID = 1L;
    private Map<Integer, Exam> exams;
    private Map<String, Student> students;
    private String[] letterGrades;
    private double[] cutoffs;

    public SystemManager() {
        exams = new HashMap<>();
        students = new HashMap<>();
    }

    @Override
    public boolean addExam(int examId, String title) {
        if (exams.containsKey(examId)) {
            return false;
        }
        exams.put(examId, new Exam(examId, title));
        return true;
    }

    @Override
    public void addTrueFalseQuestion(int examId, int questionNumber, String text, double points, boolean answer) {
        Exam exam = exams.get(examId);
        if (exam != null) {
            exam.addQuestion(new TrueFalseQuestion(questionNumber, text, points, answer));
        }
    }

    @Override
    public void addMultipleChoiceQuestion(int examId, int questionNumber, String text, double points, String[] answer) {
        Exam exam = exams.get(examId);
        if (exam != null) {
            exam.addQuestion(new MultipleChoiceQuestion(questionNumber, text, points, answer));
        }
    }

    @Override
    public void addFillInTheBlanksQuestion(int examId, int questionNumber, String text, double points, String[] answer) {
        Exam exam = exams.get(examId);
        if (exam != null) {
            exam.addQuestion(new FillInTheBlanksQuestion(questionNumber, text, points, answer));
        }
    }

    @Override
    public String getKey(int examId) {
        Exam exam = exams.get(examId);
        if (exam == null) {
            return "Exam not found";
        }
        return exam.getKey();
    }

    @Override
    public boolean addStudent(String name) {
        if (students.containsKey(name)) {
            return false;
        }
        students.put(name, new Student(name));
        return true;
    }

    @Override
    public void answerTrueFalseQuestion(String studentName, int examId, int questionNumber, boolean answer) {
        Student student = students.get(studentName);
        Exam exam = exams.get(examId);
        if (student != null && exam != null) {
            Question question = exam.getQuestion(questionNumber);
            if (question instanceof TrueFalseQuestion) {
                student.answerQuestion(examId, questionNumber, answer);
            }
        }
    }

    @Override
    public void answerMultipleChoiceQuestion(String studentName, int examId, int questionNumber, String[] answer) {
        Student student = students.get(studentName);
        Exam exam = exams.get(examId);
        if (student != null && exam != null) {
            Question question = exam.getQuestion(questionNumber);
            if (question instanceof MultipleChoiceQuestion) {
                student.answerQuestion(examId, questionNumber, answer);
            }
        }
    }

    @Override
    public void answerFillInTheBlanksQuestion(String studentName, int examId, int questionNumber, String[] answer) {
        Student student = students.get(studentName);
        Exam exam = exams.get(examId);
        if (student != null && exam != null) {
            Question question = exam.getQuestion(questionNumber);
            if (question instanceof FillInTheBlanksQuestion) {
                student.answerQuestion(examId, questionNumber, answer);
            }
        }
    }

    @Override
    public double getExamScore(String studentName, int examId) {
        Student student = students.get(studentName);
        Exam exam = exams.get(examId);
        if (student != null && exam != null) {
            return student.getExamScore(exam);
        }
        return 0;
    }

    @Override
    public String getGradingReport(String studentName, int examId) {
        Student student = students.get(studentName);
        Exam exam = exams.get(examId);
        if (student != null && exam != null) {
            return student.getGradingReport(exam);
        }
        return "";
    }

    @Override
    public void setLetterGradesCutoffs(String[] letterGrades, double[] cutoffs) {
        this.letterGrades = letterGrades;
        this.cutoffs = cutoffs;
    }

    @Override
    public double getCourseNumericGrade(String studentName) {
        Student student = students.get(studentName);
        if (student == null) {
            return 0;
        }
        
        double totalScore = 0;
        double totalPoints = 0;
        for (Exam exam : exams.values()) {
            double examScore = student.getExamScore(exam);
            double examTotalPoints = exam.getTotalPoints();
            totalScore += (examScore / examTotalPoints) * 100;
            totalPoints += 100;
        }
        
        return totalPoints > 0 ? totalScore / exams.size() : 0;
    }

    @Override
    public String getCourseLetterGrade(String studentName) {
        double numericGrade = getCourseNumericGrade(studentName);
        System.out.println("Numeric grade for " + studentName + ": " + numericGrade);
        for (int i = 0; i < cutoffs.length; i++) {
            if (numericGrade >= cutoffs[i]) {
                return letterGrades[i];
            }
        }
        return letterGrades[letterGrades.length - 1];
    }

    @Override
    public String getCourseGrades() {
        StringBuilder sb = new StringBuilder();
        List<String> sortedNames = new ArrayList<>(students.keySet());
        Collections.sort(sortedNames);
        for (String name : sortedNames) {
            double numericGrade = getCourseNumericGrade(name);
            String letterGrade = getCourseLetterGrade(name);
            sb.append(name).append(" ")
              .append(String.format("%.1f", numericGrade)).append(" ")
              .append(letterGrade).append("\n");
        }
        return sb.toString().trim();
    }
    
    @Override
    public double getMaxScore(int examId) {
        Exam exam = exams.get(examId);
        if (exam != null) {
            return students.values().stream()
                    .mapToDouble(student -> student.getExamScore(exam))
                    .max()
                    .orElse(0);
        }
        return 0;
    }
    
    @Override
    public double getMinScore(int examId) {
        Exam exam = exams.get(examId);
        if (exam != null) {
            return students.values().stream()
                    .mapToDouble(student -> student.getExamScore(exam))
                    .min()
                    .orElse(0);
        }
        return 0;
    }


    @Override
    public double getAverageScore(int examId) {
        Exam exam = exams.get(examId);
        if (exam != null) {
            return students.values().stream()
                    .mapToDouble(student -> student.getExamScore(exam))
                    .average()
                    .orElse(0);
        }
        return 0;
    }

    @Override
    public void saveManager(Manager manager, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(manager);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Manager restoreManager(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (Manager) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}