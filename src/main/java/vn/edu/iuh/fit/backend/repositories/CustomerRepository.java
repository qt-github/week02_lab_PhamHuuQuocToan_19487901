package vn.edu.iuh.fit.backend.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.edu.iuh.fit.backend.entities.Customer;
import vn.edu.iuh.fit.backend.entities.Order;

import java.util.List;

public class CustomerRepository {
    private final EntityManager em;
    private final EntityTransaction trans;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public CustomerRepository() {
        em = Persistence.createEntityManagerFactory("lab_week_2").createEntityManager();
        trans = em.getTransaction();
    }

    public Customer getCustomerById(long id) {
        return em.find(Customer.class, id);
    }

    public void insertCust(Customer customer) {
        executeTransaction(() -> em.persist(customer));
    }

    public List<Customer> getAllCust() {
        return em.createQuery("select c from Customer c", Customer.class).getResultList();
    }

    public void updateCust(Customer customer) {
        try {
            executeTransaction(() -> em.merge(customer));
        } catch (Exception ex) {
            throw new RuntimeException("Transaction failed", ex);
        }
    }

    /**
     * Xóa một thực thể Customer khỏi cơ sở dữ liệu bằng ID của nó.
     *
     * @param id ID của thực thể Customer cần xóa.
     * @return true nếu thực thể Customer đã được xóa thành công, false nếu ngược lại.
     */
    public boolean deleteCust(long id) {
        Customer customer = getCustomerById(id);
        if (customer != null) {
            try {
                executeTransaction(() -> {
                    // Delete or reassign all orders associated with the customer
                    for (Order order : customer.getOrderList()) {
                        em.remove(order);
                    }
                    // Now we can safely delete the customer
                    em.remove(customer);
                });
                return true;
            } catch (Exception ex) {
                throw new RuntimeException("Transaction failed", ex);
            }
        }
        return false;
    }


    /**
     * Thực thi một giao dịch với hành động đã cho.
     *
     * @param action Hành động cần được thực thi trong giao dịch.
     */
    private void executeTransaction(Runnable action) {
        try {
            trans.begin();
            action.run();
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();
            logger.error(ex.getMessage());
            throw new RuntimeException("Transaction failed", ex);
        }
    }
}