package com.tamizh.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class FileUtils {

    public static FileReader getReader(String filePath) throws FileNotFoundException {
        return new FileReader(filePath);
    }
}
