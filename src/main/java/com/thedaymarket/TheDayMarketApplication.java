package com.thedaymarket;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.thedaymarket.utils.DateUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@EnableScheduling
@SpringBootApplication
public class TheDayMarketApplication {

  @PostConstruct
  public void init() {
    TimeZone.setDefault(DateUtils.getTheMarketTimeZone());
  }

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
    return builder -> {
      builder.serializers(new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
      builder.deserializers(new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
      builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
      builder.deserializers(new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
      builder.serializers(
          new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
      builder.deserializers(
          new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
      builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      builder.serializationInclusion(JsonInclude.Include.NON_EMPTY);
    };
  }

  public static void main(String[] args) {
    SpringApplication.run(TheDayMarketApplication.class, args);
  }
}
