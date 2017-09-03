package Judge;

import IO.OutputWriter;
import StaticData.ExceptionMessages;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Tester {

    public static void compareContent(String actualOutput, String expectedOutput) {
        try {
            OutputWriter.writeMessageOnNewLine("Reading files...");
            String mismatchPath = getMismatchPath(expectedOutput);
            List<String> actualOutputString = readTextFile(actualOutput);
            List<String> expectedOutputString = readTextFile(expectedOutput);
            boolean mismatch = compareStrings(actualOutputString, expectedOutputString, mismatchPath);

            if (mismatch) {
                List<String> mismatchString = readTextFile(mismatchPath);
                mismatchString.forEach(OutputWriter::writeMessageOnNewLine);
            } else {
                OutputWriter.writeMessageOnNewLine("Files are identical. There are no mismatches.");
            }
        } catch (IOException e) {
            OutputWriter.displayException(ExceptionMessages.FILE_NOT_FOUND);
        }
    }

    private static String getMismatchPath(String expectedOutput) {
        int index = expectedOutput.lastIndexOf("\\");
        String directoryPath = expectedOutput.substring(0, index);
        return directoryPath + "\\mismatch.txt";
    }

    private static List<String> readTextFile(String filePath) throws IOException {
        List<String> text = new ArrayList<>();
        File file = new File(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                text.add(line);
            }
        }
        return text;
    }

    private static boolean compareStrings(List<String> actualOutputString, List<String> expectedOutputString, String mismatchPath) {
        OutputWriter.writeMessageOnNewLine("Comparing files...");
        boolean isMismatch = false;

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(mismatchPath));
            int maxLength = Math.max(actualOutputString.size(), expectedOutputString.size());
            for (int i = 0; i < maxLength; i++) {
                String actualLine = actualOutputString.get(i);
                String expectedLine = expectedOutputString.get(i);
                if (!actualLine.equals(expectedLine)) {
                    writer.write(String.format("mismatch -> expected{%s}, actual{%s}\n", expectedLine, actualLine));
                    isMismatch = true;
                } else {
                    writer.write(String.format("Line match -> %s\n", actualLine));
                }
            }
            writer.close();
        } catch (IOException e) {
            isMismatch = true;
            OutputWriter.displayException(ExceptionMessages.CANNOT_ACCESS_FILE);
        } catch (IndexOutOfBoundsException e) {
            isMismatch = true;
            OutputWriter.displayException(ExceptionMessages.INVALID_OUTPUT_LENGTH);
        }
        return isMismatch;
    }
}
