package IO;

import java.util.List;

public class OutputWriter {

    public static void writeMessage(String message) {
        System.out.print(message);
    }

    public static void writeMessageOnNewLine(String message) {
        System.out.println(message);
    }

    public static void writeEmptyLine() {
        System.out.println();
    }

    public static void displayException(String message) {
        System.out.println(message);
    }

    public static void printStudents(String names, List<Integer> marks) {
        String output = String.format("%s - %s", names, marks.toString());
        writeMessageOnNewLine(output);
    }
}
