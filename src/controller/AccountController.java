package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import domain.Account;
import service.AccountService;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;


@WebServlet("/account.ctl")
public class AccountController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //根据request对象，获得代表参数的JSON字串
        String accountToAdd_json = JSONUtil.getJSON(request);
        //将JSON字串解析为account对象
        Account accountToAdd = JSON.parseObject(accountToAdd_json, Account.class);
        //创建JSON对象
        JSONObject message = new JSONObject();
        try {
            //增加Account对象
            boolean added = AccountService.getInstance().add(accountToAdd);
            if (added) {
                message.put("message", "添加成功");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("message", "未能添加");
        } catch (Exception e) {
            e.printStackTrace();
            message.put("message", "网络异常");
        }
        //响应
        response.getWriter().println(message);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //创建JSON对象
        JSONObject message = new JSONObject();
        try {
            AccountService.getInstance().delete(id);
            message.put("message", "删除成功");
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("message", "未能删除");
        } catch (Exception e) {
            e.printStackTrace();
            message.put("message", "网络异常");
        }
        //响应
        response.getWriter().println(message);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accountToUpdate_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Account对象
        Account accountToUpdate = JSON.parseObject(accountToUpdate_json, Account.class);
        //创建JSON对象
        JSONObject resp = new JSONObject();
        try {
            AccountService.getInstance().update(accountToUpdate);
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
            //如果id = null, 表示响应所有对象，否则响应id指定的对象
            if (id_str == null) {
                responseAccounts(response);
            } else {
                int id = Integer.parseInt(id_str);
                responseAccount(id, response);
            }
        } catch (SQLException e) {
            message.put("message", "查询失败");
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
    private void responseAccount(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ClassNotFoundException {
        //根据id查找
        Account account = AccountService.getInstance().find(id);
        String account_json = JSON.toJSONString(account);
        //控制台打印结果
        //System.out.println(account_json);
        //浏览器展示结果
        response.getWriter().println(account_json);
    }

    //响应所有对象
    private void responseAccounts(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有
        Collection<Account> accounts = AccountService.getInstance().findAll();
        String accounts_json = JSON.toJSONString(accounts);
        //控制台打印结果
        //System.out.println(accounts_json);
        //浏览器展示结果
        response.getWriter().println(accounts_json);
    }
}
