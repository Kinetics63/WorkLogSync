/*
 * <copyright>Entimo AG, Germany</copyright>
 */
package com.entimo.worklogsync.exception;

/**
 * The type WorkLog scan  exception.
 */
public class WorkLogSyncException extends RuntimeException {

  /**
   * Instantiates a new WorkLog scan exception.
   *
   * @param msg   the msg
   * @param cause the cause
   */
  public WorkLogSyncException(String msg, Throwable cause) {
    super(msg, cause);
  }

  /**
   * Instantiates a new WorkLog scan exception.
   *
   * @param msg   the msg
   */
  public WorkLogSyncException(String msg) {
    super(msg);
  }
}
