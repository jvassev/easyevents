package org.springthing.easyevents;

import java.util.List;
import java.util.concurrent.Executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

public class DefaultEventPublisher implements EventPublisher, ApplicationListener<ApplicationEvent> {
    
    private static final Log logger = LogFactory.getLog(DefaultEventPublisher.class);
    
    private Executor taskExecutor;

    private List<ObservableCallback> callbacks;

    public void setCallbacks(List<ObservableCallback> callbacks) {
        this.callbacks = callbacks;
    }

    public void setTaskExecutor(Executor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    public void publish(final Object event) {
        Executor executor = getTaskExecutor();
        if (executor != null) {
            for (final ObservableCallback cb : callbacks) {
                if (cb.canHandle(event)) {
                    executor.execute(new Runnable() {
                        public void run() {
                            executeCallback(event, cb);
                        }
                    });
                }
            }
        } else {
            for (ObservableCallback cb : callbacks) {
                executeCallback(event, cb);
            }
        }
    }

    private void executeCallback(final Object event, final ObservableCallback cb)  {
        try {
            cb.invoke(event);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public Executor getTaskExecutor() {
        return taskExecutor;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (callbacks != null) {
            publish(event);
        }
    }
}
