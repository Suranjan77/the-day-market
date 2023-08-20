package com.thedaymarket.controllers.handlers;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Component
public class HealthCheckHandler {

  private final OperatingSystemMXBean mxBean;

  private HealthCheckHandler() {
    mxBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
  }

  public ServerResponse healthCheck(ServerRequest req) {
    return ServerResponse.ok()
        .body(
            new HealthCheck(
                mxBean.getTotalMemorySize(),
                mxBean.getFreeMemorySize(),
                mxBean.getAvailableProcessors(),
                LocalDateTime.now()));
  }

  private record HealthCheck(
      Long totalMemorySize,
      Long freeMemorySize,
      Integer availableProcessors,
      LocalDateTime currentTime) {}
}
