package cc.sends.pray.config;

import cc.sends.pray.result.Result;
import cc.sends.pray.result.ResultFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * HTTP 请求方式不支持异常
     * HttpRequestMethodNotSupportedException
     *
     * @return {@link Result}
     */
    @ExceptionHandler(value = Exception.class)
    public Result httpRequestMethodNotSupportException(Exception e) {
        return ResultFactory.buildResult(403, "请求方式异常", "");
    }
}