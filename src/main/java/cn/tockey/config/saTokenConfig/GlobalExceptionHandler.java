package cn.tockey.config.saTokenConfig;

import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.util.SaResult;
import cn.tockey.vo.BaseResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 全局异常处理
public class GlobalExceptionHandler {
    // 全局异常拦截
    //@ExceptionHandler
    //public BaseResult<String> handlerException(Exception e) {
    //    //e.printStackTrace();
    //    System.out.println("全局异常拦截：" + e);
    //    return BaseResult.error(e.getMessage());
    //}

    // 此方法和上面的方法区别在于，这里是拦截 SaTokenException 异常
    @ExceptionHandler(SaTokenException.class)
    public BaseResult handlerSaTokenException(SaTokenException e) {

        System.out.println("SaTokenException 异常拦截：" + e.getCode());
        // 根据不同异常细分状态码返回不同的提示
        //switch (e.getCode()) {
        //    case 11011:
        //        return new BaseResult(11011, e.getMessage()); // 未能读取到有效Token
        //    case 11012:
        //        return
        //}

        return new BaseResult(e.getCode(), e.getMessage());
    }
}
