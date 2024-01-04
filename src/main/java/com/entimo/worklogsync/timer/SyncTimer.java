package com.entimo.worklogsync.timer;

import com.entimo.worklogsync.service.AppController;
import lombok.extern.slf4j.Slf4j;

import java.util.TimerTask;

@Slf4j
public class SyncTimer  extends TimerTask {

    private final AppController controller;

    public SyncTimer(AppController appController) {
        this.controller = appController;
    }

    public void run() {
        log.info("Timer controlled sync starts now!");
        controller.startSync(null, null);
    }
}
