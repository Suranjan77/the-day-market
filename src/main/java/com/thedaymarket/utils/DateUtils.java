package com.thedaymarket.utils;

import java.util.TimeZone;

public class DateUtils {
  public static TimeZone getTheMarketTimeZone() {
    return TimeZone.getTimeZone("Europe/London");
  }
}
