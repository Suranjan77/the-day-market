package com.thedaymarket.service.impl;

import java.io.InputStream;

public record DownloadedObject(String contentType, InputStream stream) {}
