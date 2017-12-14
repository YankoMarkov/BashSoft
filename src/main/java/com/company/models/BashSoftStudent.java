package com.company.models;

import com.company.models.contracts.Course;
import com.company.models.contracts.Student;
import com.company.exceptions.DuplicateEntryInStructureException;
import com.company.exceptions.InvalidStringException;
import com.company.exceptions.KeyNotFoundException;
import com.company.staticData.ExceptionMessages;

import java.util.*;

public class BashSoftStudent implements Student {
    private String userName;
    private HashMap<String, Course> enrolledCourses;
    private HashMap<String, Double> marksByCourseName;

    public BashSoftStudent(String userName) {
        this.setUserName(userName);
        this.enrolledCourses = new LinkedHashMap<>();
        this.marksByCourseName = new LinkedHashMap<>();
    }

    @Override
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        if (userName == null || userName.trim().length() == 0){
            throw new InvalidStringException();
        }
        this.userName = userName;
    }

    public Map<String, Course> getEnrolledCourses() {
        return Collections.unmodifiableMap(enrolledCourses);
    }

    @Override
    public Map<String, Double> getMarksByCourseName() {
        return Collections.unmodifiableMap(marksByCourseName);
    }

    @Override
    public void enrollInCourse(Course course){
        if (this.enrolledCourses.containsKey(course.getName())){
            throw new DuplicateEntryInStructureException(this.getUserName(), course.getName());
        }

        this.enrolledCourses.put(course.getName(), course);
    }

    @Override
    public void setMarksInCourse(String courseName, int ... scores){
        if (!this.enrolledCourses.containsKey(courseName)){
            throw new KeyNotFoundException();
        }
        if (scores.length > Course.NUMBER_OF_TASKS_ON_EXAM){
            throw new IllegalArgumentException(ExceptionMessages.INVALID_NUMBER_OF_SCORES);
        }

        double mark = this.calculateMark(scores);
        this.marksByCourseName.put(courseName, mark);
    }

    @Override
    public int compareTo(Student other) {
        return this.getUserName().compareTo(other.getUserName());
    }

    @Override
    public String toString() {
        return this.getUserName();
    }

    private double calculateMark(int[] scores) {
        double percentageOfSolvedExam = Arrays.stream(scores).sum() /
                (double) (Course.NUMBER_OF_TASKS_ON_EXAM * Course.MAX_SCORE_ON_EXAM_TASK);

        return (percentageOfSolvedExam * 4) + 2;
    }
}
