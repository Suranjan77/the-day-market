package com.thedaymarket.repository;

import com.thedaymarket.domain.Auction;
import com.thedaymarket.domain.DutchAuctionState;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface DutchAuctionStateRepository extends JpaRepository<DutchAuctionState, Long> {

  @Query("SELECT s FROM DutchAuctionState s WHERE s.auction=:auction AND DATE(s.createdAt)=:day")
  Optional<DutchAuctionState> findStateOnDayForAuction(
      @Param("day") LocalDate day, @Param("auction") Auction auction);

  @Modifying
  @Query(
      "UPDATE DutchAuctionState ds SET ds.currentPoints=:currentPoints WHERE ds.auction=:auction")
  void updateAuctionState(
      @Param("currentPoints") BigDecimal currentPoints, @Param("auction") Auction auction);
}
