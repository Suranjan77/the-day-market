package com.thedaymarket.service.schedule;

import com.thedaymarket.repository.AuctionRepository;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** Future architecture: Must have separate service for streams/scheduled task. */
@Slf4j
@Component
@AllArgsConstructor
public class AppSchedules {

  private final DutchAuctionStateService dutchAuctionStateService;
  private final AuctionRepository auctionRepository;

  /** Updates today's dutch-auction state every second. */
  @Scheduled(fixedRate = 1000)
  public void dutchAuctionBidUpdate() {

    int index = 0;
    int batchSize = 100;

    var auctionPage =
        auctionRepository.findActiveDutchAuctions(
            LocalDate.now(), PageRequest.of(index++, batchSize));
    auctionPage.forEach(auction -> dutchAuctionStateService.getAdjustedState(auction, true));

    while (auctionPage.hasNext()) {
      auctionPage =
          auctionRepository.findActiveDutchAuctions(
              LocalDate.now(), PageRequest.of(index++, batchSize));
      auctionPage.forEach(auction -> dutchAuctionStateService.getAdjustedState(auction, true));
    }
  }

  // Every day at 10 AM
  @Scheduled(cron = "0 0 10 * * ?")
  public void marketOpen() {}

  // Every day at 5 PM
  @Scheduled(cron = "0 0 17 * * ?")
  public void marketClosed() {
    //    log.debug("Closing market");
  }

  /**
   * Creates new market every midnight and assigns Auctions scheduled for next day on that market.
   */
  @Scheduled(cron = "0 0 0 * * ?")
  public void marketMaker() {
    //    log.debug("Creating new market and assigning auctions");
  }
}
