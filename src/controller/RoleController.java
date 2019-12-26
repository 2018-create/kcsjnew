package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dao.RoleDao;
import domain.Role;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/role.ctl")
public class RoleController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有对象，否则响应id指定的对象
            if (id_str == null) {
                responseRoles(response);
            } else {
                int id = Integer.parseInt(id_str);
                responseRole(id, response);
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            //响应message到前端
            response.getWriter().println(message);
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
            //响应message到前端
            response.getWriter().println(message);
        }
    }

    //响应一个对象
    private void responseRole(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ClassNotFoundException {
        //根据id查找
        Role role = RoleDao.getInstance().find(id);
        String role_json = JSON.toJSONString(role);
        //控制台打印结果
        //System.out.println(role_json);
        //浏览器展示结果
        response.getWriter().println(role_json);
    }

    //响应所有对象
    private void responseRoles(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有
        Collection<Role> roles = RoleDao.getInstance().findAll();
        String roles_json = JSON.toJSONString(roles);
        //控制台打印结果
        //System.out.println(roles_json);
        //浏览器展示结果
        response.getWriter().println(roles_json);
    }
}
