package com.company.repository.contracts;

public interface FilteredTaker {

    void filterAndTake(String courseName, String filter);

    void filterAndTake(String courseName, String filter, int studentsToTake);
}
