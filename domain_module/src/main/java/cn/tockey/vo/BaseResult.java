package cn.tockey.vo;


import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 桐叔
 * @email liangtong@itcast.cn
 */
@Setter
public class BaseResult<T> {

    //成功状态码
    public static final int OK = 20000;
    //失败状态码
    public static final int ERROR = 0;

    //返回码
    private Integer code;
    //返回消息
    private String message;

    //存放数据
    private T data;
    //其他数据
    private Map<String,Object> other = new HashMap<>();

    public BaseResult() {

    }

    public BaseResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    public BaseResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 快捷成功BaseResult对象
     * @param message
     * @return
     */
    public static BaseResult ok(String message){
        return new BaseResult<>(BaseResult.OK , message);
    }

    public static BaseResult ok(String message, Object data){
        return new BaseResult<>(BaseResult.OK , message, data );
    }

    /**
     * 快捷失败BaseResult对象
     * @param message
     * @return
     */
    public static BaseResult error(String message){
        return new BaseResult(BaseResult.ERROR , message);
    }

    /**
     * 自定义数据区域
     * @param key
     * @param msg
     * @return
     */
    public BaseResult append(String key , Object msg){
        other.put(key , msg);
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public Map<String, Object> getOther() {
        return other;
    }
}