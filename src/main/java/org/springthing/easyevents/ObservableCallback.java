package org.springthing.easyevents;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ObservableCallback {
    private final Class<?> eventClass;
    private final Method method;
    private final Object bean;

    public ObservableCallback(Method method, Object bean) {
        this.eventClass = method.getParameterTypes()[0];
        this.method = method;
        this.bean = bean;
    }

    public void invoke(Object event) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (canHandle(event)) {
            method.invoke(bean, new Object[] { event });
        }
    }

    public boolean canHandle(Object event) {
        return eventClass.isAssignableFrom(event.getClass());
    }
}
