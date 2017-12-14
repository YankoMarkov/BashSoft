package com.company.commands;

import com.company.annotations.Alias;
import com.company.annotations.Inject;
import com.company.commands.contracts.Executable;
import com.company.exceptions.InvalidInputException;
import com.company.repository.contracts.Database;
import com.company.staticData.ExceptionMessages;

@Alias(value = "filter")
public class PrintFilteredStudentsCommand extends Command implements Executable {

    @Inject
    private Database studentRepository;

    public PrintFilteredStudentsCommand(String input, String[] data) {
        super(input, data);
    }

    @Override
    public void execute() throws Exception {
        String[] data = this.getData();
        if (data.length != 5) {
            throw new InvalidInputException(this.getInput());
        }

        String course = data[1];
        String filter = data[2].toLowerCase();
        String takeCommand = data[3].toLowerCase();
        String takeQuantity = data[4].toLowerCase();

        this.tryParseParametersForFilter(takeCommand, takeQuantity, course, filter);
    }

    private void tryParseParametersForFilter(
            String takeCommand, String takeQuantity,
            String courseName, String filter) {
        if (!takeCommand.equals("take")) {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_TAKE_COMMAND);
        }

        if (takeQuantity.equals("all")) {
            this.studentRepository.filterAndTake(courseName, filter);
            return;
        }

        try {
            int studentsToTake = Integer.parseInt(takeQuantity);
            this.studentRepository.filterAndTake(courseName, filter, studentsToTake);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_TAKE_QUANTITY_PARAMETER);
        }
    }
}
