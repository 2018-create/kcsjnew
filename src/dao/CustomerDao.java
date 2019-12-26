package dao;

import domain.Customer;
import helper.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public final class CustomerDao {
    private static CustomerDao customerDao = new CustomerDao();

    private CustomerDao() {
    }

    public static CustomerDao getInstance() {
        return customerDao;
    }

    //查询全部客户信息
    public Collection<Customer> findAll() throws SQLException {
        //定义集合
        Set<Customer> customers = new HashSet<Customer>();
        //建立连接
        Connection connection = JdbcHelper.getConn();
        //创建语句盒子
        Statement statement = connection.createStatement();
        //执行预编译语句
        ResultSet resultSet = statement.executeQuery("select * from custinfo");
        //定义while循环体，如果下一条记录不为空，创建Customer对象并令customer指向它
        while (resultSet.next()) {
            Customer customer = new Customer(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("pid"),
                    resultSet.getString("phone"),
                    resultSet.getString("address")
            );
            customers.add(customer);
        }
        //关闭资源
        JdbcHelper.close(resultSet, statement, connection);
        return customers;
    }

    //添加新客户
    public boolean add(Customer customer) throws SQLException {
        //建立连接
        Connection connection = JdbcHelper.getConn();
        //创建语句盒子并执行sql语句
        PreparedStatement preparedStatement = connection.prepareStatement(
                "insert into custinfo (name,pid,phone,address) values (?,?,?,?)");
        //为相应参数赋值
        preparedStatement.setString(1, customer.getName());
        preparedStatement.setString(2, customer.getPid());
        preparedStatement.setString(3, customer.getPhone());
        preparedStatement.setString(4, customer.getAddress());
        int affectedRowNum = preparedStatement.executeUpdate();
        //关闭资源
        JdbcHelper.close(preparedStatement, connection);
        return affectedRowNum > 0;
    }

    //更新客户信息
    public boolean update(Customer customer) throws SQLException {
        //建立连接
        Connection connection = JdbcHelper.getConn();
        //创建语句盒子并执行sql语句
        PreparedStatement preparedStatement = connection.prepareStatement("update custinfo set name = ?,pid = ?,phone = ?,address = ? where id = ?");
        //为相应参数赋值
        preparedStatement.setString(1, customer.getName());
        preparedStatement.setString(2, customer.getPid());
        preparedStatement.setString(3, customer.getPhone());
        preparedStatement.setString(4, customer.getAddress());
        preparedStatement.setInt(5, customer.getId());
        int affectedRowNum = preparedStatement.executeUpdate();
        return affectedRowNum > 0;
    }

    //按id查询单个客户信息
    public Customer find(Integer id) throws SQLException {
        Customer customer = null;
        //建立连接
        Connection connection = JdbcHelper.getConn();
        //创建语句盒子
        PreparedStatement preparedStatement = connection.prepareStatement("select * from custinfo where id = ?");
        //为预编译语句参数赋值
        preparedStatement.setInt(1, id);
        //执行预编译语句
        ResultSet resultSet = preparedStatement.executeQuery();
        //如果下一条记录不为空则执行循环
        if (resultSet.next()) {
            customer = new Customer(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("pid"),
                    resultSet.getString("phone"),
                    resultSet.getString("address")
            );
        }
        //关闭资源
        JdbcHelper.close(resultSet, preparedStatement, connection);
        return customer;
    }

    //删除客户
    public boolean delete(Integer id) throws SQLException {
        //建立连接
        Connection connection = JdbcHelper.getConn();
        //创建语句盒子并执行sql语句
        PreparedStatement preparedStatement = connection.prepareStatement("delete from custinfo where id = ?");
        //为预编译语句参数赋值
        preparedStatement.setInt(1, id);
        int affectedRowNum = preparedStatement.executeUpdate();
        //关闭资源
        JdbcHelper.close(preparedStatement, connection);
        return affectedRowNum > 0;
    }
}
