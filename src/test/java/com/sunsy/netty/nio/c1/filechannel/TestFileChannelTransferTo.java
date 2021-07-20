package com.sunsy.netty.nio.c1.filechannel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 两个channel间的数据传输
 */
public class TestFileChannelTransferTo {
    public static void main(String[] args) {
        try (
                FileChannel from = new FileInputStream("from.txt").getChannel();
                FileChannel to = new FileOutputStream("to.txt").getChannel();
        ) {
            // 效率高，一次最多传输2G
            for (long left = from.size(); left > 0;) {
                left -= from.transferTo(from.size() - left, from.size(), to);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
