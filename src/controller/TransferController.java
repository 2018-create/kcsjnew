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
import java.text.SimpleDateFormat;
import java.util.Date;


@WebServlet("/transfer.ctl")
public class TransferController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String outAcct_str = request.getParameter("outAcct");
        String inAcct_str = request.getParameter("inAcct");
        String amount_str = request.getParameter("amount");
        JSONObject message = new JSONObject();
        try {
            if (outAcct_str == null && inAcct_str == null && amount_str == null) {
                message.put("message", "请正确输入账号");
            } else if (inAcct_str.equals(outAcct_str)) {
                message.put("message", "你不能给你自己转钱！");
            } else {
                int outAcct = Integer.parseInt(outAcct_str);
                int inAcct = Integer.parseInt(inAcct_str);
                int amount = Integer.parseInt(amount_str);
                boolean transfered = AccountService.getInstance().transfer(outAcct, inAcct, amount);
                if (transfered) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dealTime = df.format(new Date());
                    String seq = "SDJZ111 " + dealTime;
                    String dealType = "转账";
                    Deal dealOfInAcct = new Deal(seq, amount, dealType, dealTime, AccountService.getInstance().findByAcct(inAcct));
                    Deal dealOfOutAcct = new Deal(seq, -amount, dealType, dealTime, AccountService.getInstance().findByAcct(outAcct));
                    DealService.getInstance().add(dealOfInAcct);
                    DealService.getInstance().add(dealOfOutAcct);
                    message.put("message", "转账成功");
                } else {
                    message.put("message", "转账失败");
                }
            }
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        response.getWriter().println(message);
    }
}
