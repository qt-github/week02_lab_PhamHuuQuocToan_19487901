package vn.edu.iuh.fit.backend.services;


import vn.edu.iuh.fit.backend.entities.Order;
import vn.edu.iuh.fit.backend.entities.OrderDetail;
import vn.edu.iuh.fit.backend.entities.Product;
import vn.edu.iuh.fit.backend.repositories.OrderDetailsRepository;

import java.util.List;
import java.util.Optional;

public class OrderDetailService {
    private final OrderDetailsRepository repo = new OrderDetailsRepository();

    public OrderDetailService() {

    }

    public boolean insertOrderDetail(OrderDetail orderDetail) {
        return repo.insertOrderDetail(orderDetail);
    }


    public List<OrderDetail> findOrderDetail(long OrderID) {
        return repo.findOrderDetail(OrderID);
    }

    public Optional<OrderDetail> findOrderDetailByOrderAndProduct(Order order, Product product) {
        return repo.findOrderDetailByOrderAndProduct(order, product);
    }


    public List<OrderDetail> getAllOrderDetails() {
        return repo.getAllOrderDetails();
    }
}
