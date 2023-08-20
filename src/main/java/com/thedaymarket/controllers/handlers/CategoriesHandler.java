package com.thedaymarket.controllers.handlers;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Component
public class CategoriesHandler {

  public ServerResponse createCategory(ServerRequest req) {
    return ServerResponse.ok().build();
  }

  public ServerResponse createSubCategory(ServerRequest req) {
    return ServerResponse.ok().build();
  }

  public ServerResponse getCategories(ServerRequest req) {
    return ServerResponse.ok().build();
  }

  public ServerResponse getCategory(ServerRequest req) {
    //      @PathVariable("categoryId") Long categoryId
    return ServerResponse.ok().build();
  }

  public ServerResponse getChildrenCategories(ServerRequest req) {
    //      @PathVariable("categoryId") Long categoryId
    return ServerResponse.ok().build();
  }

  public ServerResponse deleteCategory(ServerRequest req) {
    //      @PathVariable("categoryId") Long categoryId
    return ServerResponse.ok().build();
  }
}
