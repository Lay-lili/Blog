package com.lili.blog.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.jar.Attributes;


@Aspect
@Component
//@Slf4j
public class LogAspect {  //日志切面

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //以web包下的所有类的所有方法为切面
    @Pointcut("execution(* com.lili.blog.web.*.*(..))")
    public void log(){}

    //在切面之前处理
    @Before("log()")
    public  void doBfore(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        String ip  = request.getRemoteAddr();
        //获取类名和方法名
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        //获取请求参数
        Object[] args =  joinPoint.getArgs();

        RequestLog requestLog = new RequestLog(url, ip, classMethod, args);

        logger.info("requestLog : {}", requestLog);
    }

    //在切面之后处理
    @After("log()")
    public  void doAFter(){
        logger.info("-----doAfter------");
    }

    //在切面时等业务方法执行完成进行处理,此方法在After之后
    @AfterReturning(returning = "result", pointcut = "log()")
    public void  doAfterReturn(Object result){
        logger.info("result : {}", result);
    }

    //内部类
    private class RequestLog{
        private String url;
        private String ip;
        private String classMethods;
        private Object[] args;

        public RequestLog(String url, String ip, String classMethods, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethods = classMethods;
            this.args = args;
        }

        @Override
        public String toString() {
            return "RequestLog{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethods='" + classMethods + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}
