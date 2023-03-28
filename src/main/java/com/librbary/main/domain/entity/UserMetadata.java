package com.librbary.main.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

/**
 * Entity class to store metadata associated to Users
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_metadata")
public class UserMetadata {
  @Id
  @Column(name = "usr_metadata_id", nullable = false)
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID userMetadataId;

  @Column(name = "usr_phone")
  private String userPhoneNumber;

  @Column(name = "usr_email")
  private String userEmailId;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "usr_address_id")
  private Address userAddress;

}
