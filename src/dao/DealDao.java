package dao;

import domain.Deal;
import helper.JdbcHelper;
import service.AccountService;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;


public final class DealDao {
    private static DealDao dealDao = new DealDao();

    private DealDao() {
    }

    public static DealDao getInstance() {
        return dealDao;
    }

    //根据账号查询交易记录
    public Collection<Deal> findAllByAcct(int acct) throws SQLException, ClassNotFoundException {
        Set<Deal> deals = new HashSet<Deal>();
        //建立连接
        Connection connection = JdbcHelper.getConn();
        String selectAcct_sql = "select * from deal where acct_id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(selectAcct_sql);
        //为预编译参数赋值
        preparedStatement.setInt(1, acct);
        //执行预编译语句
        ResultSet resultSet = preparedStatement.executeQuery();
        //若结果集仍然有下一条记录，则执行循环体
        while (resultSet.next()) {
            //创建Deal对象，根据遍历结果中的值
            Deal deal = new Deal(resultSet.getInt("id"),
                    resultSet.getString("seq"),
                    resultSet.getInt("dealmoney"),
                    resultSet.getString("dtype"),
                    resultSet.getString("rdate"),
                    AccountService.getInstance().find(resultSet.getInt("acct_id"))
            );
            deals.add(deal);
        }
        //关闭资源
        JdbcHelper.close(resultSet, preparedStatement, connection);
        //返回结果集对象
        return deals;
    }

    //查询全部交易记录
    public Collection<Deal> findAll() {
        Set<Deal> deals = new HashSet<Deal>();
        try {
            //获得数据库连接对象
            Connection connection = JdbcHelper.getConn();
            //在该连接上创建语句盒子对象
            Statement stmt = connection.createStatement();
            //执行SQL查询语句并获得结果集对象
            ResultSet resultSet = stmt.executeQuery("select * from deal");
            //若结果存在下一条，执行循环体
            while (resultSet.next()) {
                //根据数据库中的数据,创建Deal类型的对象
                Deal deal = new Deal(resultSet.getInt("id"),
                        resultSet.getString("seq"),
                        resultSet.getInt("dealmoney"),
                        resultSet.getString("dtype"),
                        resultSet.getString("rdate"),
                        AccountDao.getInstance().find(resultSet.getInt("acct_id"))
                );
                //添加到集合deals中
                deals.add(deal);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deals;
    }

    //增加交易记录
    public boolean add(Deal deal) throws SQLException, ClassNotFoundException {
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();
        String addDeal_sql = "insert into deal (acct_id,seq,dtype,dealmoney,rdate) values" + " (?,?,?,?,?)";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(addDeal_sql);
        //为预编译参数赋值
        preparedStatement.setInt(1, deal.getAccount().getId());
        preparedStatement.setString(2, deal.getSeq());
        preparedStatement.setString(3, deal.getDealType());
        preparedStatement.setDouble(4, deal.getDealMoney());
        preparedStatement.setString(5, deal.getRdate());
        //执行预编译语句，获取添加记录行数并赋值给affectedRowNum
        int affectedRowNum = preparedStatement.executeUpdate();
        System.out.println("添加了" + affectedRowNum + "行记录");
        //关闭资源
        JdbcHelper.close(preparedStatement, connection);
        return affectedRowNum > 0;
    }

}
