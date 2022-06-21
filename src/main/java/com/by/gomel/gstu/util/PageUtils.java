package com.by.gomel.gstu.util;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public class PageUtils {

    private final static long ELEMENTS_PER_PAGE = 5;

    private PageUtils(){}

    public static <T> long getMaxPages(List<T> entities){
        if(entities.size() == 5){
            return 0;
        }
        return (long) Math.ceil(entities.size() / ELEMENTS_PER_PAGE);
    }

    public static <T> List<T> getAllEntitiesByPage(List<T> entities, int page){
        return entities.stream().skip(page * ELEMENTS_PER_PAGE).limit(ELEMENTS_PER_PAGE).collect(Collectors.toList());
    }
}
