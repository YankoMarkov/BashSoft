package com.company.commands;

import com.company.annotations.Alias;
import com.company.annotations.Inject;
import com.company.io.contracts.DirectoryManager;
import com.company.commands.contracts.Executable;
import com.company.exceptions.InvalidInputException;
import com.company.io.OutputWriter;

@Alias(value = "ls")
public class TraverseFoldersCommand extends Command implements Executable {

    @Inject
    private DirectoryManager ioManager;

    public TraverseFoldersCommand(String input, String[] data) {
        super(input, data);
    }

    @Override
    public void execute() throws Exception {
        String[] data = this.getData();
        if (data.length != 1 && data.length != 2) {
            throw new InvalidInputException(this.getInput());
        }
        String result;

        if (data.length == 1) {
            result = this.ioManager.traverseDirectory(0);
        } else {
            int depth = Integer.parseInt(data[1]);
            result = this.ioManager.traverseDirectory(depth);
        }
        OutputWriter.writeMessage(result);
    }
}
