package Repository;

import IO.OutputWriter;
import StaticData.ExceptionMessages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;

class RepositoryFilters {

    static void printFilteredStudents(HashMap<String, ArrayList<Integer>> courseData, String filterType, Integer numberOfStudents) {
        Predicate<Double> filter = createFilter(filterType);
        if (filter == null) {
            OutputWriter.displayException(ExceptionMessages.INVALID_FILTER);
        }
        int studentCount = 0;
        for (String student : courseData.keySet()) {
            if (studentCount == numberOfStudents) {
                break;
            }
            ArrayList<Integer> studentMarks = courseData.get(student);
            Double averageMark = studentMarks
                    .stream().mapToInt(Integer::valueOf)
                    .average()
                    .getAsDouble();
            Double percentageOfFulfilment = averageMark / 100;
            Double mark = percentageOfFulfilment * 4 + 2;
            if (filter.test(mark)) {
                OutputWriter.printStudents(student, studentMarks);
                studentCount++;
            }
        }
    }

    private static Predicate<Double> createFilter(String filterType) {
        switch (filterType) {
            case "excellent":
                return mark -> mark >= 5.0;
            case "average":
                return mark -> mark >= 3.5 && mark < 5.0;
            case "poor":
                return mark -> mark < 3.5;
            default:
                return null;
        }
    }
}


