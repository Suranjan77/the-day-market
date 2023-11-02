package com.thedaymarket.service.impl;

import com.thedaymarket.controllers.request.AuctionDetailsRequest;
import com.thedaymarket.controllers.request.CreateAuctionRequest;
import com.thedaymarket.controllers.request.CreateCategoryRequest;
import com.thedaymarket.domain.*;
import com.thedaymarket.repository.AuctionRepository;
import com.thedaymarket.repository.UserPointsRepository;
import com.thedaymarket.service.*;
import com.thedaymarket.service.schedule.DutchAuctionStateService;
import com.thedaymarket.utils.AuctionSearchFieldNames;
import com.thedaymarket.utils.ExceptionUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Service
public class AuctionServiceImpl implements AuctionService {

  private final AuctionRepository auctionRepository;
  private final CategoryService categoryService;
  private final StorageService storageService;
  private final DutchAuctionStateService dutchAuctionStateService;
  private final PaymentService paymentService;
  private UserPointsRepository userPointsRepository;

  @Override
  public Page<Auction> getTodayAuctions(Pageable pageable, String filters) {
    var specs = getAuctionSpecs(filters, true);
    return auctionRepository.findAll(specs, pageable);
  }

  @Override
  public Page<Auction> getAuctionsBySeller(
      User seller, boolean isIncludeDrafts, Pageable pageable) {
    return auctionRepository.findAllBySellerAndStatusNot(
        seller, isIncludeDrafts ? AuctionStatus.NONE : AuctionStatus.DRAFT, pageable);
  }

  @Override
  public Page<Auction> getTodayAuctionsBySeller(
      User seller, boolean isIncludeDrafts, Pageable pageable) {
    return auctionRepository.findTodayAuctionBySellerAndStatusNot(
        seller,
        LocalDate.now(),
        isIncludeDrafts ? AuctionStatus.NONE : AuctionStatus.DRAFT,
        pageable);
  }

  @Override
  public Auction getAuction(Long auctionId) {
    return auctionRepository
        .findById(auctionId)
        .orElseThrow(
            () ->
                ExceptionUtils.getNotFoundExceptionResponse(
                    "Auction not found for id:" + auctionId));
  }

  @Override
  public Auction createAuction(User seller, CreateAuctionRequest createAuctionRequest) {
    var auction = new Auction();
    auction.setCategory(
        categoryService.getOrCreate(new CreateCategoryRequest(createAuctionRequest.category())));
    auction.setDescription(createAuctionRequest.description());
    auction.setSeller(seller);
    auction.setTitle(createAuctionRequest.title());
    auction.setType(createAuctionRequest.type());
    auction.setStatus(
        createAuctionRequest.isDraft() ? AuctionStatus.DRAFT : AuctionStatus.SCHEDULED);
    auction = auctionRepository.save(auction);
    return auction;
  }

  @Override
  public Auction updateAuctionDetails(
      User seller, Long auctionId, AuctionDetailsRequest auctionDetailsRequest) {
    var auction = getAuction(auctionId);
    auction.setStatus(
        auctionDetailsRequest.publish() ? AuctionStatus.SCHEDULED : AuctionStatus.DRAFT);
    auction.setDecrementFactor(auctionDetailsRequest.decrementFactor());
    auction.setMinAskPrice(auctionDetailsRequest.minAskPrice());
    auction.setDecrementSeconds(auctionDetailsRequest.decrementSeconds());
    auction.setItemCount(auctionDetailsRequest.itemCount());
    var scheduleDate = auctionDetailsRequest.scheduledDate();
    if ((scheduleDate.isBefore(LocalDate.now()) || scheduleDate.isEqual(LocalDate.now()))
        && auctionDetailsRequest.publish()) {
      auction.setStatus(AuctionStatus.PUBLISHED);
    }
    auction.setScheduledDate(scheduleDate);
    auction = auctionRepository.save(auction);
    return auction;
  }

  @Override
  public Auction uploadAuctionImage(User seller, Long auctionId, MultipartFile file) {
    var auction = getAuction(auctionId);
    try {
      var uploadResponse = storageService.upload(file);
      auction.setImageName(uploadResponse.fileName());
      auction = auctionRepository.save(auction);
    } catch (Exception e) {
      throw ExceptionUtils.getServerException(e.getMessage());
    }

    return auction;
  }

  @Override
  public void deleteAuction(Long auctionId) {
    auctionRepository.deleteById(auctionId);
  }

  @Override
  public DutchAuctionState getDutchAuctionState(Long auctionId) {
    var auction = getAuction(auctionId);
    if (!auction.getType().equals(AuctionType.Dutch)) {
      throw ExceptionUtils.getNotFoundExceptionResponse(
          "The requested auction is not Dutch Auction");
    }
    return dutchAuctionStateService.getAdjustedState(auction, false);
  }

  @Override
  public AuctionStats getAuctionStatsForSeller(User seller) {
    if (!seller.getRole().equals(UserRole.SELLER)) {
      throw new ExceptionUtils.BusinessException(HttpStatus.BAD_REQUEST, "User is not seller");
    }

    var totalAuctions =
        auctionRepository.getTotalAuctionByStatusIn(
            seller,
            List.of(
                AuctionStatus.PUBLISHED,
                AuctionStatus.CLOSED,
                AuctionStatus.UNSOLD,
                AuctionStatus.SOLD));

    var totalSoldAuctions =
        auctionRepository.getTotalAuctionByStatusIn(seller, List.of(AuctionStatus.SOLD));

    return new AuctionStats(totalSoldAuctions, totalAuctions);
  }

  @Override
  public Auction confirmPurchase(User buyer, Bid bid) {
    validateWinningBid(bid);

    var auction = bid.getAuction();
    auction.setStatus(AuctionStatus.SOLD);
    auction = auctionRepository.save(auction);

    var seller = bid.getAuction().getSeller();
    paymentService.buyAuction(bid.getAmount(), buyer, seller, bid.getId());

    userPointsRepository.updateUserPointsAmount(
        seller.getPoints().add(bid.getAmount()), seller.getUserPoints().getId());

    return auction;
  }

  @Override
  public Auction confirmRejection(User buyer, Bid bid) {
    validateWinningBid(bid);

    var auction = bid.getAuction();
    auction.setStatus(AuctionStatus.UNSOLD);
    auction = auctionRepository.save(auction);

    var seller = bid.getAuction().getSeller();
    paymentService.refundBid(bid.getAmount(), buyer, seller, bid.getId());

    return auction;
  }

  public void validateWinningBid(Bid bid) {
    var auction = bid.getAuction();

    if (!auction.getWinningBid().equals(bid)) {
      throw new ExceptionUtils.BusinessException(
          HttpStatus.UNPROCESSABLE_ENTITY, "The bid is not the winning bid");
    }

    if (auction.getStatus().equals(AuctionStatus.SOLD)) {
      throw new ExceptionUtils.BusinessException(
          HttpStatus.UNPROCESSABLE_ENTITY, "The auction is already sold");
    }

    if (!auction.getStatus().equals(AuctionStatus.CLOSED)) {
      throw new ExceptionUtils.BusinessException(
          HttpStatus.UNPROCESSABLE_ENTITY, "Auction not yet closed");
    }
  }

  private static Specification<Auction> getAuctionSpecs(String filters, boolean isToday) {
    Map<String, List<String>> fieldBasedFilterTable = new HashMap<>();
    if (filters != null && !filters.isBlank()) {
      var filterParts = filters.split(";");
      for (var filterPart : filterParts) {
        var part = filterPart.split("=");
        if (part.length == 2) {
          var field = part[0];
          var value = part[1];
          fieldBasedFilterTable.putIfAbsent(field, new ArrayList<>());
          fieldBasedFilterTable.get(field).add(value);
        }
      }
    }

    List<AuctionSpecification> searchSpecs = new ArrayList<>();
    fieldBasedFilterTable.forEach(
        (field, filterValues) -> {
          switch (field) {
            case AuctionSearchFieldNames.CATEGORIES -> searchSpecs.add(
                new AuctionSpecification(
                    new AuctionSpecification.SearchCriteria("category.tag", "in", filterValues)));
            case AuctionSearchFieldNames.AUCTION_TYPES -> searchSpecs.add(
                new AuctionSpecification(
                    new AuctionSpecification.SearchCriteria("type", "=", filterValues.get(0))));
          }
        });

    if (isToday) {
      searchSpecs.add(
          new AuctionSpecification(
              new AuctionSpecification.SearchCriteria("scheduledDate", "=", LocalDate.now())));
    }

    var noDraftSpec =
        new AuctionSpecification(
            new AuctionSpecification.SearchCriteria("status", "!", AuctionStatus.DRAFT));

    var specs = Specification.where(noDraftSpec);

    if (!searchSpecs.isEmpty()) {
      for (var spec : searchSpecs) {
        specs = specs.and(spec);
      }
    }
    return specs;
  }
}
