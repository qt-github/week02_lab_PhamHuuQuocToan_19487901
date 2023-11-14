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
        try {
            trans.begin();
            em.persist(employee);
            trans.commit();
            return true;
        } catch (Exception e) {
            logger.info(e.getMessage());
            trans.rollback();
        }
        return false;
    }

    public boolean updateEmployee(Employee employee) {
        try {
            trans.begin();
            em.merge(employee);
            trans.commit();
            return true;
        } catch (Exception e) {
            logger.info(e.getMessage());
            trans.rollback();
        }
        return false;
    }

    public Optional<Employee> findEmployee(long id) {
        TypedQuery<Employee> query = em.createQuery("select e from Employee e where e.id=:id", Employee.class);
        query.setParameter("id", id);
        Employee employee = query.getSingleResult();
        return employee == null ? Optional.empty() : Optional.of(employee);
    }

    public boolean deleteEmployee(long id) {
        try {
            trans.begin();
            Optional<Employee> op = findEmployee(id);
            Employee employee = op.orElse(null);
            if (employee != null) {
                employee.setStatus(EmployeeStatus.DELETE);
                em.merge(employee);
            }
            trans.commit();
            return true;

        } catch (Exception e) {
            logger.info(e.getMessage());
            trans.rollback();
        }
        return false;
    }
    public List<Employee> getAllEmployee(){
        try {
            trans.begin();
            List<Employee> list= em.createNativeQuery("Select * from employee order by full_name ", Employee.class).getResultList();
            trans.commit();
            return list;
        }catch (Exception e){
            logger.info(e.getMessage());
            trans.rollback();
        }
        return null;
    }
    public List<Employee> getActiveEmployee(){
       try{
            List<Employee> list=em.createQuery("Select e from Employee e where e.status=:status",Employee.class)
                    .setParameter("status",EmployeeStatus.ACTIVE)
                    .getResultList();
            trans.commit();
            return list;
        }catch (Exception e){
            logger.info(e.getMessage());
            trans.rollback();
        }
        return null;
    }
}
