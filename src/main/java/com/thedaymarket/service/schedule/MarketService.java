package com.thedaymarket.service.schedule;

import com.thedaymarket.domain.Auction;
import com.thedaymarket.service.AuctionService;
import com.thedaymarket.service.BidService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class MarketService {
  private final AuctionService auctionService;
  private final BidService bidService;

  public void calculateWinner() {
    int index = 0;
    int batchSize = 150;

    var auctionPage = auctionService.getTodayAuctions(PageRequest.of(index, batchSize), null);
    auctionPage.stream().forEach(this::calculateWinner);

    while (auctionPage.hasNext()) {
      auctionPage = auctionService.getTodayAuctions(PageRequest.of(index, batchSize), null);
      auctionPage.stream().forEach(this::calculateWinner);
      index++;
    }
  }

  private void calculateWinner(Auction auction) {
    switch (auction.getType()) {
      case English -> calculateEnglishAuctionWinner(auction);
      case Sealed -> calculateSealedAuctionWinner(auction);
      case Dutch -> calculateDutchAuctionWinner(auction);
    }
  }

  private void calculateDutchAuctionWinner(Auction auction) {

  }

  private void calculateEnglishAuctionWinner(Auction auction) {}

  private void calculateSealedAuctionWinner(Auction auction) {}
}
