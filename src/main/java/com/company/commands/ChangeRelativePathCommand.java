package com.company.commands;

import com.company.annotations.Alias;
import com.company.annotations.Inject;
import com.company.io.contracts.DirectoryManager;
import com.company.commands.contracts.Executable;
import com.company.exceptions.InvalidInputException;

@Alias(value = "cdrel")
public class ChangeRelativePathCommand extends Command implements Executable {

    @Inject
    private DirectoryManager ioManager;

    public ChangeRelativePathCommand(String input, String[] data) {
        super(input, data);
    }

    @Override
    public void execute() throws Exception {
        String[] data = this.getData();
        if (data.length != 2) {
            throw new InvalidInputException(this.getInput());
        }

        String relativePath = data[1];
        this.ioManager.changeCurrentDirRelativePath(relativePath);
    }
}
