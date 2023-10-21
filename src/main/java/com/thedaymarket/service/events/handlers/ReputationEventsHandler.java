package com.thedaymarket.service.events.handlers;

import com.thedaymarket.domain.Rating;
import com.thedaymarket.domain.Reputation;
import com.thedaymarket.domain.ReputationModifiers;
import com.thedaymarket.repository.ReputationRepository;
import com.thedaymarket.service.AuctionService;
import com.thedaymarket.service.RatingService;
import com.thedaymarket.service.UserService;
import com.thedaymarket.service.events.AuctionChangeEvent;
import com.thedaymarket.service.events.RatingChangedEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ReputationEventsHandler {

  private final RatingService ratingService;
  private final AuctionService auctionService;
  private final ReputationRepository reputationRepository;
  private final UserService userService;

  /**
   * reputation = {(averageRating * W1) + (soldItems/listedItems) * W2 * 5 * normalizer + ...} * 20
   * Where W1 + W2 + ... + Wn = 1.0, 0 <= averageRating <= 5, multiply by 20 because total
   * reputation is 5 due to rating's range, normalizer is a value with range 0 to 1, which should
   * grow fast when listedItems is less and grow slow when listedItems is large e.g. normalizer =
   * log(listedItems + 1), +1 to avoid log(0)
   */
  @Async
  @EventListener
  public void handleRatingChangeEvent(RatingChangedEvent event) {
    var rating = event.getRating();
    var averageRating = ratingService.getAverageRating(rating.getType(), rating.getReceiverId());
    switch (rating.getType()) {
      case AUCTION -> handleRatingReputationForAuction(rating, averageRating);
      case SELLER -> handleRatingRefactorForSeller(rating, averageRating);
    }
  }

  @Async
  @EventListener
  public void handleAuctionChangeEvent(AuctionChangeEvent event) {
    var auction = event.getAuction();
    var reputation = reputationRepository.findByUser(auction.getSeller()).orElse(new Reputation());

    if (reputation.getId() == null) {
      reputation.setSellerRating(0);
      reputation.setAuctionRating(0);
    }

    var auctionStats = auctionService.getAuctionStatsForSeller(auction.getSeller());
    reputation.setTotalSold(auctionStats.soldAuctions());
    reputation.setTotalAuctions(auctionStats.totalAuctions());
    reputation.setReputationPoints(recalculateRating(reputation));

    reputationRepository.save(reputation);
  }

  private void handleRatingRefactorForSeller(Rating rating, int averageRating) {
    var seller = userService.getUser(rating.getReceiverId());
    var reputation = reputationRepository.findByUser(seller).orElse(new Reputation());

    if (reputation.getId() == null) {
      reputation.setTotalSold(0L);
      reputation.setTotalAuctions(0L);
      reputation.setAuctionRating(0);
    }

    reputation.setSellerRating(averageRating);
    reputation.setReputationPoints(recalculateRating(reputation));
    reputationRepository.save(reputation);
  }

  private void handleRatingReputationForAuction(Rating rating, int averageRating) {
    var auction = auctionService.getAuction(rating.getReceiverId());
    var seller = auction.getSeller();

    var reputation = reputationRepository.findByUser(seller).orElse(new Reputation());

    if (reputation.getId() == null) {
      reputation.setTotalSold(0L);
      reputation.setTotalAuctions(0L);
      reputation.setSellerRating(0);
    }

    reputation.setAuctionRating(averageRating);

    reputation.setReputationPoints(recalculateRating(reputation));

    reputationRepository.save(reputation);
  }

  private double recalculateRating(Reputation reputation) {

    double sellerRatingPart =
        reputation.getSellerRating() * ReputationModifiers.SELLER_RATING.getWeight();

    double auctionRatingPart =
        reputation.getAuctionRating() * ReputationModifiers.AUCTION_RATING.getWeight();

    double auctionSalesPart = getAuctionSalesPart(reputation);

    return squeezeReputationIntoRange(sellerRatingPart + auctionRatingPart + auctionSalesPart);
  }

  private static double getAuctionSalesPart(Reputation reputation) {
    double auctionSalesPart =
        reputation.getTotalAuctions() == 0
            ? 0
            : (reputation.getTotalSold() / (double) reputation.getTotalAuctions())
                * ReputationModifiers.LISTINGS.getWeight();

    if (auctionSalesPart
        > 0) { // normalizing auctionSalesPart so that seller with fewer listings don't get max
      // reputation, and multiplying by 5 to match total ratings of 5
      auctionSalesPart *= Math.log(reputation.getTotalAuctions() + 1);
    }
    return auctionSalesPart;
  }

  /**
   * Applies sigmoid function 1 / (1 - e^-x) to squeeze the input into [0,1] range, where sigmoid
   * midpoint = 50 and steepness = 0.01. Then the value is multiplied by 99 to get the range [0,99]
   * and finally 1 is added to get the range [1,100]
   */
  private double squeezeReputationIntoRange(double v) {
    return 1 + 99 * (1 / (1 + Math.exp(-1 * (0.01 * (v - 50)))));
  }
}
