package com.company.commands;

import com.company.annotations.Alias;
import com.company.annotations.Inject;
import com.company.commands.contracts.Executable;
import com.company.exceptions.InvalidInputException;
import com.company.io.OutputWriter;
import com.company.repository.contracts.Database;

@Alias(value = "dropdb")
public class DropDatabaseCommand extends Command implements Executable {

    @Inject
    private Database studentRepository;

    public DropDatabaseCommand(String input, String[] data) {
        super(input, data);
    }

    @Override
    public void execute() throws Exception {
        String[] data = this.getData();
        if (data.length != 1) {
            throw new InvalidInputException(this.getInput());
        }

        this.studentRepository.unloadData();
        OutputWriter.writeMessageOnNewLine("Database dropped!");
    }
}
