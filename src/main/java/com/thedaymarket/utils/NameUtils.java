package com.thedaymarket.utils;

public final class NameUtils {
  private NameUtils() {}

  public static String getFullName(String firstName, String lastName) {
    if (firstName == null) {
      return lastName;
    }

    if (lastName == null) {
      return firstName;
    }

    return firstName + lastName;
  }
}
