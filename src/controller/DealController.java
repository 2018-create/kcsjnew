package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import domain.Deal;
import domain.Deal;
import service.DealService;
import service.DealService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/deal.ctl")
public class DealController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //读取参数id
        String acct_str = request.getParameter("acct");
        //System.out.println(acct_str);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            if (acct_str != null) {
                int acct = Integer.parseInt(acct_str);
                responseDealsByAcct(acct, response);
            } else {
                responseDeals(response);
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

    //响应所有对象
    private void responseDealsByAcct(int acct, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ClassNotFoundException {
        //获得所有
        Collection<Deal> deals = DealService.getInstance().findAllByAcct(acct);
        String deals_json = JSON.toJSONString(deals);
        //控制台打印结果
        //System.out.println(deals_json);
        //浏览器展示结果
        response.getWriter().println(deals_json);
    }

    private void responseDeals(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有
        Collection<Deal> deals = DealService.getInstance().findAll();
        String deals_json = JSON.toJSONString(deals);
        //控制台打印结果
        //System.out.println(deals_json);
        //浏览器展示结果
        response.getWriter().println(deals_json);
    }
}
