// EmployeeRepository.java
package vn.edu.iuh.fit.backend.repositories;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.edu.iuh.fit.backend.entities.Employee;
import vn.edu.iuh.fit.backend.enums.EmployeeStatus;

import java.util.List;
import java.util.Optional;

public class EmployeeRepository {
    private final EntityManager em;
    private final EntityTransaction trans;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public EmployeeRepository() {
        em = Persistence.createEntityManagerFactory("lab_week_2").createEntityManager();
        trans = em.getTransaction();
    }

    public boolean insertEmployee(Employee employee) {
        return executeTransaction(() -> em.persist(employee));
    }

    public boolean updateEmployee(Employee employee) {
        return executeTransaction(() -> em.merge(employee));
    }

    public Optional<Employee> findEmployee(long id) {
        TypedQuery<Employee> query = em.createQuery("select e from Employee e where e.id=:id", Employee.class);
        query.setParameter("id", id);
        Employee employee = query.getSingleResult();
        return employee == null ? Optional.empty() : Optional.of(employee);
    }

    public boolean deleteEmployee(long id) {
        return executeTransaction(() -> {
            Optional<Employee> op = findEmployee(id);
            Employee employee = op.orElse(null);
            if (employee != null) {
                employee.setStatus(EmployeeStatus.DELETE);
                em.merge(employee);
            }
        });
    }

    public List<Employee> getAllEmployee() {
        return executeTransactionWithResult(() -> em.createNativeQuery("Select * from employee group by emp_id ", Employee.class).getResultList());
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

    /**
     * Thực thi một giao dịch với hành động đã cho và trả về kết quả.
     *
     * @param <T> Kiểu của kết quả.
     * @param action Hành động cần được thực thi trong giao dịch.
     * @return Kết quả của hành động nếu giao dịch thành công.
     */
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