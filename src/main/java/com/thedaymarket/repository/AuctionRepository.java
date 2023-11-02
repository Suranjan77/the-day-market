package com.thedaymarket.repository;

import com.thedaymarket.domain.Auction;
import com.thedaymarket.domain.AuctionStatus;
import com.thedaymarket.domain.User;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AuctionRepository extends JpaRepository<Auction, Long> {

  @Query(
      "SELECT a FROM Auction a WHERE DATE(a.scheduledDate) = :scheduledAt AND a.status <> :status")
  Page<Auction> findAllStatusNotAndScheduledAt(
      @Param("status") AuctionStatus status,
      @Param("scheduledAt") LocalDate scheduledAt,
      Pageable pageable);

  @Query(
      "SELECT a FROM Auction a WHERE DATE(a.scheduledDate) = :scheduledAt AND a.status=com.thedaymarket.domain.AuctionStatus.PUBLISHED AND a.type=com.thedaymarket.domain.AuctionType.Dutch")
  Page<Auction> findActiveDutchAuctions(@Param("scheduledAt") LocalDate scheduledAt, Pageable page);

  Page<Auction> findAllBySellerAndStatusNot(User seller, AuctionStatus status, Pageable pageable);

  Page<Auction> findAll(Specification<Auction> spec, Pageable pageable);

  List<Auction> findAllByScheduledDate(LocalDate scheduledDate);

  @Query(
      "SELECT a FROM Auction a WHERE a.seller = :seller AND DATE(a.scheduledDate) = :scheduledAt AND a.status <> :status")
  Page<Auction> findTodayAuctionBySellerAndStatusNot(
      @Param("seller") User seller,
      @Param("scheduledAt") LocalDate scheduleAt,
      @Param("status") AuctionStatus status,
      Pageable pageable);

  @Modifying
  @Query("UPDATE Auction a SET a.status=:status WHERE a.scheduledDate=:scheduledDate")
  void updateAuctionsStatus(
      @Param("scheduledDate") LocalDate scheduledDate, @Param("status") AuctionStatus status);

  @Query("SELECT COUNT(a) FROM Auction a WHERE a.seller=:seller AND a.status in :status")
  Long getTotalAuctionByStatusIn(
      @Param("seller") User seller, @Param("status") List<AuctionStatus> auctionStatuses);
}
