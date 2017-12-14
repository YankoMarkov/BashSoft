package com.company.models.contracts;

import java.util.HashMap;

public interface Course extends Comparable<Course> {

    int NUMBER_OF_TASKS_ON_EXAM = 5;
    int MAX_SCORE_ON_EXAM_TASK = 100;

    String getName();

    HashMap<String, Student> getStudentsByName();

    void enrollStudent(Student student);
}
