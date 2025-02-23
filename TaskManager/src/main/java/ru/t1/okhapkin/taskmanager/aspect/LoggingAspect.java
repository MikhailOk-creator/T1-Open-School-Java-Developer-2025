package ru.t1.okhapkin.taskmanager.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import ru.t1.okhapkin.taskmanager.entity.Task;

import java.util.Arrays;
import java.util.logging.Logger;

@Aspect
@Component
public class LoggingAspect {

    private final java.util.logging.Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    // Логирование перед выполнением метода
    @Before("execution(* ru.t1.okhapkin.taskmanager.service.*..*(..))")
    public void logBefore(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        logger.info("The method " + className + "." + methodName + " was called with arguments: " + Arrays.toString(args));
    }

    // Логирование после успешного выполнения метода
    @AfterReturning(pointcut = "execution(* ru.t1.okhapkin.taskmanager.service.*..*(..))")
    public void logAfterReturning(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        logger.info("The method " + className + "." + methodName + " was successfully completed");
    }

    // Логирование при создании нового задания
    @AfterReturning(pointcut = "execution(* ru.t1.okhapkin.taskmanager.service.TaskService.createTask(..))", returning = "result")
    public void logAfterCreationNewTask(Task result) {
        logger.info("A new task was created with id: " + result.getId());
    }

    // Логирование при изменении задания
    @AfterReturning(pointcut = "execution(* ru.t1.okhapkin.taskmanager.service.TaskService.updateTask(..))", returning = "result")
    public void logAfterUpdateTask(JoinPoint joinPoint, Task result) {
        Object[] args = joinPoint.getArgs();
        logger.info("The task was updated with id: " + result.getId() + ". Arguments with updates: " + Arrays.toString(args));
    }

    // Логирование при удалении задания
    @AfterReturning(pointcut = "execution(* ru.t1.okhapkin.taskmanager.service.TaskService.deleteTask(..))")
    public void logAfterDeletedTask(JoinPoint joinPoint) {
        logger.info("The task deleted with id: " + Arrays.toString(joinPoint.getArgs()));
    }

    // Логирование при возникновении исключения
    @AfterThrowing(pointcut = "execution(* ru.t1.okhapkin.taskmanager.service.*..*(..))", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        logger.info("The method " + className + "." + methodName + " was completed with error: " + exception.getMessage());
    }

    // Замер времени выполнения метода
    @Around("@annotation(ru.t1.okhapkin.taskmanager.aspect.annotaion.CustomTracking)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        try {
            Object result = joinPoint.proceed(); // Выполнение метода
            long endTime = System.currentTimeMillis();
            logger.info("The method " + className + "." + methodName + " was completed for " + (endTime - startTime) + " ms");
            return result;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            System.out.println("The method " + className + "." + methodName + " was completed with error for " + (endTime - startTime) + " ms");
            throw e;
        }
    }

}
