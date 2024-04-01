package cn.tockey.vo;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.jdbc.Null;

import java.util.HashMap;

@Setter
@Getter
public class MyResult<T> {
    private Integer code;
    private String message;
    private T data;
    private HashMap<String, Object> other = new HashMap<>();

    // 构造方法
    public MyResult(){}
    public MyResult(Integer code, String message){
        this.code = code;
        this.message = message;
    }
    public MyResult(Integer code, String message, T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 快捷成功
    public static MyResult<String> ok(String message){
        return new MyResult<>(20000, message);
    }
    public static <T> MyResult<T> ok(String message, T data){
        return new MyResult<>(20000, message, data);
    }
    // 快捷失败
    public static <T> MyResult<T> error(String message){
        return new MyResult<>(0, message);
    }

    // other
    public MyResult<T> put(String key, Object value){
        this.other.put(key, value);
        return this; // 指实例
    }
}