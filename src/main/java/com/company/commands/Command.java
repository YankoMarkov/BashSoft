package com.company.commands;

import com.company.commands.contracts.Executable;
import com.company.exceptions.InvalidInputException;

public abstract class Command implements Executable{
    private String input;
    private String[] data;

    public Command(String input, String[] data) {
        this.setInput(input);
        this.setData(data);
    }

    String getInput() {
        return input;
    }

    private void setInput(String input) {
        if (input == null || input.trim().length() == 0) {
            throw new InvalidInputException(input);
        }
        this.input = input;
    }

    String[] getData() {
        return data;
    }

    private void setData(String[] data) {
        if (data == null || data.length < 1) {
            throw new InvalidInputException(this.getInput());
        }
        this.data = data;
    }

    public abstract void execute() throws Exception;
}
