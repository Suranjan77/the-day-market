package com.thedaymarket.repository;

import com.thedaymarket.domain.Auction;
import com.thedaymarket.domain.Bid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {
  Page<Bid> findAllByAuction(Auction auction, PageRequest pageRequest);

  Optional<Bid> findFirstByAuctionOrderByAmountDesc(Auction auction);
}
