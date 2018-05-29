package com.dominator.AAAconfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 注意：该线程池被所有的异步任务共享，而不属于某一个异步任务 
 * 描述：配置异步任务的线程池 
 * @author chhliu 
 * 创建时间：2017年5月22日 上午10:20:56 
 * @version 1.2.0 
 */  
@Slf4j
@Configuration
public class AsyncTaskExecutePool implements AsyncConfigurer {
    
    @Autowired
    private TaskThreadPoolConfig config;  // 配置属性类，见上面的代码  
    
    @Override  
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(config.getCorePoolSize());    
        executor.setMaxPoolSize(config.getMaxPoolSize());    
        executor.setQueueCapacity(config.getQueueCapacity());    
        executor.setKeepAliveSeconds(config.getKeepAliveSeconds());    
        executor.setThreadNamePrefix("taskExecutor-");
    
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务    
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行    
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();    
        return executor;    
    }  
  
    @Override  
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {// 异步任务中异常处理
        return new AsyncUncaughtExceptionHandler() {  
            @Override
            public void handleUncaughtException(Throwable arg0, Method arg1, Object... arg2) {
                log.error("=========================="+arg0.getMessage()+"=======================", arg0);  
                log.error("exception method:"+arg1.getName());  
            }  
        };  
    }    
}  