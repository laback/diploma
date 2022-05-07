package com.example.application.model;

import com.example.application.model.converter.HashMapConverter;
import lombok.Data;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "details")
@Data
public class Detail extends BaseEntity{

    @ManyToOne(targetEntity = Category.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "detail_category_id")
    private Category category;

    @Column(name = "detail_vendor_code")
    private String detailVendorCode;

    @Column(name = "detail_name")
    private String detailName;

    @Column(name = "detail_description")
    private String detailDescription;

    @Convert(converter = HashMapConverter.class)
    @Column(name = "detail_attributes")
    private Map<String, String> detailAttributes = new HashMap<>();

    @Column(name = "detail_count")
    private int detailCount;

    @Column(name = "detail_cost")
    private float detailCost;

    @ManyToOne(targetEntity = AnalogGroup.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "analog_group_id")
    private AnalogGroup analogGroup;
}
