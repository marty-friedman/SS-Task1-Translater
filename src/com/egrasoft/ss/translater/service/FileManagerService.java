package com.egrasoft.ss.translater.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileManagerService {
    public String getContent(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        char[] charContent = new char[(int) file.length()];
        fileReader.read(charContent);
        return new String(charContent);
    }

    public static FileManagerService getInstance() {
        return SingletonHelper.instance;
    }

    private static class SingletonHelper {
        private static final FileManagerService instance = new FileManagerService();
    }
}
