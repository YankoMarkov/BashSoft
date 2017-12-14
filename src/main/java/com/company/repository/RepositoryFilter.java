package com.company.repository;

import com.company.repository.contracts.DataFilter;
import com.company.staticData.ExceptionMessages;
import com.company.io.OutputWriter;

import java.util.HashMap;
import java.util.function.Predicate;

public class RepositoryFilter implements DataFilter {
    public void printFilteredStudents(HashMap<String, Double> courseData, String filterType, int numberOfStudents){
        Predicate<Double> filter = createPredicate(filterType);

        if (filter == null) {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_FILTER);
        }

        filterAndTake(filter, courseData, numberOfStudents);
    }

    private void filterAndTake(Predicate<Double> filter, HashMap<String, Double> courseData, int numberOfStudents){
        int studentsCount = 0;
        for (String student : courseData.keySet()) {
            if (studentsCount >= numberOfStudents){
                break;
            }

            double mark = courseData.get(student);
            if (filter.test(mark)){
                OutputWriter.displayStudent(student, mark);
                studentsCount++;
            }
        }
    }

    private Predicate<Double> createPredicate(String filterType){
        switch (filterType){
            case "excellent":
                return mark -> mark >= 5.0;

            case "average":
                return mark -> mark >= 3.50 && mark < 5.0;

            case "poor":
                return mark -> mark < 3.5;

            default:
                return null;
        }
    }
}
