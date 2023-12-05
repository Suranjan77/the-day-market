package com.thedaymarket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.thedaymarket.controllers.request.CreateAuctionRequest;
import com.thedaymarket.controllers.request.CreateCategoryRequest;
import com.thedaymarket.domain.*;
import com.thedaymarket.repository.AuctionRepository;
import com.thedaymarket.service.AdminService;
import com.thedaymarket.service.AuctionService;
import com.thedaymarket.service.CategoryService;
import com.thedaymarket.service.impl.AuctionServiceImpl;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

public class AuctionServiceTests {

  @Test
  public void testStartMarket() {
    AuctionRepository auctionRepository = mock(AuctionRepository.class);
    Auction auction = new Auction();
    auction.setStatus(AuctionStatus.SCHEDULED);
    auction.setScheduledDate(LocalDate.now());
    when(auctionRepository.findAllByScheduledDate(LocalDate.now())).thenReturn(List.of(auction));
    AdminService adminService = new AdminService(auctionRepository, null, null, null, null);
    adminService.startMarket();
    verify(auctionRepository).save(auction);
    assertEquals(AuctionStatus.PUBLISHED, auction.getStatus());
  }

  @Test
  void testCreateAuction() {

    User seller = new User();

    CreateAuctionRequest request =
        new CreateAuctionRequest(
            "Test Auction", "Test Auction", AuctionType.Dutch, "Test auction description", false);

    CategoryService categoryService = mock(CategoryService.class);
    when(categoryService.getOrCreate(any(CreateCategoryRequest.class))).thenReturn(new Category());

    AuctionRepository auctionRepository = mock(AuctionRepository.class);

    AuctionService auctionService =
        new AuctionServiceImpl(auctionRepository, categoryService, null, null, null, null);

    auctionService.createAuction(seller, request);
    verify(categoryService).getOrCreate(any(CreateCategoryRequest.class));
    verify(auctionRepository).save(any(Auction.class));
  }
}
