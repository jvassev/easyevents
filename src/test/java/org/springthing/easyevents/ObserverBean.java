package org.springthing.easyevents;

import org.springframework.context.ApplicationEvent;

class ObserverBean {
    MyEvent my;
    YourEvent your;
    OurEvent our;
    ApplicationEvent app;

    @Observer
    public void onMyEvent(MyEvent my) {
        this.my = my;
    }

    @Observer
    public void onYourEvent(YourEvent your) {
        this.your = your;
    }

    @Observer
    public void onOurEvent(OurEvent our) {
        this.our = our;
    }

    @Observer
    public void onAppEvent(ApplicationEvent event) {
        this.app = event;
    }
}