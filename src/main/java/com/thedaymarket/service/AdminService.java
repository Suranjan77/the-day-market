package com.thedaymarket.service;

import com.thedaymarket.domain.Auction;
import com.thedaymarket.domain.AuctionStatus;
import com.thedaymarket.domain.Bid;
import com.thedaymarket.domain.TransactionType;
import com.thedaymarket.repository.AuctionRepository;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;

import com.thedaymarket.repository.TransactionRepository;
import com.thedaymarket.repository.UserPointsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class AdminService {
  private final AuctionRepository auctionRepository;
  private final BidService bidService;
  private final TransactionRepository transactionRepository;
  private final PaymentService paymentService;
  private final UserPointsRepository userPointsRepository;

  @Async
  @Transactional
  public void startMarket() {
    auctionRepository.findAllByScheduledDate(LocalDate.now()).stream()
        .filter(a -> !a.getStatus().equals(AuctionStatus.DRAFT))
        .forEach(
            a -> {
              a.setStatus(AuctionStatus.PUBLISHED);
              auctionRepository.save(a);
            });
  }

  @Async
  @Transactional
  public void endMarket() {
    var todayAuctions =
        auctionRepository.findAllByScheduledDate(LocalDate.now()).stream()
            .filter(a -> !a.getStatus().equals(AuctionStatus.DRAFT))
            .toList();

    for (Auction auction : todayAuctions) {
      Optional<Bid> winningBid = determineWinner(auction);
      winningBid.ifPresentOrElse(
          w -> {
            auction.setStatus(AuctionStatus.CLOSED);
            auction.setWinningBid(w);
          },
          () -> auction.setStatus(AuctionStatus.UNSOLD));

      for (Bid bid : auction.getBids()) {
        if (winningBid.isPresent()) {
          if (bid.equals(winningBid.get())) {
            continue;
          }
        }
        refundBid(auction, bid);
      }

      auctionRepository.save(auction);
    }
  }

  private void refundBid(Auction auction, Bid bid) {
    var bidder = bid.getBidder();
    var seller = auction.getSeller();
    var transaction =
        transactionRepository.findByTypeAndSellerAndBuyerAndOrderId(
            TransactionType.BID, seller, bidder, bid.getId());

    transaction.ifPresent(
        t -> {
          if (t.getAmount().equals(bid.getAmount())) {
            paymentService.refundTransaction(t);
            userPointsRepository.updateUserPointsAmount(
                bidder.getPoints().add(bid.getAmount()), bidder.getUserPoints().getId());
          }
        });
  }

  private Optional<Bid> determineWinner(Auction auction) {
    if (auction.getBids().isEmpty()) {
      return Optional.empty();
    }

    var bids = auction.getBids();
    switch (auction.getType()) {
      case Dutch -> {
        if (auction.getWinningBid() != null) {
          Comparator<Bid> bidComparator =
              (b1, b2) -> b2.getCreatedAt().compareTo(b1.getCreatedAt());
          bids.sort(bidComparator);
          return Optional.of(bids.get(0));
        }
      }
      case English -> {
        return bidService.getMaxBid(auction);
      }
      case Sealed -> {
        if (bids.size() == 1) {
          return Optional.of(bids.get(0));
        } else { // second-highest bidder
          return Optional.of(bids.get(1));
        }
      }
    }

    return Optional.empty();
  }
}
