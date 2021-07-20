package com.sunsy.netty.nio.c1.filechannel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 多级目录拷贝
 */
public class TestFileCopy {
    public static void main(String[] args) throws Exception {
        String targetPath = "static/target";
        String sourcePath = "static/source";
        Files.walk(Paths.get(sourcePath)).forEach(path -> {
            String targetName = path.toString().replace(sourcePath, targetPath);
            try {
                if (Files.isDirectory(path)) {
                    Files.createDirectory(Paths.get(targetName));
                } else if (Files.isRegularFile(path)) {
                    Files.copy(path, Paths.get(targetName));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }
}
