package com.company.commands;

import com.company.annotations.Alias;
import com.company.annotations.Inject;
import com.company.commands.contracts.Executable;
import com.company.exceptions.InvalidInputException;
import com.company.io.contracts.DirectoryManager;

@Alias(value = "cdabs")
public class ChangeAbsolutePathCommand extends Command implements Executable {

    @Inject
    private DirectoryManager ioManager;

    public ChangeAbsolutePathCommand(String input, String[] data) {
        super(input, data);
    }

    @Override
    public void execute() throws Exception {
        String[] data = this.getData();
        if (data.length != 2) {
            throw new InvalidInputException(this.getInput());
        }

        String absolutePath = data[1];
        this.ioManager.changeCurrentDirAbsolutePath(absolutePath);
    }
}
