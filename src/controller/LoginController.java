package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import domain.Account;
import domain.Customer;
import service.AccountService;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //定义string类型的username、password字段分别等于请求得到的参数username、password
        String account_json = JSONUtil.getJSON(request);
        Account account = JSON.parseObject(account_json, Account.class);
        String act = account.getAcct();
        String password = account.getPasswd();
        //创建json对象message，以便于向前端响应信息
        JSONObject message = new JSONObject();
        try {
            //调用accountService的login方法并传入相应参数并令Account类型的loggedAccount指向它
            Account loggedAccount = AccountService.getInstance().login(act, password);
            //如果不为空
            if (loggedAccount != null) {
                //如果当前请求对应服务器内存中的一个session对象，返回该对象
                //如果当前服务器内存中的没有session对象与当前请求对应，则建立一个session对象并返回该对象
                HttpSession session = request.getSession();
                //10分钟未操作则session失效
                session.setMaxInactiveInterval(10 * 60);
                session.setAttribute("currentAccount", loggedAccount);
                String user_json = JSONObject.toJSONString(loggedAccount);
                //响应并打印出数据信息
                response.getWriter().println(user_json);
                //此处应重定向到索引页（菜单页）
                return;
            } else {
                message.put("message", "账号或密码错误");
            }
        } catch (SQLException e) {
            //得到具体错误信息
            e.printStackTrace();
            message.put("message", "数据库操作异常");
        } catch (Exception e) {
            e.printStackTrace();
            message.put("message", "网络异常");
        }
        //响应message到前端
        response.getWriter().println(message);
    }
}
