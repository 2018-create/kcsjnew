package controller;

import com.alibaba.fastjson.JSONObject;
import domain.Deal;
import service.AccountService;
import service.DealService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;


@WebServlet("/draw.ctl")
public class DrawController extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acctId_str = request.getParameter("acctId");
        String amount_str = request.getParameter("amount");
        int acctId = Integer.parseInt(acctId_str);
        int amount = Integer.parseInt(amount_str);
        //创建JSON对象
        JSONObject message = new JSONObject();
        try {
            double balanceBeforeDraw = AccountService.getInstance().find(acctId).getBalance();
            double balanceAfterDraw = balanceBeforeDraw - amount;
            if (amount >= 0) {
                if (amount <= 20000) {
                    if (balanceAfterDraw < 0) {
                        message.put("message", "余额不足");
                    } else {
                        AccountService.getInstance().saveOrDrawMoney(acctId, balanceAfterDraw);
                        //设置时间格式
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String dealTime = df.format(new Date());
                        String seq = "SDJZ111 " + dealTime;
                        String dealType = "取款";
                        Deal deal = new Deal(seq, amount, dealType, dealTime, AccountService.getInstance().find(acctId));
                        DealService.getInstance().add(deal);
                        //加入数据信息
                        message.put("message", "你已成功取出" + amount + "元");
                    }
                } else {
                    message.put("message", "取款金额不能超过20000");
                }
            } else {
                message.put("message", "取款金额不能为负");
            }
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
