package vn.edu.iuh.fit.backend.services;

import vn.edu.iuh.fit.backend.entities.Customer;
import vn.edu.iuh.fit.backend.repositories.CustomerRepository;

import java.util.List;
import java.util.Optional;

public class CustomerService {
    private final CustomerRepository repository = new CustomerRepository();

    public void insertCust(Customer customer) {
        repository.insertCust(customer);
    }

    public Optional<Customer> findCustomer(long id) {
        return Optional.ofNullable(repository.getCustomerById(id));
    }

    public void updateCust(Customer customer) {
        repository.updateCust(customer);
    }

    public void deleteCust(long id) {
        repository.deleteCust(id);
    }

    public List<Customer> getAll() {
        return repository.getAllCust();
    }
}