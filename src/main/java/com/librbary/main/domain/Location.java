package com.librbary.main.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * All available locations and pincode listed
 */

@Getter
@AllArgsConstructor
public enum Location {
  HALDWANI("263139"), DEHRADUN("263114");

  private final String pincode;

  public static Location fromPincode(String pincode) {
    return Arrays.stream(Location.values()).filter(location -> location.getPincode().equals(pincode)).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid Location Code!"));
  }
}
