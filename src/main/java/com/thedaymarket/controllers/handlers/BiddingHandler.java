package com.thedaymarket.controllers.handlers;

import com.thedaymarket.controllers.request.BidRequest;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Slf4j
@Component
public class BiddingHandler {

  private final ConcurrentHashMap<Long, CopyOnWriteArrayList<ServerResponse.SseBuilder>>
      SSE_CLIENTS_BY_AUCTION_ID = new ConcurrentHashMap<>();

  public ServerResponse streamBids(ServerRequest req) {
    String auctionId = req.pathVariable("auctionId");
    // todo: validate aucitonId.
    if (StringUtils.hasLength(auctionId)) {
      // todo: Check live or dead auction, and send SSE for live only.
      return ServerResponse.sse(
          builder -> registerClient(Long.parseLong(auctionId), builder), Duration.ofSeconds(30L));
    } else {
      throw new ServerWebInputException("Auction id is required.");
    }
  }

  public ServerResponse addBid(ServerRequest req) {
    var auctionId = req.pathVariable("auctionId");
    if (!StringUtils.hasLength(auctionId)) {
      throw new ServerWebInputException("AuctionId is required.");
    }
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
