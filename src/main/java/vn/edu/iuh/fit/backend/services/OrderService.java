package vn.edu.iuh.fit.backend.services;


import vn.edu.iuh.fit.backend.dto.OrderDTO;
import vn.edu.iuh.fit.backend.entities.Order;
import vn.edu.iuh.fit.backend.entities.OrderDetail;
import vn.edu.iuh.fit.backend.repositories.OrderRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class OrderService {
    private final OrderRepository repo = new OrderRepository();

    public OrderService() {

    }

    public boolean insertOrder(Order orders, OrderDetail orderDetail) {
        return repo.insertOrder(orders, orderDetail);
    }

    public boolean updateOrder(Order orders) {
        return repo.updateOrder(orders);
    }

    public Optional<Order> findOrder(long id) {
        return repo.findOrder(id);
    }

    public boolean deleteOrders(long id) {
        return repo.deleteOrders(id);
    }

    public List<Order> getAllOrders() {
        return repo.getAllOrders();
    }
    public List<OrderDTO> getInfoOrder() {
        return repo.getInfoOrder();
    }
    public List<Object[]> getOrderStatisticsByDate() {
        return repo.getOrderStatisticsByDate();
    }

    public List<Object[]> getOrderStatisticsByDateRange(LocalDate startDate, LocalDate endDate) {
        return repo.getOrderStatisticsByDateRange(startDate, endDate);
    }

    public List<Object[]> getOrderStatisticsByEmployeeAndDateRange(Long employeeId, LocalDate startDate, LocalDate endDate) {
        return repo.getOrderStatisticsByEmployeeAndDateRange(employeeId, startDate, endDate);
    }

    public Optional<Order> findOrderByCustomerAndEmployee(long cusId, long empId) {
        return repo.findOrderByCustomerAndEmployee(cusId, empId);
    }

}
