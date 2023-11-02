package com.thedaymarket.service;

import com.thedaymarket.controllers.request.AuctionDetailsRequest;
import com.thedaymarket.controllers.request.CreateAuctionRequest;
import com.thedaymarket.domain.Auction;
import com.thedaymarket.domain.Bid;
import com.thedaymarket.domain.DutchAuctionState;
import com.thedaymarket.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface AuctionService {
  Page<Auction> getTodayAuctions(Pageable pageable, String filters);

  Page<Auction> getAuctionsBySeller(User seller, boolean isIncludeDrafts, Pageable pageable);

  Page<Auction> getTodayAuctionsBySeller(User seller, boolean isIncludeDrafts, Pageable pageable);

  Auction getAuction(Long auctionId);

  Auction createAuction(User seller, CreateAuctionRequest createAuctionRequest);

  Auction updateAuctionDetails(User seller, Long auctionId, AuctionDetailsRequest auctionDetailsRequest);

  Auction uploadAuctionImage(User seller, Long auctionId, MultipartFile file);

  void deleteAuction(Long auctionId);

  DutchAuctionState getDutchAuctionState(Long auctionId);

  AuctionStats getAuctionStatsForSeller(User seller);

  Auction confirmPurchase(User user, Bid bid);
  Auction confirmRejection(User user, Bid bid);
}
