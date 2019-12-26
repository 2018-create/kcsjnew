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
import java.util.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;


@WebServlet("/save.ctl")
public class SaveController extends HttpServlet {


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
            double balanceBeforeSave = AccountService.getInstance().find(acctId).getBalance();
            double balanceAfterSave = balanceBeforeSave + amount;
            if (amount >= 0) {
                if (amount <= 20000) {
                    AccountService.getInstance().saveOrDrawMoney(acctId, balanceAfterSave);
                    //设置时间格式
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dealTime = df.format(new Date());
                    String seq = "SDJZ111 " + dealTime;
                    String dealType = "存款";
                    Deal deal = new Deal(seq, amount, dealType, dealTime, AccountService.getInstance().find(acctId));
                    DealService.getInstance().add(deal);
                    //加入数据信息
                    message.put("message", "你已成功存入" + amount + "元");
                } else {
                    message.put("message", "存款金额不能超过20000");
                }
            } else {
                message.put("message", "存款金额不能为负");
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
