package com.egrasoft.ss.translater.service;

import java.io.File;
import java.io.IOException;

public interface FileManagerService {
    String getContent(File file) throws IOException;
}
