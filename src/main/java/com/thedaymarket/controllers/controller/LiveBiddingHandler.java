package com.thedaymarket.controllers.controller;

import com.thedaymarket.controllers.response.BidResponse;
import com.thedaymarket.controllers.response.StreamBidResponse;
import com.thedaymarket.controllers.response.StreamStatus;
import com.thedaymarket.domain.Bid;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

@Slf4j
@Component
public class LiveBiddingHandler {

  private final ConcurrentHashMap<Long, List<Client>> REQUESTER_BY_AUCTION_ID =
      new ConcurrentHashMap<>();

  public void notifyAll(Long auctionId, Bid bid) {
    var clients = REQUESTER_BY_AUCTION_ID.get(auctionId);
    if (clients != null) {
      List<Client> completedClients = new ArrayList<>();
      for (var client : clients) {
        client.client.setResult(new StreamBidResponse(StreamStatus.COMPLETED, BidResponse.of(bid)));
        completedClients.add(client);
      }
      clients.removeAll(completedClients);
    }
  }

  public void registerClient(Long auctionId, DeferredResult<StreamBidResponse> client) {
    REQUESTER_BY_AUCTION_ID.putIfAbsent(auctionId, new ArrayList<>());
    client.onError(
        sub -> {
          log.debug("Error occurred: removing subscriber for auctionId {}", auctionId);
          REQUESTER_BY_AUCTION_ID.get(auctionId).remove(new Client(client));
          client.setResult(new StreamBidResponse(StreamStatus.ERROR, null));
        });
    client.onTimeout(
        () -> {
          REQUESTER_BY_AUCTION_ID.get(auctionId).remove(new Client(client));
          client.setResult(new StreamBidResponse(StreamStatus.TIME_OUT, null));
        });
    REQUESTER_BY_AUCTION_ID.get(auctionId).add(new Client(client));
  }

  private record Client(@NotNull DeferredResult<StreamBidResponse> client) {}
}
