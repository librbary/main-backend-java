package com.librbary.main.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;
import java.util.UUID;

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

    @Column(name = "usr_create_ts", nullable = false)
    private Instant userCreateTime;

    @Column(name = "usr_expr_ts", nullable = false)
    private Instant userExpireTime;

    @Column(name = "usr_status", nullable = false)
    private String userStatus;
}
