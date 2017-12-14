package com.company.commands;

import com.company.annotations.Alias;
import com.company.annotations.Inject;
import com.company.judge.contracts.ContentComparer;
import com.company.commands.contracts.Executable;
import com.company.exceptions.InvalidInputException;

@Alias(value = "cmp")
public class CompareFilesCommand extends Command implements Executable {

    @Inject
    private ContentComparer tester;

    public CompareFilesCommand(String input, String[] data) {
        super(input, data);
    }

    @Override
    public void execute() throws Exception {
        String[] data = this.getData();
        if (data.length != 3){
            throw new InvalidInputException(this.getInput());
        }

        String firstPath = data[1];
        String secondPath = data[2];
        this.tester.compareContent(firstPath, secondPath);
    }
}
