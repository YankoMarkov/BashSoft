package com.company.commands;

import com.company.annotations.Alias;
import com.company.annotations.Inject;
import com.company.commands.contracts.Executable;
import com.company.exceptions.InvalidInputException;
import com.company.repository.contracts.Database;
import com.company.staticData.ExceptionMessages;

@Alias(value = "order")
public class PrintOrderedStudentsCommand extends Command implements Executable {

    @Inject
    private Database studentRepository;

    public PrintOrderedStudentsCommand(String input, String[] data) {
        super(input, data);
    }

    @Override
    public void execute() throws Exception {
        String[] data = this.getData();
        if (data.length != 5) {
            throw new InvalidInputException(this.getInput());
        }

        String courseName = data[1];
        String orderType = data[2].toLowerCase();
        String takeCommand = data[3].toLowerCase();
        String takeQuantity = data[4].toLowerCase();

        this.tryParseParametersForOrder(takeCommand, takeQuantity, courseName, orderType);
    }

    private void tryParseParametersForOrder(
            String takeCommand, String takeQuantity,
            String courseName, String orderType) {
        if (!takeCommand.equals("take")) {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_TAKE_COMMAND);
        }

        if (takeQuantity.equals("all")) {
            this.studentRepository.orderAndTake(courseName, orderType);
            return;
        }

        try {
            int studentsToTake = Integer.parseInt(takeQuantity);
            this.studentRepository.orderAndTake(courseName, orderType, studentsToTake);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_TAKE_QUANTITY_PARAMETER);
        }
    }
}
