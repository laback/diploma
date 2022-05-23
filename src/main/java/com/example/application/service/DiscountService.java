package com.example.application.service;

import com.example.application.model.Discount;
import com.example.application.model.User;
import com.example.application.repository.DiscountRepository;
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
