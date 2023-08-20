package com.thedaymarket.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.*;
import java.util.Map;
import java.util.WeakHashMap;

public class JsonUtils {
  private static final ObjectMapper om = createObjectMapper();
  private static final Map<Class<?>, ObjectReader> readerMap = new WeakHashMap<>();
  private static final Map<Class<?>, ObjectWriter> writerMap = new WeakHashMap<>();

  private JsonUtils() {}

  private static JsonMapper createObjectMapper() {
    return JsonMapper.builder()
        .serializationInclusion(JsonInclude.Include.NON_EMPTY)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
        .configure(MapperFeature.USE_GETTERS_AS_SETTERS, false)
        .addModule(new JavaTimeModule())
        .findAndAddModules()
        .build();
  }

  private static ObjectReader getObjectReader(Class<?> klass) {
    ObjectReader or = readerMap.get(klass);
    if (null == or) {
      or = om.readerFor(klass);
      readerMap.put(klass, or);
    }

    return or;
  }

  private static ObjectWriter getObjectWriter(Class<?> klass) {
    ObjectWriter ow = writerMap.get(klass);
    if (null == ow) {
      ow = om.writerFor(klass);
      writerMap.put(klass, ow);
    }

    return ow;
  }

  public static <T> T fromJson(String json, Class<T> beanClass) {
    return fromJson((Reader) (new StringReader(json)), (ObjectReader) getObjectReader(beanClass));
  }

  public static <T> T fromJsonByType(String json, TypeReference<T> type) {
    return fromJson((Reader) (new StringReader(json)), (ObjectReader) om.readerFor(type));
  }

  public static <T> T fromJson(Reader jsonSrc, Class<T> beanClass) {
    return fromJson(jsonSrc, getObjectReader(beanClass));
  }

  private static <T> T fromJson(Reader jsonSrc, ObjectReader or) {
    try {
      return or.readValue(jsonSrc);
    } catch (IOException var3) {
      throw new RuntimeException("JSON parsing error", var3);
    }
  }

  public static String toJson(Object bean) {
    return toJson(bean, false);
  }

  public static String toJson(Object bean, boolean prettyPrint) {
    return toJson(getObjectWriter(bean.getClass()), bean, prettyPrint);
  }

  public static void toJson(Writer w, Object bean, boolean prettyPrint) {
    toJson(getObjectWriter(bean.getClass()), w, bean, prettyPrint);
  }

  private static String toJson(ObjectWriter ow, Object bean, boolean prettyPrint) {
    if (prettyPrint) {
      ow = ow.withDefaultPrettyPrinter();
    }

    try {
      return ow.writeValueAsString(bean);
    } catch (IOException var4) {
      throw new RuntimeException("JSON parsing error", var4);
    }
  }

  private static void toJson(ObjectWriter ow, Writer w, Object bean, boolean prettyPrint) {
    if (prettyPrint) {
      ow = ow.withDefaultPrettyPrinter();
    }

    try {
      ow.writeValue(w, bean);
    } catch (IOException var5) {
      throw new RuntimeException("JSON parsing error", var5);
    }
  }
}
