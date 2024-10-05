package com.example.demo1.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Orders {

    @Id
    @GeneratedValue
    private Long idx;

    
}
