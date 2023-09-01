package com.thedaymarket.service.impl;

import com.thedaymarket.controllers.request.AuctionDetailsRequest;
import com.thedaymarket.controllers.request.CreateAuctionRequest;
import com.thedaymarket.domain.Auction;
import com.thedaymarket.domain.AuctionStatus;
import com.thedaymarket.domain.User;
import com.thedaymarket.repository.AuctionRepository;
import com.thedaymarket.service.AuctionService;
import com.thedaymarket.service.CategoryService;
import com.thedaymarket.service.StorageService;
import com.thedaymarket.utils.ExceptionUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Service
public class AuctionServiceImpl implements AuctionService {

  private final AuctionRepository auctionRepository;
  private final CategoryService categoryService;
  private final StorageService storageService;

  @Override
  public Page<Auction> getTodayAuctions(Pageable pageable) {
    return auctionRepository.findAllStatusNotAndScheduledAt(
        AuctionStatus.DRAFT, LocalDate.now(), pageable);
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
    auction.setCategory(categoryService.getCategory(createAuctionRequest.categoryId()));
    auction.setDescription(createAuctionRequest.description());
    auction.setSeller(seller);
    auction.setTitle(createAuctionRequest.title());
    auction.setType(createAuctionRequest.type());
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
    var scheduleDateTime =
        LocalDateTime.of(
            auctionDetailsRequest.scheduledDate(), auctionDetailsRequest.scheduledTime());
    if (scheduleDateTime.isBefore(LocalDateTime.now()) && auctionDetailsRequest.publish()) {
      auction.setStatus(AuctionStatus.PUBLISHED);
    }
    auction.setScheduledDateTime(scheduleDateTime);
    auction = auctionRepository.save(auction);
    return auction;
  }

  @Override
  public Auction uploadAuctionImage(User seller, Long auctionId, MultipartFile file) {
    var auction = getAuction(auctionId);
    try {
      var uploadResponse = storageService.upload(file);
      auction.setImageName(uploadResponse.fileName());
      auction.setImageUrl(uploadResponse.url());
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
}
