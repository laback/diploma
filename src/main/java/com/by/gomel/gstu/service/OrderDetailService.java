package com.by.gomel.gstu.service;

import com.by.gomel.gstu.model.Detail;
import com.by.gomel.gstu.model.Order;
import com.by.gomel.gstu.model.OrdersDetail;
import com.by.gomel.gstu.repository.DetailRepository;
import com.by.gomel.gstu.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderDetailService {

    private final OrderDetailRepository repository;
    private final DetailRepository detailRepository;

    @Autowired
    public OrderDetailService(OrderDetailRepository repository, DetailRepository detailRepository) {
        this.repository = repository;
        this.detailRepository = detailRepository;
    }

    public List<OrdersDetail> getAll(){
        return repository.findAll();
    }

    public List<Detail> getDetailsByOrderId(Order order){
        var detailsInOrder = repository.getAllByOrder(order);

        return detailsInOrder.stream().map(OrdersDetail::getDetail).collect(Collectors.toList());
    }

    @Transactional
    public float setDetailsInOrder(String expression, Order order){
        var pairs = expression.split(",");

        List<OrdersDetail> result = new ArrayList<>();

        float cost = 0;

        for(int i = 0; i < pairs.length; i += 2){

            int count = Integer.parseInt(pairs[i + 1]);

            var detail = detailRepository.getById(Long.parseLong(pairs[i]));
            detail.setDetailCount(detail.getDetailCount() - count);
            detail = detailRepository.save(detail);

            cost += detail.getDetailCost() *  count;

            result.add(new OrdersDetail(order, detail, count));
        }

        repository.saveAll(result);

        return cost;
    }
}
