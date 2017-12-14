package com.company.commands;

import com.company.annotations.Alias;
import com.company.annotations.Inject;
import com.company.commands.contracts.Executable;
import com.company.exceptions.InvalidInputException;
import com.company.repository.contracts.Database;

@Alias(value = "show")
public class ShowWantedCourseCommand extends Command implements Executable {

    @Inject
    private Database studentRepository;

    public ShowWantedCourseCommand(String input, String[] data) {
        super(input, data);
    }

    @Override
    public void execute() throws Exception {
        String[] data = this.getData();
        if (data.length != 2 && data.length != 3) {
            throw new InvalidInputException(this.getInput());
        }

        if (data.length == 2) {
            String courseName = data[1];
            this.studentRepository.getStudentsByCourse(courseName);
        } else {
            String courseName = data[1];
            String student = data[2];
            this.studentRepository.getStudentMarksInCourse(courseName, student);
        }
    }
}
