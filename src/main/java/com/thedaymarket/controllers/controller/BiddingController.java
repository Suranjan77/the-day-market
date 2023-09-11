package com.thedaymarket.controllers.controller;

import com.thedaymarket.controllers.request.BidRequest;
import com.thedaymarket.utils.RESTUtils;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Slf4j
@Component
//todo
public class BiddingController {

  private final ConcurrentHashMap<Long, CopyOnWriteArrayList<ServerResponse.SseBuilder>>
      SSE_CLIENTS_BY_AUCTION_ID = new ConcurrentHashMap<>();

  public ServerResponse streamBids(ServerRequest req) {
    var auctionId = RESTUtils.getPathVariable(req, "auctionId");

    // todo: Check live or dead auction, and send SSE for live only.
    return ServerResponse.sse(
        builder -> registerClient(Long.parseLong(auctionId), builder), Duration.ofSeconds(30L));
  }

  public ServerResponse addBid(ServerRequest req) {
    var auctionId = RESTUtils.getPathVariable(req, "auctionId");
    var subscribers = SSE_CLIENTS_BY_AUCTION_ID.get(Long.parseLong(auctionId));

    try {
      var body = req.body(BidRequest.class);
      // todo: Save bid to DB
      if (subscribers != null && !subscribers.isEmpty()) {
        pushBid(subscribers, body);
      }
      return ServerResponse.ok().build();
    } catch (ServletException | IOException e) {
      throw new ServerWebInputException(e.getMessage());
    }
  }

  public void pushBid(
      CopyOnWriteArrayList<ServerResponse.SseBuilder> subscribers, BidRequest data) {
    List<ServerResponse.SseBuilder> failedSubscribers = new ArrayList<>();
    for (var subscriber : subscribers) {
      try {
        subscriber.send(data);
      } catch (IOException e) {
        failedSubscribers.add(subscriber);
      }
    }

    subscribers.removeAll(failedSubscribers);
  }

  private void registerClient(Long auctionId, ServerResponse.SseBuilder sseBuilder) {
    SSE_CLIENTS_BY_AUCTION_ID.putIfAbsent(auctionId, new CopyOnWriteArrayList<>());
    sseBuilder.onComplete(
        () -> {
          log.debug("Complete: removing subscriber");
          SSE_CLIENTS_BY_AUCTION_ID.get(auctionId).remove(sseBuilder);
        });
    sseBuilder.onError(
        sub -> {
          log.debug("Error occurred: removing subscriber");
          SSE_CLIENTS_BY_AUCTION_ID.get(auctionId).remove(sseBuilder);
        });
    sseBuilder.onTimeout(
        () -> {
          log.debug("Timeout: removing subscriber");
          SSE_CLIENTS_BY_AUCTION_ID.get(auctionId).remove(sseBuilder);
        });
    SSE_CLIENTS_BY_AUCTION_ID.get(auctionId).add(sseBuilder);
  }
}
