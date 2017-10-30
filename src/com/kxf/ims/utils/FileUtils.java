package com.kxf.ims.utils;

import java.io.File;
import java.io.IOException;

public class FileUtils {
	public static void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }

    public static void creatFile(File file) {
        if (!file.getParentFile().exists()) {
            makeDir(file.getParentFile());
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
