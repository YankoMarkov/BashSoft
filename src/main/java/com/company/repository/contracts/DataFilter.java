package com.company.repository.contracts;

import java.util.HashMap;

public interface DataFilter {

    void printFilteredStudents(HashMap<String, Double> courseData, String filterType, int numberOfStudents);
}
