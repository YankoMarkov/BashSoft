package com.company.repository;

import com.company.collections.SimpleSortedList;
import com.company.collections.contracts.SimpleOrderedBag;
import com.company.models.contracts.Course;
import com.company.models.contracts.Student;
import com.company.models.BashSoftCourse;
import com.company.models.BashSoftStudent;
import com.company.repository.contracts.DataFilter;
import com.company.repository.contracts.DataSorter;
import com.company.repository.contracts.Database;
import com.company.staticData.ExceptionMessages;
import com.company.io.OutputWriter;
import com.company.staticData.SessionData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentRepository implements Database {
    private boolean isDataInitialized = false;
    private HashMap<String, Course> courses;
    private HashMap<String, Student> students;
    private DataFilter repositoryFilter;
    private DataSorter repositorySorter;

    public StudentRepository(DataFilter repositoryFilter, DataSorter repositorySorter) {
        this.repositoryFilter = repositoryFilter;
        this.repositorySorter = repositorySorter;
    }

    @Override
    public void unloadData(){
        if (!this.isDataInitialized){
            throw new IllegalArgumentException(ExceptionMessages.DATA_NOT_INITIALIZED);
        }

        this.courses = null;
        this.students = null;
        this.isDataInitialized = false;
    }

    @Override
    public void loadData(String fileName){
        if (this.isDataInitialized){
            throw new IllegalArgumentException(ExceptionMessages.DATA_ALREADY_INITIALIZED);
        }

        this.courses = new LinkedHashMap<>();
        this.students = new LinkedHashMap<>();
        try {
            this.readData(fileName);
            this.isDataInitialized = true;
        } catch (IOException e) {
            throw new IllegalArgumentException(ExceptionMessages.CANNOT_ACCESS_FILE);
        }
    }

    @Override
    public void getStudentMarksInCourse(String course, String student){
        if (this.isQueryForStudentPossible(course, student)){
            return;
        }
        double mark = this.courses.get(course).getStudentsByName().get(student).getMarksByCourseName().get(course);
        OutputWriter.displayStudent(student, mark);
    }

    @Override
    public void getStudentsByCourse(String course){
        if (!this.isQueryForCoursePossible(course)){
            return;
        }
        OutputWriter.writeMessageOnNewLine(course + ":");
        for (Map.Entry<String, Student> student : this.courses.get(course).getStudentsByName().entrySet()) {
            this.getStudentMarksInCourse(course, student.getKey());
        }
    }

    @Override
    public SimpleOrderedBag<Course> getAllCoursesSorted(Comparator<Course> comparator) {
        SimpleOrderedBag<Course> sortedCourses = new SimpleSortedList<>(Course.class, comparator);
        sortedCourses.addAll(this.courses.values());
        return sortedCourses;
    }

    @Override
    public SimpleOrderedBag<Student> getAllStudentsSorted(Comparator<Student> comparator) {
        SimpleOrderedBag<Student> sortedStudents = new SimpleSortedList<>(Student.class, comparator);
        sortedStudents.addAll(this.students.values());
        return sortedStudents;
    }

    @Override
    public void filterAndTake(String courseName, String filter) {
        int studentsToTake = this.courses.get(courseName).getStudentsByName().size();
        this.filterAndTake(courseName, filter, studentsToTake);
    }

    @Override
    public void filterAndTake(String courseName, String filter, int studentsToTake) {
        if (!this.isQueryForCoursePossible(courseName)) {
            return;
        }
        HashMap<String, Double> marks = new LinkedHashMap<>();
        for (Map.Entry<String,Student> entry : this.courses.get(courseName).getStudentsByName().entrySet()) {
            marks.put(entry.getKey(), entry.getValue().getMarksByCourseName().get(courseName));
        }
        this.repositoryFilter.printFilteredStudents(marks, filter, studentsToTake);
    }

    @Override
    public void orderAndTake(String courseName, String orderType, int studentsToTake) {
        if (!this.isQueryForCoursePossible(courseName)) {
            return;
        }
        HashMap<String, Double> marks = new LinkedHashMap<>();
        for (Map.Entry<String,Student> entry : this.courses.get(courseName).getStudentsByName().entrySet()) {
            marks.put(entry.getKey(), entry.getValue().getMarksByCourseName().get(courseName));
        }
        this.repositorySorter.printSortedStudents(marks, orderType, studentsToTake);
    }

    @Override
    public void orderAndTake(String courseName, String orderType) {
        int studentsToTake = this.courses.get(courseName).getStudentsByName().size();
        this.orderAndTake(courseName, orderType, studentsToTake);
    }

    private void readData(String fileName) throws IOException {
        String regex = "([A-Z][a-zA-Z#\\+]*_[A-Z][a-z]{2}_\\d{4})\\s+([A-Za-z]+\\d{2}_\\d{2,4})\\s([\\s0-9]+)";
        Pattern pattern = Pattern.compile(regex);

        String path = SessionData.currentPath + "\\" + fileName;
        List<String> lines = Files.readAllLines(Paths.get(path));

        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (line.isEmpty() || !matcher.find()){
                continue;
            }

            String courseName = matcher.group(1);
            String studentName = matcher.group(2);
            String scoresStr = matcher.group(3);
            try{
                int[] scores = Arrays.stream(scoresStr.split("\\s+")).mapToInt(Integer::parseInt).toArray();
                if (Arrays.stream(scores).anyMatch(score -> score > 100 || score < 0)){

                    OutputWriter.displayException(ExceptionMessages.INVALID_SCORE);
                    continue;
                }
                if (scores.length > BashSoftCourse.NUMBER_OF_TASKS_ON_EXAM){
                    OutputWriter.displayException(ExceptionMessages.INVALID_NUMBER_OF_SCORES);
                    continue;
                }
                if (!this.students.containsKey(studentName)){
                    this.students.put(studentName, new BashSoftStudent(studentName));
                }
                if (!this.courses.containsKey(courseName)){
                    this.courses.put(courseName, new BashSoftCourse(courseName));
                }

                Course course = this.courses.get(courseName);
                Student student = this.students.get(studentName);
                student.enrollInCourse(course);
                student.setMarksInCourse(course.getName(), scores);
                course.enrollStudent(student);
            } catch (NumberFormatException e){
                OutputWriter.displayException(e.getMessage());
            }
        }

        OutputWriter.writeMessageOnNewLine("Data read.");
    }

    private boolean isQueryForCoursePossible(String courseName){
        if (!this.isDataInitialized){
            OutputWriter.displayException(ExceptionMessages.DATA_NOT_INITIALIZED);
            return false;
        }
        if (!this.courses.containsKey(courseName)){
            OutputWriter.displayException(ExceptionMessages.NOT_EXISTING_COURSE);
            return false;
        }

        return true;
    }

    private boolean isQueryForStudentPossible(String courseName, String studentName){
        if (!this.isQueryForCoursePossible(courseName)){
            return false;
        }
        if (!this.courses.get(courseName).getStudentsByName().containsKey(studentName)){
            OutputWriter.displayException(ExceptionMessages.NOT_EXISTING_STUDENT);
            return false;
        }

        return true;
    }
}