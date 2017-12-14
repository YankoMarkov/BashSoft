package com.company.io;

import com.company.annotations.Alias;
import com.company.annotations.Inject;
import com.company.judge.contracts.ContentComparer;
import com.company.io.contracts.DirectoryManager;
import com.company.commands.contracts.Executable;
import com.company.io.contracts.Interpreter;
import com.company.network.contracts.AsynchDownloader;
import com.company.repository.contracts.Database;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class CommandInterpreter implements Interpreter{

    private static final String COMMANDS_LOCATION = "src/main/java/com/company/commands/";
    private static final String COMMANDS_PACKAGE = "com.company.commands.";

    private ContentComparer tester;
    private Database studentRepository;
    private AsynchDownloader downloadManager;
    private DirectoryManager ioManager;

    public CommandInterpreter(ContentComparer tester,
                              Database studentRepository,
                              AsynchDownloader downloadManager,
                              DirectoryManager ioManager) {
        this.tester = tester;
        this.studentRepository = studentRepository;
        this.downloadManager = downloadManager;
        this.ioManager = ioManager;
    }

    public void interpretCommand(String input){
        String[] data = input.split("\\s+");
        String commandName = data[0].toLowerCase();
        try{
            Executable command = parseCommand(input, data, commandName);
            command.execute();
        } catch (Throwable ex){
            OutputWriter.displayException(ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private Executable parseCommand(String input, String[] data, String command) {
        File commandsFolder = new File(COMMANDS_LOCATION);
        Executable executable = null;

        for (File file : commandsFolder.listFiles()) {
            if (!file.isFile() || !file.getName().endsWith(".java")){
                continue;
            }

            try {
                String fileName = file.getName().substring(0, file.getName().lastIndexOf('.'));
                Class<Executable> executableClass = (Class<Executable>) Class.forName(COMMANDS_PACKAGE + fileName);
                if (!executableClass.isAnnotationPresent(Alias.class)){
                    continue;
                }

                Alias alias = executableClass.getAnnotation(Alias.class);
                if (!alias.value().equalsIgnoreCase(command)){
                    continue;
                }

                Constructor executableCtor = executableClass.getConstructor(String.class, String[].class);
                executable = (Executable) executableCtor.newInstance(input, data);
                this.injectDependencies(executable, executableClass);

            } catch (ReflectiveOperationException ex){
                ex.printStackTrace();
            }
        }
        return executable;
    }

    private void injectDependencies(Executable executable, Class<Executable> executableClass) throws IllegalAccessException {
        Field[] fields = executableClass.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Inject.class)){
                continue;
            }

            field.setAccessible(true);
            Field[] theseFields = CommandInterpreter.class.getDeclaredFields();

            for (Field thisField : theseFields) {
                if (!thisField.getType().equals(field.getType())){
                    continue;
                }

                thisField.setAccessible(true);
                field.set(executable, thisField.get(this));
            }
        }
    }
}
