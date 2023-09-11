package com.thedaymarket.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin("*")
@RequestMapping("web")
public class ViewController {
  @GetMapping()
  public String index() {
    return "LandingPage";
  }

  @GetMapping("/login")
  public String login() {
    return "Login";
  }

  @GetMapping("/register")
  public String signup() {
    return "Signup";
  }

  @GetMapping("/user-details")
  public String userDetails() {
    return "UserDetails";
  }

  @GetMapping("/list-item")
  public String listAnItem() {
    return "ListanItem";
  }

  @GetMapping("/my-bids")
  public String myBid() {
    return "MyBid";
  }

  @GetMapping("/my-listings")
  public String myListings() {
    return "MyListings";
  }

  @GetMapping("/market")
  public String market() {
    return "MarketPlace";
  }

  @GetMapping("/dutch-auction")
  public String dutchAuction() {
    return "DutchAuctionBid";
  }

  @GetMapping("/dutch-auction-config")
  public String dutchAuctionConfig() {
    return "DutchAuctionConfiguration";
  }

  @GetMapping("/auction")
  public String englishAuction() {
    return "Auction";
  }

  @GetMapping("/english-auction-config")
  public String englishAuctionConfig() {
    return "EnglishAuctionConfiguration";
  }

  @GetMapping("/sealed-bid-auction")
  public String sealedBidAuction() {
    return "SealedBidAuction";
  }

  @GetMapping("/sealed-bid-auction-config")
  public String sealedBidAuctionConfig() {
    return "SealedBidAuctionConfiguration";
  }

  @GetMapping("/buy-points")
  public String buyPoints() {
    return "BuyPoints";
  }

  @GetMapping("/buyer-dashboard")
  public String buyerDashboard() {
    return "BuyerDashboard";
  }

  @GetMapping("/seller-dashboard")
  public String sellerDashboard() {
    return "SellerDashboard";
  }
}
