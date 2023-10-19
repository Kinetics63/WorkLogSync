package com.entimo.worklogsync.timer;

import com.entimo.worklogsync.service.AppController;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.TimerTask;

@Log4j2
public class SyncTimer  extends TimerTask {

    private final AppController controller;

    public SyncTimer(AppController appController) {
        this.controller = appController;
    }

    public void run() {
        log.info("Timer controlled sync starts now!");
        controller.startSync(null);
    }
}
