package cn.tockey.utils;

import java.util.concurrent.atomic.AtomicLong;

public class UserIdGenerator {  
    // 原子长整型来确保线程安全地生成ID
    //private static AtomicLong counter = new AtomicLong(12);
    // 前缀
    private static final String PREFIX = "U";  
    // 填充字符，用于确保ID长度一致  
    private static final char PADDING_CHAR = '0';  
    // 生成的ID的总长度（包括前缀和填充）  
    private static final int TOTAL_LENGTH = 7;  
    // 前缀后的数字部分长度  
    private static final int NUMBER_LENGTH = TOTAL_LENGTH - PREFIX.length();  
  
    public static synchronized String generateUserId(int n) {
        AtomicLong counter = new AtomicLong(n);
        long nextId = counter.incrementAndGet();  
        // 格式化ID，确保它是固定长度的  
        String formattedId = String.format("%0" + (NUMBER_LENGTH) + "d", nextId);  
        return PREFIX + formattedId;  
    }  
  
    //public static void main(String[] args) {
    //    // 测试生成用户ID
    //    for (int i = 0; i < 10; i++) {
    //        System.out.println(generateUserId());
    //    }
    //}


}