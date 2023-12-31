package vn.edu.iuh.fit.backend.repositories;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.edu.iuh.fit.backend.dto.OrderDTO;
import vn.edu.iuh.fit.backend.entities.Order;
import vn.edu.iuh.fit.backend.entities.OrderDetail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@FunctionalInterface //explain: https://www.geeksforgeeks.org/functional-interfaces-java/
interface ResultSupplier<T> {
    T get() throws PersistenceException;
}

public class OrderRepository {
    private final EntityManager em;
    private final EntityTransaction trans;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public OrderRepository() {
        em = Persistence.createEntityManagerFactory("lab_week_2").createEntityManager();
        trans = em.getTransaction();
    }

    public boolean insertOrder(Order orders, OrderDetail orderDetail) {
        return executeTransaction(() -> {
            em.persist(orders);
            em.persist(orderDetail);
        });
    }

    public boolean updateOrder(Order orders) {
        return executeTransaction(() -> em.merge(orders));
    }

    public Optional<Order> findOrder(long id) {
        TypedQuery<Order> query = em.createQuery("select o from Order o where o.order_id=:id", Order.class);
        query.setParameter("id", id);
        Order orders = query.getSingleResult();
        return orders == null ? Optional.empty() : Optional.of(orders);
    }

    public boolean deleteOrders(long id) {
        Optional<Order> op = findOrder(id);
        Order orders = op.isPresent() ? op.get() : null;
        if (orders == null) return false;
        return executeTransaction(() -> em.remove(orders));
    }

    public List<Order> getAllOrders() {
        return executeTransactionWithResult(() -> em.createNativeQuery("SELECT * from orders ", Order.class).getResultList());
    }

    /**
     * Truy xuất thống kê đơn hàng được nhóm theo ngày từ cơ sở dữ liệu.
     *
     * @return Danh sách các mảng đối tượng, mỗi mảng chứa ngày đặt hàng, số lượng đơn hàng và tổng số tiền cho ngày đó.
     */
    public List<Object[]> getOrderStatisticsByDate() {
        return executeTransactionWithResult(() -> {
            String queryString = "SELECT FUNCTION('DATE', o.orderDate) AS orderDay, COUNT(o) AS orderCount, SUM(od.price * od.quantity) AS totalAmount " +
                    "FROM Order o JOIN o.orderDetails od " +
                    "GROUP BY orderDay " +
                    "ORDER BY FUNCTION('DATE', o.orderDate)";
            return em.createQuery(queryString).getResultList();
        });
    }

    /**
     * Truy xuất thống kê đơn hàng được nhóm theo ngày trong một khoảng thời gian cụ thể từ cơ sở dữ liệu.
     *
     * @param startDate Ngày bắt đầu của khoảng thời gian.
     * @param endDate Ngày kết thúc của khoảng thời gian.
     * @return Danh sách các mảng đối tượng, mỗi mảng chứa ngày đặt hàng, số lượng đơn hàng và tổng số tiền cho ngày đó trong khoảng thời gian đã chỉ định.
     */
    public List<Object[]> getOrderStatisticsByDateRange(LocalDate startDate, LocalDate endDate) {
        return executeTransactionWithResult(() -> em.createQuery(
                "SELECT FUNCTION('DATE', o.orderDate) AS orderDay, COUNT(o) AS orderCount, SUM(od.price * od.quantity) AS totalAmount " +
                        "FROM Order o JOIN o.orderDetails od " +
                        "WHERE FUNCTION('DATE', o.orderDate) BETWEEN :startDate AND :endDate " +
                        "GROUP BY orderDay " +
                        "ORDER BY FUNCTION('DATE', o.orderDate)"
        ).setParameter("startDate", startDate).setParameter("endDate", endDate).getResultList());
    }

    /**
     * Truy xuất thống kê đơn hàng được nhóm theo nhân viên và ngày trong một khoảng thời gian cụ thể từ cơ sở dữ liệu.
     *
     * @param employeeId ID của đối tượng Nhân viên để sử dụng cho việc tìm kiếm.
     * @param startDate Ngày bắt đầu của khoảng thời gian.
     * @param endDate Ngày kết thúc của khoảng thời gian.
     * @return Danh sách các mảng đối tượng, mỗi mảng chứa tên nhân viên, số lượng đơn hàng và tổng số tiền cho nhân viên đó trong khoảng thời gian đã chỉ định.
     */
    public List<Object[]> getOrderStatisticsByEmployeeAndDateRange(Long employeeId, LocalDate startDate, LocalDate endDate) {
        return executeTransactionWithResult(() -> em.createQuery(
                        "SELECT e.fullname AS employeeName, COUNT(o) AS orderCount, " +
                                "SUM(od.price * od.quantity) AS totalAmount " +
                                "FROM Order o " +
                                "JOIN o.employee e " +
                                "JOIN o.orderDetails od " +
                                "WHERE e.id = :employeeId " +
                                "AND FUNCTION('DATE', o.orderDate) BETWEEN :startDate AND :endDate " +
                                "GROUP BY employeeName " +
                                "ORDER BY employeeName"
                ).setParameter("employeeId", employeeId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList());
    }

    /**
     * Tìm kiếm thông tin về tất cả các đơn hàng từ cơ sở dữ liệu.
     *
     * @return Danh sách các đối tượng OrderDTO chứa thông tin về mỗi đơn hàng.
     */
    public List<OrderDTO> getInfoOrder() {
        return executeTransactionWithResult(() -> {
            List<Order> list = em.createQuery("select distinct o from Order o join o.orderDetails od order by o.order_id asc ", Order.class)
                    .getResultList();

            return list.stream().map(order -> {
                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setOrderId(order.getOrder_id());
                orderDTO.setOrderDate(order.getOrderDate());
                orderDTO.setNote(order.getOrderDetails().get(0).getNote());
                orderDTO.setCusId(order.getCustomer().getId());
                orderDTO.setEnoId(order.getEmployee().getId());
                orderDTO.setEmployeeName(order.getEmployee().getFullname());
                orderDTO.setCustomerName(order.getCustomer().getName());

                OrderDetail orderDetail = order.getOrderDetails().get(0);
                orderDTO.setProductId(orderDetail.getProduct().getProduct_id());
                orderDTO.setProductName(orderDetail.getProduct().getName());
                orderDTO.setQuantity(orderDetail.getQuantity());
                orderDTO.setPrice(orderDetail.getPrice());

                return orderDTO;
            }).collect(Collectors.toList());
        });
    }

    /**
     * Truy xuất một đối tượng Order từ cơ sở dữ liệu sử dụng các đối tượng Customer và Employee đã cho.
     *
     * @param customerId ID của đối tượng Customer để sử dụng cho việc tìm kiếm.
     * @param employeeId ID của đối tượng Employee để sử dụng cho việc tìm kiếm.
     * @return Một Optional chứa đối tượng Order nếu tìm thấy, hoặc một Optional trống nếu không tìm thấy.
     */
    public Optional<Order> findOrderByCustomerAndEmployee(Long customerId, Long employeeId) {
        String queryString = "SELECT o FROM Order o WHERE o.customer.id = :customerId AND o.employee.id = :employeeId";
        TypedQuery<Order> query = em.createQuery(queryString, Order.class);
        query.setParameter("customerId", customerId);
        query.setParameter("employeeId", employeeId);
        return Optional.ofNullable(query.getSingleResult());
    }

    private boolean executeTransaction(Runnable action) {
        try {
            trans.begin();
            action.run();
            trans.commit();
            return true;
        } catch (Exception e) {
            logger.info(e.getMessage());
            trans.rollback();
            return false;
        }
    }

    private <T> T executeTransactionWithResult(ResultSupplier<T> action) {
        try {
            trans.begin();
            T result = action.get();
            trans.commit();
            return result;
        } catch (Exception e) {
            logger.info(e.getMessage());
            trans.rollback();
            return null;
        }
    }
}