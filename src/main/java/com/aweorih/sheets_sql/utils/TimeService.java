package com.aweorih.sheets_sql.utils;

public interface TimeService {

  long getEpochTimeMillis();

//  void setTime(Time time);

  static long getSecondsInMillis(long seconds) {
    return seconds * 1_000;
  }

  static long getMinutesInMillis(long minutes) {
    return getSecondsInMillis(minutes) * 60;
  }

  static long getHoursInMillis(long hours) {
    return getMinutesInMillis(hours) * 24;
  }
}
