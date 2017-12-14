package com.company.commands;

import com.company.annotations.Alias;
import com.company.commands.contracts.Executable;
import com.company.exceptions.InvalidInputException;
import com.company.io.OutputWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Alias(value = "help")
public class GetHelpCommand extends Command implements Executable {
    public GetHelpCommand(String input, String[] data) {
        super(input, data);
    }

    @Override
    public void execute() throws Exception {
        String[] data = this.getData();
        if (data.length != 1) {
            throw new InvalidInputException(this.getInput());
        }

        this.displayHelp();
    }

    private void displayHelp() {
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
            OutputWriter.displayException(e.getMessage());
        }
    }
}
