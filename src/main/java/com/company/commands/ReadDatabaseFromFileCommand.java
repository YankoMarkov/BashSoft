package com.company.commands;

import com.company.annotations.Alias;
import com.company.annotations.Inject;
import com.company.commands.contracts.Executable;
import com.company.exceptions.InvalidInputException;
import com.company.repository.contracts.Database;

@Alias(value = "readdb")
public class ReadDatabaseFromFileCommand extends Command implements Executable {

    @Inject
    private Database studentRepository;

    public ReadDatabaseFromFileCommand(String input, String[] data) {
        super(input, data);
    }

    @Override
    public void execute() throws Exception {
        String[] data = this.getData();
        if (data.length != 2) {
            throw new InvalidInputException(this.getInput());
        }

        String fileName = data[1];
        this.studentRepository.loadData(fileName);
    }
}
