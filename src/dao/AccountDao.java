package dao;

import domain.Account;
import domain.Role;
import domain.Customer;
import helper.JdbcHelper;
import service.AccountService;
import service.CustomerService;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public final class AccountDao {
    private static AccountDao accountDao = new AccountDao();

    private AccountDao() {
    }

    public static AccountDao getInstance() {
        return accountDao;
    }

    //查询全部账户记录
    public Collection<Account> findAll() {
        Set<Account> accounts = new HashSet<Account>();
        try {
            //获得数据库连接对象
            Connection connection = JdbcHelper.getConn();
            //在该连接上创建语句盒子对象
            Statement stmt = connection.createStatement();
            //执行SQL查询语句并获得结果集对象
            ResultSet resultSet = stmt.executeQuery("select * from account");
            //若结果存在下一条，执行循环体
            while (resultSet.next()) {
                Customer customer = CustomerDao.getInstance().find(resultSet.getInt("cust_id"));
                Role role = RoleDao.getInstance().find(resultSet.getInt("role_id"));
                //创建Account对象，根据遍历结果中的id,description,no,remarks，school值
                Account account = new Account(resultSet.getInt("id"),
                        resultSet.getString("acct"),
                        resultSet.getString("passwd"),
                        resultSet.getDouble("balance"),
                        resultSet.getString("address"),
                        customer,
                        role);
                //添加到集合accounts中
                accounts.add(account);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    //查询单个账户记录
    public Account find(Integer id) throws SQLException {
        //声明一个Account类型的变量
        Account account = null;
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();
        String deleteAccount_sql = "select * from account where id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(deleteAccount_sql);
        //为预编译参数赋值
        preparedStatement.setInt(1, id);
        //执行预编译语句
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Customer customer = CustomerDao.getInstance().find(resultSet.getInt("cust_id"));
            Role role = RoleDao.getInstance().find(resultSet.getInt("role_id"));
            //创建Account对象，根据遍历结果中的id,description,no,remarks，school值
            account = new Account(resultSet.getInt("id"),
                    resultSet.getString("acct"),
                    resultSet.getString("passwd"),
                    resultSet.getDouble("balance"),
                    resultSet.getString("address"),
                    customer,
                    role);
        }
        //关闭资源
        JdbcHelper.close(resultSet, preparedStatement, connection);
        return account;
    }

    //按账号查询单个账户记录
    public Account findByAcct(int acct) throws SQLException {
        //声明一个Account类型的变量
        Account account = null;
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();
        String deleteAccount_sql = "select * from account where acct=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(deleteAccount_sql);
        //为预编译参数赋值
        preparedStatement.setInt(1, acct);
        //执行预编译语句
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Customer customer = CustomerDao.getInstance().find(resultSet.getInt("cust_id"));
            Role role = RoleDao.getInstance().find(resultSet.getInt("role_id"));
            //创建Account对象，根据遍历结果中的id,description,no,remarks，school值
            account = new Account(resultSet.getInt("id"),
                    resultSet.getString("acct"),
                    resultSet.getString("passwd"),
                    resultSet.getDouble("balance"),
                    resultSet.getString("address"),
                    customer,
                    role);
        }
        //关闭资源
        JdbcHelper.close(resultSet, preparedStatement, connection);
        return account;
    }

    //根据账号查询余额
    public double findBalanceByAcct(Integer acct) throws SQLException {
        //声明一个Account类型的变量
        double balance = 0;
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement("select balance from account where acct=?");
        //为预编译参数赋值
        preparedStatement.setInt(1, acct);
        //执行预编译语句
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            balance = resultSet.getDouble("balance");
        }
        //关闭资源
        JdbcHelper.close(resultSet, preparedStatement, connection);
        return balance;
    }

    //转账
    public boolean transfer(int outAcct, int inAcct, int amount) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean transfered = false;
        try {
            if (amount > 0 && amount <= 5000) {
                connection = JdbcHelper.getConn();
                connection.setAutoCommit(false);
                double balanceOfOutAcctBeforeTransfer = AccountService.getInstance().findBalanceByAcct(outAcct);
                double balanceOfInAcctBeforeTransfer = AccountService.getInstance().findBalanceByAcct(inAcct);
                if (balanceOfOutAcctBeforeTransfer >= amount) {
                    double balanceOfOutAcctAfterTransfer = balanceOfOutAcctBeforeTransfer - amount;
                    preparedStatement = connection.prepareStatement("update account set balance = ? where acct = ?");
                    preparedStatement.setDouble(1, balanceOfOutAcctAfterTransfer);
                    preparedStatement.setInt(2, outAcct);
                    int affectOutAcct = preparedStatement.executeUpdate();
                    double balanceOfInAcctAfterTransfer = balanceOfInAcctBeforeTransfer + amount;
                    preparedStatement = connection.prepareStatement("update account set balance = ? where acct = ?");
                    preparedStatement.setDouble(1, balanceOfInAcctAfterTransfer);
                    preparedStatement.setInt(2, inAcct);
                    int affectInAcct = preparedStatement.executeUpdate();
                    if (affectOutAcct == 1 && affectInAcct == 1) {
                        transfered = true;
                    } else {
                        connection.rollback();
                    }
                }
                connection.commit();
            }
        } catch (Exception e) {
            try {
                //回滚当前连接所做的操作
                if (connection != null) {
                    connection.rollback();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                //回复自动提交
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            JdbcHelper.close(preparedStatement, connection);
        }
        return transfered;
    }

    //添加新账户
    public boolean add(Account account) throws SQLException, ClassNotFoundException {
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();
        String addAccount_sql = "insert into account(acct,passwd,cust_id,balance,address,role_id) values(?,?,?,?,?,?)";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(addAccount_sql);
        //为预编译参数赋值
        preparedStatement.setString(1, account.getAcct());
        preparedStatement.setString(2, account.getPasswd());
        preparedStatement.setInt(3, account.getCustomer().getId());
        preparedStatement.setDouble(4, account.getBalance());
        preparedStatement.setString(5, account.getAddress());
        preparedStatement.setInt(6, account.getRole().getId());
        //执行预编译语句，获取添加记录行数并赋值给affectedRowNum
        int affectedRowNum = preparedStatement.executeUpdate();
        //System.out.println("添加了" + affectedRowNum + "行记录");
        //关闭资源
        JdbcHelper.close(preparedStatement, connection);
        return affectedRowNum > 0;
    }

    //修改账户信息
    public boolean update(Account account) throws ClassNotFoundException, SQLException {
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();
        String updateAccount_sql = " update account set acct=?,passwd=?,cust_id=?,balance=?,address=?,role_id=? where id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(updateAccount_sql);
        //为预编译参数赋值
        preparedStatement.setString(1, account.getAcct());
        preparedStatement.setString(2, account.getPasswd());
        preparedStatement.setInt(3, account.getCustomer().getId());
        preparedStatement.setDouble(4, account.getBalance());
        preparedStatement.setString(5, account.getAddress());
        preparedStatement.setInt(6, account.getRole().getId());
        preparedStatement.setInt(7, account.getId());
        //执行预编译语句，获取改变记录行数并赋值给affectedRowNum
        int affectedRows = preparedStatement.executeUpdate();
        //System.out.println("修改了" + affectedRows + "行记录");
        //关闭资源
        JdbcHelper.close(preparedStatement, connection);
        return affectedRows > 0;
    }

    //删除账户
    public boolean delete(int id) throws ClassNotFoundException, SQLException {
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();
        String deleteAccount_sql = "delete from account where id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(deleteAccount_sql);
        //为预编译参数赋值
        preparedStatement.setInt(1, id);
        //执行预编译语句，获取删除记录行数并赋值给affectedRowNum
        int affectedRows = preparedStatement.executeUpdate();
        //System.out.println("删除了" + affectedRows + "行记录");
        //关闭资源
        JdbcHelper.close(preparedStatement, connection);
        return affectedRows > 0;
    }

    //修改密码
    public boolean changPassWord(Account account) throws ClassNotFoundException, SQLException {
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();
        String updateAccount_sql = " update account set passwd=? where acct=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(updateAccount_sql);
        //为预编译参数赋值
        preparedStatement.setString(1, account.getPasswd());
        preparedStatement.setString(2, account.getAcct());
        //执行预编译语句，获取改变记录行数并赋值给affectedRowNum
        int affectedRows = preparedStatement.executeUpdate();
        //System.out.println("修改了" + affectedRows + "行记录");
        //关闭资源
        JdbcHelper.close(preparedStatement, connection);
        return affectedRows > 0;
    }

    //存款或取款
    public boolean saveOrDrawMoney(int acctId, Double newbalance) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        String updateBalance_sql = " update account set balance=? where id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(updateBalance_sql);
        preparedStatement.setDouble(1, newbalance);
        preparedStatement.setInt(2, acctId);
        //执行预编译语句，获取改变记录行数并赋值给affectedRowNum
        int affectedRows = preparedStatement.executeUpdate();
        // System.out.println("修改了" + affectedRows + "行记录");
        //关闭资源
        JdbcHelper.close(preparedStatement, connection);
        return affectedRows > 0;
    }

    //登录
    public Account login(String acct, String passwd) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        //写sql语句
        String updateAccount_sql = "select * from account where acct=? and passwd=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(updateAccount_sql);
        preparedStatement.setString(1, acct);
        preparedStatement.setString(2, passwd);
        ResultSet resultSet = preparedStatement.executeQuery();
        Account account = null;
        while (resultSet.next()) {
            account = new Account(resultSet.getInt("id"),
                    resultSet.getString("acct"),
                    resultSet.getString("passwd"),
                    resultSet.getDouble("balance"),
                    resultSet.getString("address"),
                    CustomerService.getInstance().find(resultSet.getInt("cust_id")),
                    RoleDao.getInstance().find(resultSet.getInt("role_id")));
        }
        return account;
    }

}
