package com.company.network.contracts;

public interface AsynchDownloader extends Downloader {
    void downloadOnNewThread(String fileUrl);
}
