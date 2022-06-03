package com.by.gomel.gstu.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "analoggroups")
@Data
public class AnalogGroup extends BaseEntity{

    private String analogGroupName;

    public AnalogGroup(String analogGroupName) {
        this.analogGroupName = analogGroupName;
    }

    public AnalogGroup() {
    }
}
