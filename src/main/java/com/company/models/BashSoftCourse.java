package com.company.models;

import com.company.models.contracts.Course;
import com.company.models.contracts.Student;
import com.company.exceptions.DuplicateEntryInStructureException;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class BashSoftCourse implements Course {

    private String name;
    private HashMap<String, Student> studentsByName;

    public BashSoftCourse(String name) {
        this.name = name;
        this.studentsByName = new LinkedHashMap<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public HashMap<String, Student> getStudentsByName() {
        return studentsByName;
    }

    public void enrollStudent(Student student){
        if (this.studentsByName.containsKey(student.getUserName())){
            throw new DuplicateEntryInStructureException(student.getUserName(), this.name);
        }

        this.studentsByName.put(student.getUserName(), student);
    }

    @Override
    public int compareTo(Course other) {
        return this.getName().compareTo(other.getName());
    }

    @Override
    public String toString() {
        return this.getName();
    }
}