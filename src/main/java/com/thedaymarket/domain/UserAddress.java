package com.thedaymarket.domain;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class UserAddress {
  String addressLine1;
  String city;
  String postCode;
}
