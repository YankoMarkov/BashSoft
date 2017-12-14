package com.company;

import com.company.judge.contracts.ContentComparer;
import com.company.io.contracts.DirectoryManager;
import com.company.io.contracts.Interpreter;
import com.company.io.contracts.Reader;
import com.company.io.CommandInterpreter;
import com.company.io.IOManager;
import com.company.io.InputReader;
import com.company.io.OutputWriter;
import com.company.judge.Tester;
import com.company.network.DownloadManager;
import com.company.network.contracts.AsynchDownloader;
import com.company.repository.RepositoryFilter;
import com.company.repository.RepositorySorter;
import com.company.repository.StudentRepository;
import com.company.repository.contracts.DataFilter;
import com.company.repository.contracts.DataSorter;
import com.company.repository.contracts.Database;

public class Main {

    public static void main(String[] args) {

        ContentComparer tester = new Tester();
        AsynchDownloader downloadManager = new DownloadManager();
        DirectoryManager ioManager = new IOManager();
        DataSorter sorter = new RepositorySorter();
        DataFilter filter = new RepositoryFilter();
        Database studentRepository = new StudentRepository(filter, sorter);
        Interpreter commandInterpreter = new CommandInterpreter(tester, studentRepository, downloadManager, ioManager);
        Reader inputReader = new InputReader(commandInterpreter);

        try {
            inputReader.readCommands();
        } catch (Exception e) {
            OutputWriter.displayException(e.getMessage());
        }
    }
}
