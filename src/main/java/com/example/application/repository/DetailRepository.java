package com.example.application.repository;

import com.example.application.model.Category;
import com.example.application.model.Detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailRepository extends JpaRepository<Detail, Long> {

    List<Detail> findAll();

    List<Detail> getDetailsByCategory(Category category);

    Detail getDetailByDetailVendorCode(String vendorCode);
}
