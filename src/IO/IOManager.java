package IO;

import StaticData.ExceptionMessages;
import StaticData.SessionData;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

public class IOManager {

    public static void traverseDirectory(int depth) {
        Queue<File> subFolders = new LinkedList<>();
        String path = SessionData.currentPath;
        int initialIndentation = path.split("\\\\").length;
        File root = new File(path);
        subFolders.add(root);

        while (subFolders.size() != 0) {
            File currentFolder = subFolders.remove();
            int currentIdentation = currentFolder.toString().split("\\\\").length - initialIndentation;
            if (depth - currentIdentation < 0) {
                break;
            }
            OutputWriter.writeMessageOnNewLine(currentFolder.toString());
            if (currentFolder.listFiles() != null) {
                for (File file : currentFolder.listFiles()) {
                    if (file.isDirectory()) {
                        subFolders.add(file);
                    } else {
                        int indexOfLastSlash = file.toString().lastIndexOf("\\");
                        for (int i = 0; i < indexOfLastSlash; i++) {
                            OutputWriter.writeMessage("-");
                        }
                        OutputWriter.writeMessageOnNewLine(file.getName());
                    }
                }
            }
        }
    }

    public static void createDirectoryInCurrentFolder(String name) {
        String path = getCurrentDirectoryPath() + "\\" + name;
        File file = new File(path);
        file.mkdir();
    }

    private static String getCurrentDirectoryPath() {
        String currentPath = SessionData.currentPath;
        return currentPath;
    }

    public static void changeCurrentDirRelativePath(String relativePath) {
        if (relativePath.equals("..")) {
            //go one directory up
            try {
                String currentPath = SessionData.currentPath;
                int indexOfLastSlash = currentPath.lastIndexOf("\\");
                String newPath = currentPath.substring(0, indexOfLastSlash);
                SessionData.currentPath = newPath;
            } catch (StringIndexOutOfBoundsException e) {
                OutputWriter.displayException(ExceptionMessages.INVALID_DESTINATION);
            }
        } else {
            //go to given directory
            String currentPath = SessionData.currentPath;
            currentPath += "\\" + relativePath;
            changeCurrentDirAbsolute(currentPath);
        }
    }

    public static void changeCurrentDirAbsolute(String absolutePath) {
        File file = new File(absolutePath);

        if (!file.exists()) {
            OutputWriter.displayException(ExceptionMessages.FILE_DOES_NOT_EXIST);
            return;
        }
        SessionData.currentPath = absolutePath;
    }
}