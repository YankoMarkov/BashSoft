package com.company.commands;

import com.company.annotations.Alias;
import com.company.annotations.Inject;
import com.company.commands.contracts.Executable;
import com.company.network.contracts.AsynchDownloader;

@Alias(value = "download")
public class DownloadFileCommand extends Command implements Executable {

    @Inject
    private AsynchDownloader downloadManager;

    public DownloadFileCommand(String input, String[] data) {
        super(input, data);
    }

    @Override
    public void execute() throws Exception {
        String[] data = this.getData();
        if (data.length != 2) {
            throw new IllegalArgumentException(this.getInput());
        }
        String fileUrl = data[1];
        this.downloadManager.download(fileUrl);
    }
}
