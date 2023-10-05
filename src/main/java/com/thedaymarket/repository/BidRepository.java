package com.thedaymarket.repository;

import com.thedaymarket.domain.Auction;
import com.thedaymarket.domain.Bid;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BidRepository extends JpaRepository<Bid, Long> {
  Page<Bid> findAllByAuction(Auction auction, PageRequest pageRequest);

  Optional<Bid> findFirstByAuctionOrderByAmountDesc(Auction auction);

  @Query("SELECT coalesce(MAX(b.amount), 0.0) FROM Bid b WHERE b.auction=:auction")
  BigDecimal findMaxBid(@Param("auction") Auction auction);
}
