package com.example.application.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
public class Category extends BaseEntity{

    @Transient
    private int detailsCount;

    @Column(name = "category_name")
    private String categoryName;

    @ManyToOne(targetEntity = Category.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_parent_id")
    private Category parentCategory;

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public Category() {
    }
}
