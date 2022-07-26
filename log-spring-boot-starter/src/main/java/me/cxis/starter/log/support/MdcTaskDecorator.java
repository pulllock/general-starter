package me.cxis.starter.log.support;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * MDC任务装饰器，将MDC上下文传递到子线程中去。目前主要的功能有：
 * - 为了将MDC上下文中的TraceId传递到子线程中
 */
public class MdcTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                }

                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
