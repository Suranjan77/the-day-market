package com.thedaymarket.controllers.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ErrorMessage(
    int status, String error, List<String> errors, String request, LocalDateTime timeStamp) {}
