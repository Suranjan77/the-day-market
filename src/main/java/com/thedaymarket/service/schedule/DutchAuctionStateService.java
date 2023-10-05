package com.thedaymarket.service.schedule;

import com.thedaymarket.controllers.handlers.DutchAuctionStateHandler;
import com.thedaymarket.controllers.response.DutchAuctionStateResponse;
import com.thedaymarket.domain.Auction;
import com.thedaymarket.domain.DutchAuctionState;
import com.thedaymarket.repository.DutchAuctionStateRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

/**
 * Fetch Auction| Case [First Time]: create new state, Timer start when at least 1 buyer visits
 * auction. Default case: get existing state, don't adjust.
 *
 * <p>Adjust Auction| Case [Market Open]: adjust currentTime and currentPoints, if currentPoints is
 * zero or negative or currentTime is After marketClosed set the state as expired. Case [Market
 * Closed]: Set state expired
 */
@Service
@RequiredArgsConstructor
public class DutchAuctionStateService {
  private final DutchAuctionStateRepository dutchAuctionStateRepository;
  private final DutchAuctionStateHandler auctionStateHandler;

  @Value("${market.open.time}")
  @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
  private LocalTime marketStartTime;

  @Value("${market.closed.time}")
  @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
  private LocalTime marketCloseTime;

  public DutchAuctionState getAdjustedState(Auction auction, Boolean shouldNotify) {
    var state =
        dutchAuctionStateRepository
            .findStateOnDayForAuction(LocalDate.now(), auction)
            .orElseGet(
                () -> {
                  var auctionState = new DutchAuctionState();
                  auctionState.setAuction(auction);
                  auctionState.setTimerStartedAt(LocalTime.now());
                  auctionState.setCurrentPoints(auction.getMinAskPrice());
                  auctionState.setExpired(false);
                  return auctionState;
                });

    if (isMarketOpen()) {
      var exhaustedSeconds = state.getTimerStartedAt().until(LocalTime.now(), ChronoUnit.SECONDS);

      var currentPoints =
          auction
              .getMinAskPrice()
              .subtract(
                  BigDecimal.valueOf(
                      ((double) (exhaustedSeconds / auction.getDecrementSeconds()))
                          * auction.getDecrementFactor()));

      if (currentPoints.signum() <= 0) {
        state.setExpired(true);
      }

      state.setCurrentPoints(currentPoints);
      state.setExpired(false);
    } else {
      state.setExpired(true);
    }

    if (shouldNotify) {
      auctionStateHandler.notifyAll(auction.getId(), DutchAuctionStateResponse.of(state));
    }

    return dutchAuctionStateRepository.save(state);
  }

  private boolean isMarketOpen() {
    return !LocalTime.now().isBefore(marketStartTime) && !LocalTime.now().isAfter(marketCloseTime);
  }
}
