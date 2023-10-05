package com.thedaymarket.service.impl;

import com.thedaymarket.controllers.response.ReputationStatus;
import com.thedaymarket.domain.Auction;
import jakarta.persistence.criteria.*;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class AuctionSpecification implements Specification<Auction> {

  private final SearchCriteria criteria;

  public AuctionSpecification(SearchCriteria criteria) {
    this.criteria = criteria;
  }

  @Override
  public Predicate toPredicate(
      Root<Auction> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    Expression<String> selectorExpression;

    if (criteria.key().contains(".")) {
      var paths = criteria.key().split("\\.");
      selectorExpression = root.join(paths[0]).get(paths[1]);
    } else {
      selectorExpression = root.get(criteria.key());
    }
    if (criteria.operation().equalsIgnoreCase(">")) {
      return builder.greaterThanOrEqualTo(selectorExpression, criteria.value().toString());
    } else if (criteria.operation().equalsIgnoreCase("<")) {
      return builder.lessThanOrEqualTo(selectorExpression, criteria.value().toString());
    } else if (criteria.operation().equalsIgnoreCase(":")) {
      if (root.get(criteria.key()).getJavaType() == String.class) {
        return builder.like(selectorExpression, "%" + criteria.value() + "%");
      } else {
        return builder.equal(selectorExpression, criteria.value());
      }
    } else if (criteria.operation.equalsIgnoreCase("!")) {
      return builder.notEqual(selectorExpression, criteria.value());
    } else if (criteria.operation.equalsIgnoreCase("in")) {
      CriteriaBuilder.In<String> builderIn = builder.in(selectorExpression);
      for (var val : ((List<String>) criteria.value)) {
        builderIn = builderIn.value(val);
      }
      return builderIn;
    } else {
      return builder.equal(
          selectorExpression,
          castToRequiredType(selectorExpression.getJavaType(), criteria.value()));
    }
  }

  private Object castToRequiredType(Class fieldType, Object value) {
    if (fieldType.isAssignableFrom(Double.class)) {
      return value;
    } else if (fieldType.isAssignableFrom(Integer.class)) {
      return value;
    } else if (Enum.class.isAssignableFrom(fieldType)) {
      return Enum.valueOf(fieldType, value.toString());
    }
    return value;
  }

  public record SearchCriteria(String key, String operation, Object value) {}
}
