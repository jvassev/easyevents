package org.springthing.easyevents;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppContext {

    @Bean
    public ObserverBean observer() {
        return new ObserverBean();
    }

    @Bean
    public DefaultEventPublisher eventPublisher() {
        DefaultEventPublisher ep = new DefaultEventPublisher();
        return ep;
    }

    @Bean
    public ObserverAnnotationBeanPostProcessor beanPostProcessor() {
        ObserverAnnotationBeanPostProcessor postProcessor = new ObserverAnnotationBeanPostProcessor();
        postProcessor.setDefaultEventPublisher(eventPublisher());
        return postProcessor;
    }
}
