package com.entimo.worklogsync.service;


import com.entimo.worklogsync.oracle.data.PepProject;
import com.entimo.worklogsync.timer.SyncTimer;
import com.entimo.worklogsync.utile.ProjectUtil;
import com.entimo.worklogsync.utile.WorkLogEntry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/")
@PropertySource("classpath:application.yml")
@ConditionalOnExpression("${app.controller.enabled:true}")
public class AppController {

  private int daysToScan = 21;
  private int timerSyncPeriod = 5;
  private boolean syncOncePerDay = false;
  private java.util.Timer syncTimer = new java.util.Timer("SyncTimer");
  private java.util.Timer pingTimer = new java.util.Timer("PingTimer");
  private PostgreSqlService jiraService;
  private OracleService pepService;

  private ProjectUtil projectUtil;

  public AppController(PostgreSqlService jiraService, OracleService pepService, Environment env) {
    this.jiraService = jiraService;
    this.pepService = pepService;
    init(env);
  }

  private void init(Environment env) {

    String o = env.getProperty("timer.syncOncePerDay");
    if (Boolean.parseBoolean(o)) {
      syncOncePerDay = true;
    }
    String period = env.getProperty("timer.timerSyncPeriod");
    if (period != null) {
      try {
        timerSyncPeriod = Integer.parseInt(period);
      } catch (NumberFormatException ex) {
        // nothing to do
      }
    }
    String b = env.getProperty("timer.startTimerAtStartup");
    if (Boolean.parseBoolean(b)) {
      startTimer(timerSyncPeriod);
    }
    String days = env.getProperty("daysToScan");
    if (days != null) {
      try {
        daysToScan = Integer.parseInt(days);
      } catch (NumberFormatException ex) {
        // nothing to do
      }
    }

    // check projects in Jira as well as in PEP and stop app in case of problems
    projectUtil = new ProjectUtil();
    projectUtil.addJiraProject(jiraService.loadProject(ProjectUtil.JIRA_CLASSIC));
    projectUtil.addJiraProject(jiraService.loadProject(ProjectUtil.JIRA_DOCUMENTATION));
    projectUtil.addJiraProject(jiraService.loadProject(ProjectUtil.JIRA_NEXTGEN));
    projectUtil.addPepProject(pepService.loadProject(ProjectUtil.PEP_DARE, ProjectUtil.PEP_DARE_DEV));
    projectUtil.addPepProject(pepService.loadProject(ProjectUtil.PEP_DOCUMENTATION, ProjectUtil.PEP_DOCUMENTATION_USERGUID));
    projectUtil.addPepProject(pepService.loadProject(ProjectUtil.PEP_NEXTGEN, ProjectUtil.PEP_NEXTGEN_DEV));

    startSync(daysToScan, null);
  }

  @Operation(summary = "Start synchronisation from Jira work logs to PEP.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Information about synchronisation process.",
          content = {@Content(mediaType = "application/json")}
      )
  })
  @PutMapping("/startSync")
  public String startSync(@RequestParam(required = false) Integer lastDays,
      @RequestParam(required = false) String user) {
    int d = lastDays == null ? daysToScan : lastDays;
    if (StringUtils.isNotEmpty(user)) {
      log.info("Worklog scan of the last {} days started for {}.", d, user);
    } else {
      log.info("Worklog scan of the last {} days started.", d);
    }
    Map<String, WorkLogEntry> workLogEntries = jiraService.loadWorkLog(d, user);

    List<String> logList = new ArrayList<>();
    workLogEntries.forEach((k, v) -> pepService.processWorkLog(v, logList));
    logList.forEach(log::info);

    return "Found " + workLogEntries.size() + " work log(s) for last " + d + " days.";
  }

  /**
   * just a test
   *
   * @return
   */
  @GetMapping("/")
  public Map<String, Object> greeting() {
    return Collections.singletonMap("message", "Hello, World");
  }

  @Operation(summary = "Get the projects from PEP assigned to user.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found projects for user.",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = PepProject.class))}
      )
  })
  @GetMapping("/userProjects")
  public List<PepProject> userProjects(@RequestParam String persKurz) {
    log.info("load user {} projects from PEP", persKurz);
    return pepService.loadPepProjectsForUser(persKurz);
  }

  @PutMapping("/startTimer")
  public void startTimer(@RequestParam(required = false) Integer period) {
    if (syncOncePerDay) {
      Calendar date = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("UTC")));
      ZonedDateTime now = ZonedDateTime.now();
      date.set(now.getYear(), now.getMonthValue()-1, now.getDayOfMonth());
      date.set(Calendar.HOUR, 23);
      date.set(Calendar.MINUTE, 59);
      date.set(Calendar.SECOND, 59);
      date.set(Calendar.MILLISECOND, 0);
      syncTimer.scheduleAtFixedRate(
          new SyncTimer(this),
          date.getTime(),
          ((long) 1000) * 60 * 60 * 24
      );
      log.info("Timer started with sync period of once per day at 12pm.");
    } else {
      int p = period != null ? period : timerSyncPeriod;
      syncTimer.schedule(
          new SyncTimer(this), ((long) 1000) * 5, ((long) 1000) * 60 * p);
      log.info("Timer started with period of {} minutes", p);
    }
  }

  @PutMapping("/stopTimer")
  public void stopTimer() {
    log.info("Timer stopped");
    syncTimer.cancel();
  }

  public void ping() {
    List<PepProject> rw = userProjects("rw");
    log.info("{} projects loaded for rw.", rw.size());
  }
}
