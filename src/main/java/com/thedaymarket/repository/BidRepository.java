package com.thedaymarket.repository;

import com.thedaymarket.domain.Auction;
import com.thedaymarket.domain.Bid;
import com.thedaymarket.domain.User;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BidRepository extends JpaRepository<Bid, Long> {
  Page<Bid> findAllByAuction(Auction auction, PageRequest pageRequest);

  Optional<Bid> findFirstByAuctionOrderByAmountDesc(Auction auction);

  boolean existsByAuctionAndCreatedAt(Auction auction, LocalDateTime day);

  boolean existsByAuctionAndBidder(Auction auction, User user);

  @Query("SELECT coalesce(MAX(b.amount), 0.0) FROM Bid b WHERE b.auction=:auction")
  BigDecimal findMaxBid(@Param("auction") Auction auction);

  @Query(
      value =
          """
        WITH LatestBids AS (
            SELECT
                b.id,
                b.created_at,
                b.updated_at,
                b.amount,
                b.auction_id,
                b.user_id,
                ROW_NUMBER() OVER (PARTITION BY b.auction_id ORDER BY b.created_at DESC) AS rn
            FROM bid b
            WHERE b.user_id = :userId
        )

        SELECT
            lb.id,
            lb.created_at,
            lb.updated_at,
            lb.amount,
            lb.auction_id,
            lb.user_id
        FROM LatestBids lb
        WHERE lb.rn = 1
    """,
      countQuery =
          """
        WITH LatestBids AS (
            SELECT
                b.id,
                ROW_NUMBER() OVER (PARTITION BY b.auction_id ORDER BY b.created_at DESC) AS rn
            FROM bid b
            WHERE b.user_id = :userId
        )
        SELECT COUNT(lb.id)
        FROM LatestBids lb
        WHERE lb.rn = 1
    """,
      nativeQuery = true)
  Page<Bid> findLatestBidsByUser(@Param("userId") Long userId, Pageable pageable);
}
