package com.by.gomel.gstu.repository;

import com.by.gomel.gstu.model.Order;
import com.by.gomel.gstu.model.OrdersDetail;
import com.by.gomel.gstu.viewModel.SailedDetailsViewModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrdersDetail, Long> {

    List<OrdersDetail> getAllByOrder(Order order);

    @Query("select new com.by.gomel.gstu.viewModel.SailedDetailsViewModel(a.detail, sum(a.count)) from ordersdetails a where a.order in (select b from orders b where b.created between ?1 and ?2) group by a.detail")
    List<SailedDetailsViewModel> getForSailedDetailsReport(LocalDateTime beginDate, LocalDateTime endDate);
}
