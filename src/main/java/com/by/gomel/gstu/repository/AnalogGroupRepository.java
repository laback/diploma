package com.by.gomel.gstu.repository;

import com.by.gomel.gstu.model.AnalogGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalogGroupRepository extends JpaRepository<AnalogGroup, Long> {

    AnalogGroup getAnalogGroupByAnalogGroupName(String analogGroupName);
}
