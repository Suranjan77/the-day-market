package com.thedaymarket.repository;

import com.thedaymarket.domain.Auction;
import com.thedaymarket.domain.AuctionStatus;
import java.time.LocalDate;

import com.thedaymarket.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

  @Query(
      "SELECT a FROM Auction a WHERE DATE(a.scheduledDateTime) = :scheduledAt AND a.status <> :status")
  Page<Auction> findAllStatusNotAndScheduledAt(
      @Param("status") AuctionStatus status,
      @Param("scheduledAt") LocalDate scheduledAt,
      Pageable pageable);

  Page<Auction> findAllBySellerAndStatusNot(User seller, AuctionStatus status, Pageable pageable);

  @Query(
      "SELECT a FROM Auction a WHERE a.seller = :seller AND DATE(a.scheduledDateTime) = :scheduledAt AND a.status <> :status")
  Page<Auction> findTodayAuctionBySellerAndStatusNot(
      @Param("seller") User seller,
      @Param("scheduledAt") LocalDate scheduleAt,
      @Param("status") AuctionStatus status,
      Pageable pageable);
}
