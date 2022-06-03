package com.by.gomel.gstu.service;

import com.by.gomel.gstu.repository.AnalogGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalogGroupService {

    private final AnalogGroupRepository analogGroupRepository;

    @Autowired
    public AnalogGroupService(AnalogGroupRepository analogGroupRepository) {
        this.analogGroupRepository = analogGroupRepository;
    }
}
