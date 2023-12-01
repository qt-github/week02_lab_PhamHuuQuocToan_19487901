package vn.edu.iuh.fit.backend.repositories;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.edu.iuh.fit.backend.entities.Order;
import vn.edu.iuh.fit.backend.entities.OrderDetail;
import vn.edu.iuh.fit.backend.entities.Product;

import java.util.List;
import java.util.Optional;

public class OrderDetailsRepository {
    private final EntityManager em;
    private final EntityTransaction trans;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public OrderDetailsRepository() {
        em = Persistence.createEntityManagerFactory("lab_week_2").createEntityManager();
        trans = em.getTransaction();
    }

    public boolean insertOrderDetail(OrderDetail orderDetail) {
        return executeTransaction(() -> em.persist(orderDetail));
    }


    public List<OrderDetail> findOrderDetail(long OrderID) {
        return executeTransactionWithResult(() -> {
            TypedQuery<OrderDetail> query = em.createQuery("select od from OrderDetail od where od.order.order_id =:OrderID", OrderDetail.class);
            query.setParameter("OrderID", OrderID);
            return query.getResultList();
        });
    }

    /**
     * Truy xuất tất cả các thực thể OrderDetail từ cơ sở dữ liệu.
     *
     * @return Danh sách tất cả các thực thể OrderDetail trong cơ sở dữ liệu.
     */
    public List<OrderDetail> getAllOrderDetails() {
        return executeTransactionWithResult(() -> {
            String query = "SELECT od.*, pp.price AS latest_price, od.product_id, od.order_id " +
                    "FROM order_detail od " +
                    "INNER JOIN (SELECT product_id, MAX(price_date_time) AS latest_date " +
                    "            FROM product_price " +
                    "            GROUP BY product_id) latest_prices " +
                    "ON od.product_id = latest_prices.product_id " +
                    "INNER JOIN product_price pp " +
                    "ON od.product_id = pp.product_id AND latest_prices.latest_date = pp.price_date_time";

            return em.createNativeQuery(query, OrderDetail.class).getResultList();
        });
    }

    /**
     * Truy xuất một thực thể OrderDetail từ cơ sở dữ liệu sử dụng các thực thể Order và Product đã cho.
     *
     * @param order Thực thể Order để sử dụng cho việc tìm kiếm.
     * @param product Thực thể Product để sử dụng cho việc tìm kiếm.
     * @return Một Optional chứa thực thể OrderDetail nếu tìm thấy, hoặc một Optional trống nếu không tìm thấy.
     */
    public Optional<OrderDetail> findOrderDetailByOrderAndProduct(Order order, Product product) {
        return executeTransactionWithResult(() -> {
            String queryString = "SELECT od FROM OrderDetail od WHERE od.order = :order AND od.product = :product";
            TypedQuery<OrderDetail> query = em.createQuery(queryString, OrderDetail.class);
            query.setParameter("order", order);
            query.setParameter("product", product);
            return Optional.ofNullable(query.getSingleResult());
        });
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