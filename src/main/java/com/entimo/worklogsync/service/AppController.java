package com.entimo.worklogsync.service;


import com.entimo.worklogsync.timer.SyncTimer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Calendar;

@Controller
@PropertySource("classpath:application.yml")
public class AppController {

    public AppController(Environment env) {
        String b = env.getProperty("startTimerAtStartup");
        if(Boolean.parseBoolean(b)) {
            startTimer();
        }
    }

    private java.util.Timer syncTimer = new java.util.Timer("SyncTimer");

    @Autowired
    private PostgreSqlService jiraService;

    @PutMapping("/startSync")
    public String startSync(@RequestParam Integer lastDays) {
        int d = lastDays == null ? 7 : lastDays;
        return "found work log(s) for last " + lastDays + " days: " + jiraService.loadWorkLog(d);
    }

    @PutMapping("/startTimer")
    public void startTimer() {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.HOUR, 23);
        date.set(Calendar.MINUTE, 59);
        date.set(Calendar.SECOND, 59);
        date.set(Calendar.MILLISECOND, 0);
        syncTimer.schedule(
                new SyncTimer(this),
                date.getTime(),
                1000 * 60 * 60 * 24
        );
    }

    @PutMapping("/stopTimer")
    public void stopTimer() {
        syncTimer.cancel();
    }

}
