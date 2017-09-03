package IO;

import Judge.Tester;
import Network.DowloadManager;
import Repository.StudentsRepository;
import StaticData.ExceptionMessages;
import StaticData.SessionData;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CommandInterpreter {

    public static void interpreterCommand(String input) {
        String[] data = input.split("\\s+");
        String command = data[0];

        switch (command) {
            case "open":
                tryOpenFile(input, data);
                break;
            case "mkdir":
                tryCreateDirectory(input, data);
                break;
            case "ls":
                tryTraversFolder(input, data);
                break;
            case "cmp":
                tryCompareFiles(input, data);
                break;
            case "changeDirRel":
                tryChangeRelativePath(input, data);
                break;
            case "changeDirAbs":
                tryChangeAbsolutePath(input, data);
                break;
            case "readDb":
                tryReadDatabaseFromFile(input, data);
                break;
            case "filter":
                tryPrintFilteredStudents(input, data);
                break;
            case "order":
                tryPrintOrderedStudents(input, data);
                break;
            case "download":
                tryDownloadFile(input, data);
                break;
            case "downloadAsynch":
                tryDownloadFileOnNewThread(input, data);
                break;
            case "help":
                getHelp();
                break;
            default:
                displayInvalidCommandMessage(input);
                break;
        }
    }

    private static void displayInvalidCommandMessage(String input) {
        String output = String.format("The command '%s' is invalid", input);
        OutputWriter.writeMessage(output);
    }

    private static void tryOpenFile(String input, String[] data) {
        if (data.length != 2) {
            displayInvalidCommandMessage(input);
            return;
        }
        String fileName = data[1];
        String filePath = SessionData.currentPath + "\\" + fileName;
        File file = new File(filePath);
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    private static void tryCreateDirectory(String input, String[] data) {
        if (data.length != 2) {
            displayInvalidCommandMessage(input);
            return;
        }
        String folderName = data[1];
        IOManager.createDirectoryInCurrentFolder(folderName);
    }

    private static void tryTraversFolder(String input, String[] data) {
        if (data.length != 1 && data.length != 2) {
            displayInvalidCommandMessage(input);
            return;
        }
        if (data.length == 1) {
            IOManager.traverseDirectory(0);
        }
        if (data.length == 2) {
            int depth = Integer.valueOf(data[1]);
            IOManager.traverseDirectory(depth);
        }
    }

    private static void tryCompareFiles(String input, String[] data) {
        if (data.length != 3) {
            displayInvalidCommandMessage(input);
            return;
        }
        String firstPath = data[1];
        String secondPath = data[2];
        Tester.compareContent(firstPath, secondPath);
    }

    private static void tryChangeRelativePath(String input, String[] data) {
        if (data.length != 2) {
            displayInvalidCommandMessage(input);
            return;
        }
        String relativePath = data[1];
        IOManager.changeCurrentDirRelativePath(relativePath);
    }

    private static void tryChangeAbsolutePath(String input, String[] data) {
        if (data.length != 2) {
            displayInvalidCommandMessage(input);
            return;
        }
        String absolutePath = data[1];
        IOManager.changeCurrentDirAbsolute(absolutePath);
    }

    private static void tryReadDatabaseFromFile(String input, String[] data) {
        if (data.length != 2) {
            displayInvalidCommandMessage(input);
            return;
        }
        String fileName = data[1];
        StudentsRepository.initializeData(fileName);
    }

    private static void tryPrintFilteredStudents(String input, String[] data) {
        if (data.length != 3 || data.length != 4) {
            displayInvalidCommandMessage(input);
            return;
        }
        String course = data[1];
        String filter = data[2];
        if (data.length == 3) {
            StudentsRepository.printFilteredStudents(course, filter, null);
            return;
        }
        Integer numberOfStudent = Integer.valueOf(data[3]);
        if (data.length == 4) {
            StudentsRepository.printFilteredStudents(course, filter, numberOfStudent);
        }
    }

    private static void tryPrintOrderedStudents(String input, String[] data) {
        if (data.length != 3 || data.length != 4) {
            displayInvalidCommandMessage(input);
            return;
        }
        String course = data[1];
        String compareType = data[2];
        if (data.length == 3) {
            StudentsRepository.printOrderedStudents(course, compareType, null);
            return;
        }
        Integer numberOfStudent = Integer.valueOf(data[3]);
        if (data.length == 4) {
            StudentsRepository.printOrderedStudents(course, compareType, numberOfStudent);
        }
    }

    private static void getHelp() {
        try (BufferedReader reader = new BufferedReader(new FileReader("resources\\getHelp.txt"))) {

            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                OutputWriter.writeMessageOnNewLine(line);
            }
            OutputWriter.writeEmptyLine();
        } catch (IOException e) {
            OutputWriter.displayException(ExceptionMessages.FILE_NOT_FOUND);
        }
    }

    private static void tryDownloadFile(String input, String[] data) {
        if (data.length != 2) {
            displayInvalidCommandMessage(input);
            return;
        }
        String fileUrl = data[1];
        DowloadManager.download(fileUrl);
    }

    private static void tryDownloadFileOnNewThread(String input, String[] data) {
        if (data.length != 2) {
            displayInvalidCommandMessage(input);
            return;
        }
        String fileUrl = data[1];
        DowloadManager.downloadOnNewThread(fileUrl);
    }
}
