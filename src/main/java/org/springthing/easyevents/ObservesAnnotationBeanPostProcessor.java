package org.springthing.easyevents;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.MethodCallback;

public class ObservesAnnotationBeanPostProcessor implements BeanPostProcessor, Ordered, ApplicationContextAware,
        ApplicationListener<ApplicationContextEvent> {

    private final List<ObservableCallback> callbacks;

    private ApplicationContext applicationContext;

    private DefaultEventPublisher defaultEventPublisher;

    public ObservesAnnotationBeanPostProcessor() {
        callbacks = new ArrayList<ObservableCallback>();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        ReflectionUtils.doWithMethods(targetClass, new MethodCallback() {
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                Observer annotation = AnnotationUtils.getAnnotation(method, Observer.class);
                if (annotation != null && Modifier.isPublic(method.getModifiers())) {
                    Assert.isTrue(void.class.equals(method.getReturnType()),
                            "Only void-returning methods may be annotated with @Observes.");
                    Assert.isTrue(method.getParameterTypes().length == 1,
                            "Only 1-arg methods may be annotated with @Observes.");

                    method.setAccessible(true);
                    ObservableCallback callback = new ObservableCallback(method, bean);
                    callbacks.add(callback);
                }
            }
        });

        return bean;
    }

    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        if (event.getApplicationContext() == this.applicationContext) {
            if (event instanceof ContextRefreshedEvent) {
                defaultEventPublisher.setCallbacks(callbacks);
            }
        }
    }

    public void setDefaultEventPublisher(DefaultEventPublisher eventPublisherImpl) {
        this.defaultEventPublisher = eventPublisherImpl;
    }
}
