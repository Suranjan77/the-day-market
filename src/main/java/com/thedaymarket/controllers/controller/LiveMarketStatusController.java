package com.thedaymarket.controllers.controller;

import com.thedaymarket.controllers.response.LiveMarketStatus;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.thedaymarket.domain.MarketStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("api/v1/market-live")
public class LiveMarketStatusController {
  private final ConcurrentLinkedQueue<DeferredResult<LiveMarketStatus>> LIVE_MARKET_STATUS_CLIENT =
      new ConcurrentLinkedQueue<>();

  @GetMapping
  public DeferredResult<LiveMarketStatus> registerClient() {
    var output = new DeferredResult<LiveMarketStatus>(30_000L);
    output.onTimeout(() -> LIVE_MARKET_STATUS_CLIENT.remove(output));
    output.onError(err -> LIVE_MARKET_STATUS_CLIENT.remove(output));
    LIVE_MARKET_STATUS_CLIENT.offer(output);
    return output;
  }

  public void notifyAll(MarketStatus status) {
    DeferredResult<LiveMarketStatus> client;
    while ((client = LIVE_MARKET_STATUS_CLIENT.poll()) != null) {
      client.setResult(new LiveMarketStatus(status));
    }
  }
}
