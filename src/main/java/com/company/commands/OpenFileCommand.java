package com.company.commands;

import com.company.annotations.Alias;
import com.company.commands.contracts.Executable;
import com.company.exceptions.InvalidInputException;
import com.company.staticData.SessionData;

import java.awt.*;
import java.io.File;

@Alias(value = "open")
public class OpenFileCommand extends Command implements Executable {

    public OpenFileCommand(String input, String[] data) {
        super(input, data);
    }

    @Override
    public void execute() throws Exception {
        String[] data = this.getData();
        if (data.length != 2) {
            throw new InvalidInputException(this.getInput());
        }

        String fileName = data[1];
        String filePath = SessionData.currentPath + "\\" + fileName;
        File file = new File(filePath);
        Desktop.getDesktop().open(file);
    }
}
