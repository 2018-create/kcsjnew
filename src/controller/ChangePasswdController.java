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


@WebServlet("/change.ctl")
public class ChangePasswdController extends HttpServlet {

    /**
     * 方法-功能
     * put 修改
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String account_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Save对象
        Account balanceToUpdate = JSON.parseObject(account_json, Account.class);
        //创建JSON对象
        JSONObject message = new JSONObject();
        try {
            AccountService.getInstance().changePassWord(balanceToUpdate);
            //加入数据信息
            message.put("message", "你已成功修改密码，请妥善保存。");
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("message", "数据库操作异常");
        } catch (Exception e) {
            e.printStackTrace();
            message.put("message", "网络异常");
        }
        //响应
        response.getWriter().println(message);
    }
}
