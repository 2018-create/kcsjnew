package dao;

import domain.Role;

import helper.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RoleDao {
    private static RoleDao roleDao = new RoleDao();

    private RoleDao() {
    }

    public static RoleDao getInstance() {
        return roleDao;
    }

    //按id查询单个角色
    public Role find(Integer id) throws SQLException {
        //声明一个Role类型的变量
        Role role = null;
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement("select * from role where id=?");
        //为预编译参数赋值
        preparedStatement.setInt(1, id);
        //执行预编译语句
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            role = new Role(resultSet.getInt("id"),
                    resultSet.getString("no"),
                    resultSet.getString("description"));
        }
        //关闭资源
        JdbcHelper.close(resultSet, preparedStatement, connection);
        return role;
    }

    //查询全部角色
    public Collection<Role> findAll() {
        Set<Role> roles = new HashSet<Role>();
        try {
            //获得数据库连接对象
            Connection connection = JdbcHelper.getConn();
            //在该连接上创建语句盒子对象
            Statement stmt = connection.createStatement();
            //执行SQL查询语句并获得结果集对象
            ResultSet resultSet = stmt.executeQuery("select * from role");
            //若结果存在下一条，执行循环体
            while (resultSet.next()) {
                //创建Role对象，根据遍历结果中的id,description,no,remarks，school值
                Role role = new Role(resultSet.getInt("id"),
                        resultSet.getString("no"),
                        resultSet.getString("description"));
                //添加到集合roles中
                roles.add(role);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }
}
