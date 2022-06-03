package com.by.gomel.gstu.service;

import com.by.gomel.gstu.model.Order;
import com.by.gomel.gstu.model.User;
import com.by.gomel.gstu.repository.DetailRepository;
import com.by.gomel.gstu.repository.OrderRepository;
import com.by.gomel.gstu.util.PageUtils;
import com.by.gomel.gstu.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final DetailRepository detailRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserService userService;
    private final DiscountService discountService;

    @Autowired
    public OrderService(OrderRepository orderRepository, DetailRepository detailRepository, OrderDetailRepository orderDetailRepository, UserService userService, DiscountService discountService) {
        this.orderRepository = orderRepository;
        this.detailRepository = detailRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.userService = userService;
        this.discountService = discountService;
    }

    public List<Order> getOrderByParams(String... params){
        var orders = getAllOrders();

        if(params[0] != null && !params[0].isBlank()){
            orders = orders.stream().filter(order -> order.getUser().getSurName().contains(params[0])).collect(Collectors.toList());
        }

        if(params[1] != null && !params[1].isBlank()){
            orders = orders.stream().filter(order -> order.getEmployee().getSurName().contains(params[1])).collect(Collectors.toList());
        }

        if(params[2] != null && !params[2].isBlank()){
            orders = orders.stream().filter(order -> order.getCreated().toLocalDate().toString().equals(params[2])).collect(Collectors.toList());
        }

        return orders;
    }

    public List<Order> getAllOrders(int page, List<Order> orders){

        return PageUtils.getAllEntitiesByPage(orders, page);
    }

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public long getMaxPages(List<Order> orders){
        return PageUtils.getMaxPages(orders);
    }

    public Order addOrder(Order order){
        order.setCreated(LocalDateTime.now());
        if(order.getEmployee() == null){
            order.setEmployee((User) userService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        }
        return orderRepository.save(order);
    }

    public void save(Order order){
        var discount = discountService.getDiscountByUser(order.getUser());

        if(discount.getDiscount() < order.getDiscount()){
            order.setDiscount(discount.getDiscount());
        }

        discount.setDiscount(discount.getDiscount() - order.getDiscount());

        discountService.save(discount);

        orderRepository.save(order);
    }

    public Order getOrderById(Long id){
        return orderRepository.getById(id);
    }

    public void issueOrder(Long id){
        var order = orderRepository.getById(id);

        var discount = discountService.getDiscountByUser(order.getUser());

        discount.setDiscount(discount.getDiscount() + (order.getOrderCost() / 100));

        discountService.save(discount);

        order.setIssued(true);

        orderRepository.save(order);
    }

    public void save(List<Order> orders){
        orderRepository.saveAll(orders);
    }

    public List<Order> getTodaysOrder(){
        return orderRepository.getAllByPlannedIssueDate(LocalDate.now());
    }
}
