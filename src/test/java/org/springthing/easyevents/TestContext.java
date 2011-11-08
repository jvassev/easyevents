package org.springthing.easyevents;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestContext {

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
    public ObservesAnnotationBeanPostProcessor beanPostProcessor() {
        ObservesAnnotationBeanPostProcessor postProcessor = new ObservesAnnotationBeanPostProcessor();
        postProcessor.setDefaultEventPublisher(eventPublisher());
        return postProcessor;
    }
}
