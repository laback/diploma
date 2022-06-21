package com.by.gomel.gstu.scheduled;

import com.by.gomel.gstu.email.EmailSender;
import com.by.gomel.gstu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Component
public class EmailNotifSender {

    private final OrderService orderService;
    private final EmailSender sender;

    @Autowired
    public EmailNotifSender(OrderService orderService, EmailSender sender) {
        this.orderService = orderService;
        this.sender = sender;
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void sendEmailNotif(){
        orderService.getTodaysOrder().forEach(order -> {
            try {
                sender.sendOrderNotif(order.getUser().getEmail(), order);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
    }


}
