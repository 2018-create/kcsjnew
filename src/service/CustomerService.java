package service;


import dao.CustomerDao;
import domain.Customer;

import java.sql.SQLException;
import java.util.Collection;


public final class CustomerService {
    private static CustomerDao customerDao
            = CustomerDao.getInstance();
    private static CustomerService customerService
            = new CustomerService();

    private CustomerService() {
    }

    public static CustomerService getInstance() {
        return customerService;
    }

    //定义相关所需的方法(增删改查)
    public Collection<Customer> findAll() throws SQLException {
        return customerDao.findAll();
    }

    public Customer find(Integer id) throws SQLException {
        return customerDao.find(id);
    }

    public boolean update(Customer customer) throws SQLException, ClassNotFoundException {
        return customerDao.update(customer);
    }

    public boolean add(Customer customer) throws SQLException, ClassNotFoundException {
        return customerDao.add(customer);
    }

    public boolean delete(Integer id) throws SQLException, ClassNotFoundException {
        return customerDao.delete(id);
    }
}

