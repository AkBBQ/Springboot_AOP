package com.sun.springboot_aop.service;

import com.google.gson.Gson;
import com.sun.springboot_aop.common.Result;
import com.sun.springboot_aop.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * 切面
 *
 * @Aspect 这个注解的作用是:将一个类定义为一个切面类
 * @Component 这个注解的作用：把切面类加入到IOC容器中
 * @Order(1) 这个注解的作用是:标记切面类的处理优先级,i值越小,优先级别越高.PS:可以注解类,也能注解到方法上
 * @author sunjie
 */
@Aspect
@Component
@Slf4j
@Order(1)
public class AspectDemo {

    @Autowired
    private Gson gson;

    /**
     * execution (* com.sample.service.impl..*. *(..))
     *
     * 1、execution(): 表达式主体。
     *
     * 2、第一个*号：表示返回类型， *号表示所有的类型。
     *
     * 3、包名：表示需要拦截的包名，后面的两个句点表示当前包和当前包的所有子包，com.sample.service.impl包、子孙包下所有类的方法。
     *
     * 4、第二个*号：表示类名，*号表示所有的类。
     *
     * 5、*(..):最后这个星号表示方法名，*号表示所有的方法，后面括弧里面表示方法的参数，两个句点表示任何参数
     *
     */



    /**
     * 声明一个切点 表示作用域
     */
    @Pointcut("execution(* com.sun.springboot_aop..*.*(..))")
    private void hello(){

    }

    /**
     * 声明一个切点 表示作用域
     */
    @Pointcut("execution(String com.sun.springboot_aop..*.*(..))")
    private void hello2(){

    }

    /**
     *  前置通知，方法调用前被调用
     *  在切点前执行方法,作用域为指定的切点
     */
    @Before(value = "hello()")
    public void methodBefore(JoinPoint joinPoint){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        javax.servlet.http.HttpServletRequest request = requestAttributes.getRequest();

        //可以获取到请求中的session信息
        User user = (User)request.getSession().getAttribute("sss");


        //打印请求内容
        log.info("========================前置通知开始======================");
        log.info("请求地址:" + request.getRequestURI());
        log.info("请求方式" + request.getMethod());
        log.info("请求类方法" + joinPoint.getSignature());
        log.info("请求类方法参数" + Arrays.toString(joinPoint.getArgs()));
        log.info("========================前置通知结束======================");


    }

//    /**
//     * 前置通知，方法调用前被调用
//     * 在切点前执行方法,作用域为指定的切点
//     */
//    @Before(value = "hello2()")
//    public void methodBefore2(JoinPoint joinPoint){
//        log.info("========================请求内容3333======================");
//    }

    /**
     * 环绕型通知
     */
    @Around(value = "hello()")
    public Object methodAround(ProceedingJoinPoint point){
        log.info("========================环绕型通知开始======================");
        Object result = null;
        try {
            //执行Advice方法
            result = point.proceed();

        } catch (Throwable throwable) {
            log.info("环绕型通知捕获了异常");
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();
            HttpServletResponse response = requestAttributes.getResponse();
            //response.sendRedirect("templates/err.html");
            return "对不起";

        }
        log.info("========================环绕型通知结束======================");
        return result;
    }


    /**
     *   后置通知
     *   returning：限定了只有目标方法返回值与通知方法相应参数类型时才能执行后置返回通知，否则不执行，
     *   对于returning对应的通知方法参数为Object类型将匹配任何目标返回值
     *
     */
    @AfterReturning(returning = "s",pointcut = "hello()")
    public void methodAfterRunning(String s){
        log.info("--------------后置通知开始----------------");
        log.info("Response内容:" + gson.toJson(s));
        log.info("--------------后置通知结束----------------");

    }

    /**
     * 后置异常通知@AfterThrowing：在方法抛出异常退出时执行的通知。
     * @param joinPoint
     * @param throwable
     */
    @AfterThrowing(value = "hello()",throwing = "throwable")
    public void doAfterThrowingAdvice(JoinPoint joinPoint,Throwable throwable){
        if(Objects.nonNull(throwable)){
            log.info("捕获到异常");
        }
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();

        //跳转到出错页面
        try {
            assert response != null;
            response.sendRedirect("templates/err.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}