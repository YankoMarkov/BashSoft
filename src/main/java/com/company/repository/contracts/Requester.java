package com.company.repository.contracts;

import com.company.collections.contracts.SimpleOrderedBag;
import com.company.models.contracts.Course;
import com.company.models.contracts.Student;

import java.util.Comparator;

public interface Requester {

    void getStudentMarksInCourse(String course, String student);

    void getStudentsByCourse(String course);

    SimpleOrderedBag<Course> getAllCoursesSorted(Comparator<Course> comparator);

    SimpleOrderedBag<Student> getAllStudentsSorted(Comparator<Student> comparator);
}
