package com.by.gomel.gstu.service;

import com.by.gomel.gstu.model.Discount;
import com.by.gomel.gstu.model.User;
import com.by.gomel.gstu.repository.DiscountRepository;
import org.springframework.stereotype.Service;

@Service
public class DiscountService {

    private final DiscountRepository discountRepository;

    public DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    public Discount getDiscountByUser(User user){
        return discountRepository.findDiscountByUser(user).orElse(new Discount(user));
    }

    public void save(Discount discount){
        discountRepository.save(discount);
    }
}
