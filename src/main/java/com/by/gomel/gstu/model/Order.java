package com.by.gomel.gstu.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "orders")
@Table(name = "orders")
@Data
public class Order extends BaseEntity{

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    private User employee;

    @Column(name = "order_cost")
    private float orderCost;

    @Column(name = "is_issued")
    private boolean isIssued;

    @Column(name = "discount")
    private float discount;

    @Column(name = "planned_issue_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate plannedIssueDate;

    @Transient
    private String details;

    public Order(User user, User employee) {
        this.user = user;
        this.employee = employee;
        setCreated(LocalDateTime.now());
    }

    public Order(User user, User employee, LocalDate plannedIssueDate) {
        this.user = user;
        this.employee = employee;
        setCreated(LocalDateTime.now());
        this.plannedIssueDate = plannedIssueDate;
    }

    public Order() {
    }

    @Override
    public String toString() {
        return "Покупатель =" + user.getSurName() +
                ", продавец=" + employee.getSurName() +
                ", стоимость заказ без учёта скидки=" + orderCost +
                ", скидка=" + discount +
                ", планируемая дата выдачи заказа=" + plannedIssueDate;
    }
}
