package com.company.io;

import com.company.io.contracts.Interpreter;
import com.company.io.contracts.Reader;
import com.company.staticData.SessionData;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class InputReader implements Reader {
    private final String END_COMMAND = "quit";
    private Interpreter commandInterpreter;

    public InputReader(Interpreter commandInterpreter) {
        this.commandInterpreter = commandInterpreter;
    }

    public void readCommands() throws Exception {

        BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            OutputWriter.writeMessage(String.format("%s>", SessionData.currentPath));
            String input = scanner.readLine().trim();
            if (input.equals(END_COMMAND)){
                break;
            }

            this.commandInterpreter.interpretCommand(input);
        }

        for (Thread thread : SessionData.threadPool) {
            thread.join();
        }
    }
}
