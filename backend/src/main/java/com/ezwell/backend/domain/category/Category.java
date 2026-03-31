package com.ezwell.backend.domain.category;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String status;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Category(String name, String status, int displayOrder) {
        this.name = name;
        this.status = status;
        this.displayOrder = displayOrder;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public void update(String name, String status, Integer displayOrder) {
        if (name != null) {
            this.name = name;
        }
        if (status != null) {
            this.status = status;
        }
        if (displayOrder != null) {
            this.displayOrder = displayOrder;
        }
    }
}