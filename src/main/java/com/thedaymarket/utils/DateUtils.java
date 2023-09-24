package com.thedaymarket.utils;

import java.util.TimeZone;

public class DateUtils {

  public static final String THE_DAY_MARKET_TIMEZONE_ID = "Europe/London";

  public static TimeZone getTheMarketTimeZone() {
    return TimeZone.getTimeZone(THE_DAY_MARKET_TIMEZONE_ID);
  }
}
