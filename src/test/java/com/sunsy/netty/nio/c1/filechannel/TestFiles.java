package com.sunsy.netty.nio.c1.filechannel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Files类
 */
public class TestFiles {
    public static void main(String[] args) throws IOException {
        // 检查文件是否存在
        Path path = Paths.get("data.txt");
        System.out.println(Files.exists(path));

        // 创建目录
        // 创建一级目录，已存在则抛出异常
        Path path2 = Paths.get("static/test");
        Files.createDirectory(path2);
        // 创建多级目录，已存在则抛出异常
        Path path3 = Paths.get("static/test/tst/st");
        Files.createDirectories(path2);

        // 文件拷贝, 效率高
        Path source = Paths.get("from.txt");
        Path target = Paths.get("to.txt");
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        // 移动文件
        Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);
        // 删除文件，文件不存在则抛出异常, 删除目录有内容抛出异常
        Files.delete(source);
    }
}
