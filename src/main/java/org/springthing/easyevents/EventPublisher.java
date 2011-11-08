package org.springthing.easyevents;

public interface EventPublisher {
    public <T> void publish(T event);
}
