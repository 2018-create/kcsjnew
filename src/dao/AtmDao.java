package dao;

import domain.Atm;
import helper.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;


public final class AtmDao {
    private static AtmDao atmDao =
            new AtmDao();

    private AtmDao() {
    }

    public static AtmDao getInstance() {
        return atmDao;
    }

    //查询全部ATM信息
    public Collection<Atm> findAll() {
        Set<Atm> atms = new HashSet<Atm>();
        try {
            //获得数据库连接对象
            Connection connection = JdbcHelper.getConn();
            //在该连接上创建语句盒子对象
            Statement stmt = connection.createStatement();
            //执行SQL查询语句并获得结果集对象
            ResultSet resultSet = stmt.executeQuery("select * from atm");
            //若结果存在下一条，执行循环体
            while (resultSet.next()) {
                //根据数据库中的数据,创建Atm类型的对象
                Atm atm = new Atm(resultSet.getInt("id"),
                        resultSet.getString("no"),
                        resultSet.getString("address"));
                //添加到集合atms中
                atms.add(atm);
            }
            //关闭连接
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return atms;
    }

    //查询单个ATM信息
    public Atm find(Integer id) throws SQLException {
        //声明一个Atm类型的变量
        Atm atm = null;
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();
        String deleteAtm_sql = "select * from atm where id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(deleteAtm_sql);
        //为预编译参数赋值
        preparedStatement.setInt(1, id);
        //执行预编译语句
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            atm = new Atm(resultSet.getInt("id"),
                    resultSet.getString("no"),
                    resultSet.getString("address"));
        }
        //关闭资源
        JdbcHelper.close(resultSet, preparedStatement, connection);
        return atm;
    }

    //添加新ATM
    public boolean add(Atm atm) throws SQLException, ClassNotFoundException {
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();
        String addAtm_sql = "insert into atm (no,address) values" + " (?,?)";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(addAtm_sql);
        //为预编译参数赋值
        preparedStatement.setString(1, atm.getNo());
        preparedStatement.setString(2, atm.getAddress());
        //执行预编译语句，获取添加记录行数并赋值给affectedRowNum
        int affectedRowNum = preparedStatement.executeUpdate();
        System.out.println("添加了" + affectedRowNum + "行记录");
        //关闭资源
        JdbcHelper.close(preparedStatement, connection);
        return affectedRowNum > 0;
    }

    //删除ATM
    public boolean delete(int id) throws ClassNotFoundException, SQLException {
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();
        String deleteAtm_sql = "delete from atm where id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(deleteAtm_sql);
        //为预编译参数赋值
        preparedStatement.setInt(1, id);
        //执行预编译语句，获取删除记录行数并赋值给affectedRowNum
        int affectedRows = preparedStatement.executeUpdate();
        System.out.println("删除了" + affectedRows + "行记录");
        //关闭资源
        JdbcHelper.close(preparedStatement, connection);
        return affectedRows > 0;
    }

    //更新ATM信息
    public boolean update(Atm atm) throws ClassNotFoundException, SQLException {
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();
        String updateAtm_sql = " update atm set no=?,address=? where id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(updateAtm_sql);
        //为预编译参数赋值
        preparedStatement.setString(1, atm.getNo());
        preparedStatement.setString(2, atm.getAddress());
        preparedStatement.setInt(3, atm.getId());
        //执行预编译语句，获取改变记录行数并赋值给affectedRowNum
        int affectedRows = preparedStatement.executeUpdate();
        System.out.println("修改了" + affectedRows + "行记录");
        //关闭资源
        JdbcHelper.close(preparedStatement, connection);
        return affectedRows > 0;
    }
}
