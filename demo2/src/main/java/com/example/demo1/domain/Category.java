package com.example.demo1.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Category {

    @Id @GeneratedValue
    private Long idx;

    private String categoryName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Category(String categoryName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.categoryName = categoryName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
