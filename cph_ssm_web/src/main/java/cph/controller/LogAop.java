package cph.controller;

import cph.domain.SysLog;
import cph.service.ISysLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Component
@Aspect
public class LogAop {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ISysLogService sysLogService;

    private Date startTime;//开始时间
    private Class executionClass;//访问的类
    private Method executionMethod;//访问的方法

    @Before("execution(* cph.controller.*.*(..))")
    public void doBefore(JoinPoint joinPoint) throws NoSuchMethodException {
        startTime = new Date();//访问时间
        //获取访问的类
        executionClass = joinPoint.getTarget().getClass();

        //获取访问的方法
        String methodName = joinPoint.getSignature().getName();//访问方法的名字

        Object[] args = joinPoint.getArgs();//获取访问方法的参数
        if(args == null || args.length ==0 )//若无参
        {
            executionMethod = executionClass.getMethod(methodName);
        }else {
            //有参数，则遍历args中所有元素，获取对应的class，装入到class[]中
            Class[] classArgs = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                classArgs[i] = args[i].getClass();//参数需要是对象，把int改成Integer
            }
           executionMethod = executionClass.getMethod(methodName,classArgs);
        }

    }



    @After("execution(* cph.controller.*.*(..))")
    public void doAfter(JoinPoint joinPoint) throws Exception {
        long time = new Date().getTime() - startTime.getTime();//获取访问时长

        String url = "";
        //获取url(通过反射)
        if(executionClass != null && executionMethod != null && executionClass != LogAop.class){
            //1.获取类上的注解的value
            RequestMapping classAnnotation = (RequestMapping) executionClass.getAnnotation(RequestMapping.class);
            if(classAnnotation != null){
                String[] calssValue = classAnnotation.value();
                //2.获取方法上的注解的value
                RequestMapping methodAnnotation = executionMethod.getAnnotation(RequestMapping.class);
                if(methodAnnotation != null){
                    String[] methodValue = methodAnnotation.value();
                    url = calssValue[0] + methodValue[0];
                }
            }
        }


        //获取ip
        String ip = request.getRemoteAddr();

        //获取当前操作的用户（用户由spring-security控制）
        SecurityContext context = SecurityContextHolder.getContext();//从上下文获取当前登录的用户
        User user = (User) context.getAuthentication().getPrincipal();
        String username = user.getUsername();


        //将日志相关信息封装到SysLog对象中
        SysLog sysLog = new SysLog();
        sysLog.setExecutionTime(time);
        sysLog.setIp(ip);
        sysLog.setUrl(url);
        sysLog.setUsername(username);
        sysLog.setVisitTime(startTime);
        sysLog.setMethod("[类名] " + executionClass.getName() + "[方法名] " + executionMethod.getName());

        //调用service方法完成操作
        sysLogService.save(sysLog);

    }


}
