package com.example.application.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "analoggroups")
@Data
public class AnalogGroup extends BaseEntity{

    private String analogGroupName;
}
