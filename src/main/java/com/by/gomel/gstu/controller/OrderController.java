package com.by.gomel.gstu.controller;

import com.by.gomel.gstu.model.Order;
import com.by.gomel.gstu.service.DetailService;
import com.by.gomel.gstu.service.OrderDetailService;
import com.by.gomel.gstu.service.OrderService;
import com.by.gomel.gstu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final DetailService detailService;
    private final OrderDetailService orderDetailService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService, DetailService detailService, OrderDetailService orderDetailService) {
        this.orderService = orderService;
        this.userService = userService;
        this.detailService = detailService;
        this.orderDetailService = orderDetailService;
    }

    @GetMapping("/user/order/{page}")
    public String getAllDetails(Model model, @PathVariable int page, @Param(value = "customerSurname")String customerSurname, @Param(value = "employeeSurname")String employeeSurname, @Param(value = "orderDate") String orderDate) {

        var orders = orderService.getOrderByParams(customerSurname, employeeSurname, orderDate);

        model.addAttribute("orders", orderService.getAllOrders(page, orders));

        model.addAttribute("page", page);
        model.addAttribute("maxPages", orderService.getMaxPages(orderService.getAllOrders(page, orders)));

        return "orders/index";
    }

    @GetMapping("user/order/add")
    public String getAddForm(Model model, @ModelAttribute(name = "order") Order order){
        model.addAttribute("users", userService.getAll());
        model.addAttribute("details", detailService.getAllDetailsInStock());


        return "orders/add";
    }

    @PostMapping("user/order")
    public String addOrder(@ModelAttribute("order") Order order){

        String expression = order.getDetails();

        order = orderService.addOrder(order);

        float cost = orderDetailService.setDetailsInOrder(expression, order);

        order.setOrderCost(cost);

        orderService.save(order);

        return "redirect:/user/order/0";
    }

    @GetMapping("user/order/getOrder/{id}")
    public String getOrder(@PathVariable long id, Model model){
        model.addAttribute("order", orderService.getOrderById(id));

        model.addAttribute("details", detailService.getDetailsByOrderId(id));

        return "orders/order";
    }

    @GetMapping("user/order/issue/{id}")
    public String issueOrder(@PathVariable long id){
        orderService.issueOrder(id);

        return "redirect:/user/order/0";
    }
}
