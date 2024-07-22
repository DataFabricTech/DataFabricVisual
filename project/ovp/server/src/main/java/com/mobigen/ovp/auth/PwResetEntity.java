package com.mobigen.ovp.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name = "cm_password_reset")
@EntityListeners(AuditingEntityListener.class)
public class PwResetEntity {
    @Id
    @Column(name = "id", nullable = false, length = 55)
    private String id;

    @NotNull
    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "create_date")
    @CreatedDate
    private LocalDateTime createDate;

    @NotNull
    @Column(name = "valid_time", nullable = false, length = 10)
    private String validTime;

}
