package com.mxch.imgreconsturct.config;

import com.mxch.imgreconsturct.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.mxch.imgreconsturct.util.ResultCodeEnum.SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class WebException {
    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e) {
        log.error(e.toString(), e);
        return Result.build(null,SERVER_ERROR);
    }
}
