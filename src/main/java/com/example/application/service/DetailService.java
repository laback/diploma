package com.example.application.service;

import com.example.application.model.Detail;
import com.example.application.repository.CategoryRepository;
import com.example.application.repository.DetailRepository;
import com.example.application.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DetailService {

    private final CategoryRepository categoryRepository;
    private final DetailRepository detailRepository;

    @Autowired
    public DetailService(CategoryRepository categoryRepository, DetailRepository detailRepository) {
        this.categoryRepository = categoryRepository;
        this.detailRepository = detailRepository;
    }

    public List<Detail> getAllDetails(int page){
        return detailRepository.getAll();
    }

    public long getMaxPages(){
        return PageUtils.getMaxPages(detailRepository);
    }
}
