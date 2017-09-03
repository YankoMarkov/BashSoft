package Repository;

import IO.OutputWriter;
import StaticData.ExceptionMessages;
import StaticData.SessionData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentsRepository {
    private static boolean isDataInitialized = false;
    private static HashMap<String, HashMap<String, ArrayList<Integer>>> studentsByCourse;

    public static void initializeData(String fileName) {
        if (isDataInitialized) {
            System.out.println(ExceptionMessages.DATA_ALREADY_INITIALIZED);
            return;
        }
        studentsByCourse = new HashMap<>();
        try {
            readData(fileName);
            isDataInitialized = true;
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    private static void readData(String fileName) throws IOException {
        String path = SessionData.currentPath + "\\resources\\" + fileName;
        List<String> lines = Files.readAllLines(Paths.get(path));

        for (String line : lines) {
            String[] tokens = line.split("\\s+");
            String course = tokens[0];
            String student = tokens[1];
            int mark = Integer.valueOf(tokens[2]);
            if (!studentsByCourse.containsKey(course)) {
                studentsByCourse.put(course, new HashMap<>());
            }
            if (!studentsByCourse.get(course).containsKey(student)) {
                studentsByCourse.get(course).put(student, new ArrayList<>());
            }
            studentsByCourse.get(course).get(student).add(mark);
        }
        isDataInitialized = true;
        OutputWriter.writeMessageOnNewLine("Data read.");
    }

    private static boolean isQueryForCoursePossible(String courseName) {
        if (!isDataInitialized) {
            OutputWriter.displayException(ExceptionMessages.DATA_NOT_INITIALIZED);
            return false;
        }
        if (!studentsByCourse.containsKey(courseName)) {
            OutputWriter.displayException(ExceptionMessages.NON_EXISTENT_COURSE);
            return false;
        }
        return true;
    }

    private static boolean isQueryForStudentPossible(String courseName, String studentName) {
        if (!isQueryForCoursePossible(courseName)) {
            return false;
        }
        if (!studentsByCourse.get(courseName).containsKey(studentName)) {
            OutputWriter.displayException(ExceptionMessages.NON_EXISTING_STUDENT);
            return false;
        }
        return true;
    }

    public static void getStudentMarksInCourse(String course, String student) {
        if (!isQueryForStudentPossible(course, student)) {
            return;
        }
        ArrayList<Integer> marks = studentsByCourse.get(course).get(student);
        OutputWriter.printStudents(student, marks);
    }

    public static void getStudentByCourse(String course) {
        if (!isQueryForCoursePossible(course)) {
            return;
        }
        OutputWriter.writeMessageOnNewLine(course + ":");
        for (Map.Entry<String, ArrayList<Integer>> student : studentsByCourse.get(course).entrySet()) {
            OutputWriter.printStudents(student.getKey(), student.getValue());
        }
    }
    public static void printFilteredStudents(String course, String filter, Integer numberOfStudents) {
        if (!isQueryForCoursePossible(course)) {
            return;
        }
        if (numberOfStudents == null) {
            numberOfStudents = studentsByCourse.get(course).size();
        }
        RepositoryFilters.printFilteredStudents(studentsByCourse.get(course), filter, numberOfStudents);
    }

    public static void printOrderedStudents(String course, String compareType, Integer numberOfStudents){
        if (!isQueryForCoursePossible(course)) {
            return;
        }
        if (numberOfStudents == null) {
            numberOfStudents = studentsByCourse.get(course).size();
        }
        RepositorySorters.printSortedStudents(studentsByCourse.get(course), compareType, numberOfStudents);
    }
}