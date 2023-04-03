package com.librbary.main.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

/**
 * Entity class to store Users Address
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_address")
public class Address {

  @Id
  @Column(name = "usr_address_id", nullable = false)
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID userAddressId;

  @Column(name = "usr_address")
  private String userAddress;

  @Column(name = "pin_cd")
  private String pincode;
}
