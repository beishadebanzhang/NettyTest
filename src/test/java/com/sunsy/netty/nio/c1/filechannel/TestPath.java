package com.sunsy.netty.nio.c1.filechannel;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Path和Paths
 */
public class TestPath {
    public static void main(String[] args) {
        // 获取path实例，支持相对路径绝对路径
        Path path1 = Paths.get("1.txt");
        Path path2 = Paths.get("d:\\data","1.txt");
        // .代表当前路径 ..代表上一级路径
        Path path3 = Paths.get("d:\\data\\projects\\a\\..\\b");
        System.out.println(path3.normalize()); // 正常化路径
    }
}
