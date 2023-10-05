package com.thedaymarket.controllers.handlers;

import com.thedaymarket.controllers.response.DutchAuctionStateResponse;
import com.thedaymarket.controllers.response.StreamResponse;
import com.thedaymarket.controllers.response.StreamStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

@Slf4j
@Component
public class DutchAuctionStateHandler {

  private final ConcurrentHashMap<
          Long, List<DeferredResult<StreamResponse<DutchAuctionStateResponse>>>>
      REQUESTER_BY_AUCTION_ID = new ConcurrentHashMap<>();

  public void notifyAll(Long auctionId, DutchAuctionStateResponse auctionState) {
    var clients = REQUESTER_BY_AUCTION_ID.get(auctionId);
    if (clients != null) {
      List<DeferredResult<StreamResponse<DutchAuctionStateResponse>>> completedClients =
          new ArrayList<>();
      for (var client : clients) {
        client.setResult(new StreamResponse<>(StreamStatus.COMPLETED, auctionState));
        completedClients.add(client);
      }
      clients.removeAll(completedClients);
    }
  }

  public void registerClient(
      Long auctionId, DeferredResult<StreamResponse<DutchAuctionStateResponse>> client) {
    REQUESTER_BY_AUCTION_ID.putIfAbsent(auctionId, new ArrayList<>());
    client.onError(
        sub -> {
          log.debug("Error occurred: removing subscriber for auctionId {}", auctionId);
          REQUESTER_BY_AUCTION_ID.get(auctionId).remove(client);
          client.setResult(new StreamResponse<>(StreamStatus.ERROR, null));
        });
    client.onTimeout(
        () -> {
          REQUESTER_BY_AUCTION_ID.get(auctionId).remove(client);
          client.setResult(new StreamResponse<>(StreamStatus.TIME_OUT, null));
        });
    REQUESTER_BY_AUCTION_ID.get(auctionId).add(client);
  }
}
