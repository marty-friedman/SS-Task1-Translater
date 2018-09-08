package com.egrasoft.ss.translater.service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManagerService {
    public String getContent(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        char[] charContent = new char[(int) file.length()];
        fileReader.read(charContent);
        fileReader.close();
        return new String(charContent);
    }

    public void saveContent(File file, String content) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(content);
        fileWriter.flush();
        fileWriter.close();
    }

    public static FileManagerService getInstance() {
        return SingletonHelper.instance;
    }

    private FileManagerService() {}

    private static class SingletonHelper {
        private static final FileManagerService instance = new FileManagerService();
    }
}
