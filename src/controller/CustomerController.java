package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import domain.Customer;
import service.CustomerService;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;


@WebServlet("/customer.ctl")
public class CustomerController extends HttpServlet {

    /**
     * 方法-功能
     * put 修改
     * post 添加
     * delete 删除
     * get 查找
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //根据request对象，获得代表参数的JSON字串
        String customer_json = JSONUtil.getJSON(request);
        //将JSON字串解析为customer 对象
        Customer customerToAdd = JSON.parseObject(customer_json, Customer.class);
        System.out.println(customerToAdd);
        //创建JSON对象
        JSONObject resp = new JSONObject();
        try {
            //增加加Customer对象
            CustomerService.getInstance().add(customerToAdd);
            resp.put("message", "添加成功");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.put("message", "数据库操作异常");
        } catch (Exception e) {
            e.printStackTrace();
            resp.put("message", "网络异常");
        }
        //响应
        response.getWriter().println(resp);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //创建JSON对象
        JSONObject resp = new JSONObject();
        try {
            boolean deleted = CustomerService.getInstance().delete(id);
            if(deleted){
                resp.put("message", "删除成功");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.put("message", "未能删除,请检查此客户是否还绑定着账户。");
        } catch (Exception e) {
            e.printStackTrace();
            resp.put("message", "网络异常");
        }
        //响应
        response.getWriter().println(resp);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String customer_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Customer对象
        Customer customerToUpdate = JSON.parseObject(customer_json, Customer.class);
        //创建JSON对象
        JSONObject resp = new JSONObject();
        try {
            CustomerService.getInstance().update(customerToUpdate);
            //加入数据信息
            resp.put("message", "修改成功");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.put("message", "未能修改");
        } catch (Exception e) {
            e.printStackTrace();
            resp.put("message", "网络异常");
        }
        //响应
        response.getWriter().println(resp);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            if (id_str == null) {
                responseCustomers(response);
            } else {
                int id = Integer.parseInt(id_str);
                responseCustomer(id, response);
            }
        } catch (SQLException e) {
            message.put("message", "查询失败");
            //响应message到前端
            response.getWriter().println(message);
        } catch (Exception e) {
            message.put("message", "网络异常");
            //响应message到前端
            response.getWriter().println(message);
        }
    }

    //响应一个对象
    private void responseCustomer(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找
        Customer customer = CustomerService.getInstance().find(id);
        String customer_json = JSON.toJSONString(customer);
        //控制台打印结果
        //System.out.println(customer_json);
        //浏览器展示结果
        response.getWriter().println(customer_json);
    }

    //响应所有对象
    private void responseCustomers(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有
        Collection<Customer> customers = CustomerService.getInstance().findAll();
        String customers_json = JSON.toJSONString(customers);
        //控制台打印结果
        //System.out.println(customers_json);
        //浏览器展示结果
        response.getWriter().println(customers_json);
    }
}
