package com.example.application.util;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public class PageUtils {

    private PageUtils(){}

    public static <T> long getMaxPages(JpaRepository<T, Long> repository){
        return (long) Math.ceil(repository.findAll().size() / 15);
    }

    public static <T> List<T> getAllEntitiesByPage(JpaRepository<T, Long> repository, int page){
        return repository.findAll().stream().skip(page * 15L).limit(15).collect(Collectors.toList());
    }
}
