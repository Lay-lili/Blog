package com.lili.blog;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//返回的状态码
@ResponseStatus(HttpStatus.NOT_FOUND)
//访问资源找不到异常
public class  NotFoundException  extends RuntimeException{

    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
