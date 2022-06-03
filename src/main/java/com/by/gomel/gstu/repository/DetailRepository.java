package com.by.gomel.gstu.repository;

import com.by.gomel.gstu.model.AnalogGroup;
import com.by.gomel.gstu.model.Category;
import com.by.gomel.gstu.model.Detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailRepository extends JpaRepository<Detail, Long> {

    List<Detail> findAll();

    List<Detail> getDetailsByCategory(Category category);

    Detail getDetailByDetailVendorCode(String vendorCode);

    List<Detail> getDetailsByAnalogGroup(AnalogGroup analogGroup);
}
