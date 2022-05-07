package com.example.application.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "discounts")
@Data
public class Discount extends BaseEntity{

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private float discount;
}
