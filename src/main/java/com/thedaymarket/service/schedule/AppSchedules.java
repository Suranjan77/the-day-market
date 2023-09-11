package com.thedaymarket.service.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppSchedules {

  // Check every 5 seconds
  @Scheduled(fixedRate = 5000)
  public void dutchAuctionBidUpdate() {
//    log.debug("Checking dutch auctions to update bid amount");
  }

  // Every day at 10 AM
  @Scheduled(cron = "0 0 10 * * ?")
  public void marketOpen() {

  }

  // Every day at 5 PM
  @Scheduled(cron = "0 0 17 * * ?")
  public void marketClosed() {
//    log.debug("Closing market");
  }

  // Creates new market every midnight
  @Scheduled(cron = "0 0 0 * * ?")
  public void marketMaker() {
//    log.debug("Creating new market and assigning auctions");
  }
}
