package controller;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //如果当前请求对应服务器内存中的一个session对象，返回该对象
        //如果当前服务器内存中的没有session对象与当前请求对应，则建立一个session对象并返回该对象
        JSONObject message = new JSONObject();
        HttpSession session = request.getSession(false);
        if (session != null) {
            //退出登录
            session.invalidate();
            message.put("message", "已退出");
            response.getWriter().println(message);
            return;
        }

    }
}
