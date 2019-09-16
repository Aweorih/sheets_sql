package com.aweorih.sheets_sql.google_handler.existing;

import com.aweorih.sheets_sql.utils.IOExceptionWrapper.IOExceptionRunnable;
import com.aweorih.sheets_sql.utils.TimeService;
import com.aweorih.sheets_sql.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PushExecuter {

  private static final Logger LOGGER = LogManager.getLogger(PushExecuter.class);

  private final List<Long> requestTimeouts = Arrays.asList(
    0L,
    TimeService.getSecondsInMillis(1),
    TimeService.getSecondsInMillis(5),
    TimeService.getSecondsInMillis(10),
    TimeService.getSecondsInMillis(30),
    TimeService.getMinutesInMillis(1),
    TimeService.getMinutesInMillis(5),
    TimeService.getMinutesInMillis(10)
  );

  private       long    lastExecutionTime     = 0;
  private       int     requestTimeoutPointer = 0;
  private       boolean isInMaxTimeout        = false;
  private final int     totalRequests;
  private       int     finishedExecutions    = 0;

  public PushExecuter(int totalRequests) {
    this.totalRequests = totalRequests;
  }

  public boolean push(IOExceptionRunnable runnable) {

    long waitInterval = requestTimeouts.get(requestTimeoutPointer);

    if (0 != waitInterval) Utils.sleep(waitInterval, getClass());

    boolean success = false;

    try {
      LOGGER.info(
        "start of google request execution... ({} of {})",
        finishedExecutions + 1,
        totalRequests
      );
      runnable.run();
      decrementTimeout();
      success = true;
    } catch (IOException e) {

      LOGGER.error(e);
      incrementTimeout();
    }

    LOGGER.info(
      "finished google request execution! success: {} ({} of {})",
      success,
      finishedExecutions + 1,
      totalRequests
    );
    lastExecutionTime = System.currentTimeMillis();

    if (success) ++finishedExecutions;


    LOGGER.info("finished slack request!");

    return success;
  }

  private boolean incrementTimeout() {

    long    oldTimeout = requestTimeouts.get(requestTimeoutPointer);
    boolean response   = true;

    if (requestTimeoutPointer >= requestTimeouts.size() - 1) {
      requestTimeoutPointer = requestTimeouts.size() - 1;
      response = false;
    } else if (requestTimeoutPointer == 0) {
      requestTimeoutPointer = 1;
    } else {
      requestTimeoutPointer++;
    }

    long newTimeout = requestTimeouts.get(requestTimeoutPointer);

    LOGGER.info("incremented push waiting interval from {} to {}", oldTimeout, newTimeout);

    return response;
  }

  private void decrementTimeout() {

    long oldTimeout = requestTimeouts.get(requestTimeoutPointer);

    isInMaxTimeout = false;
    if (requestTimeoutPointer >= requestTimeouts.size() - 1) {
      requestTimeoutPointer = requestTimeouts.size() - 2;
    } else if (requestTimeoutPointer > 0) {
      requestTimeoutPointer--;
    }

    long newTimeout = requestTimeouts.get(requestTimeoutPointer);

    if (oldTimeout == newTimeout) return;

    LOGGER.info("decremented push waiting interval from {}ms to {}ms", oldTimeout, newTimeout);
  }
}
