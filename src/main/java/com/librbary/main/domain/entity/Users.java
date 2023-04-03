package com.librbary.main.domain.entity;

import com.librbary.main.domain.Location;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity class to store User credentials and Details
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class Users {
  @Id
  @Column(name = "usr_id", nullable = false)
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID userId;

  @Column(name = "usr_name", nullable = false)
  private String userName;

  @Column(name = "usr_passwrd", nullable = false)
  private String userPassword;

  @Column(name = "usr_location", nullable = false)
  private Location location;

  @Column(name = "usr_create_ts", nullable = false)
  private LocalDateTime userCreateTime;

  @Column(name = "usr_expr_ts", nullable = false)
  private LocalDateTime userExpireTime;

  @Column(name = "usr_status", nullable = false)
  private String userStatus;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "usr_metadata_id")
  private UserMetadata userMetadata;
}
