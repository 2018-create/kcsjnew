package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import domain.Atm;
import service.AtmService;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;


@WebServlet("/atm.ctl")
public class AtmController extends HttpServlet {

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
        String atm_json = JSONUtil.getJSON(request);
        //将JSON字串解析为atm对象
        Atm atmToAdd = JSON.parseObject(atm_json, Atm.class);
        //System.out.println(atmToAdd);
        //创建JSON对象
        JSONObject message = new JSONObject();
        try {
            //增加加Atm对象
            AtmService.getInstance().add(atmToAdd);
            message.put("message", "添加成功");
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

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //创建JSON对象
        JSONObject message = new JSONObject();
        try {
            //到数据库表中删除对应的学院
            AtmService.getInstance().delete(id);
            //加入数据信息
            message.put("message", "删除成功");
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

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String atm_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Atm对象
        Atm atmToAdd = JSON.parseObject(atm_json, Atm.class);
        //创建JSON对象
        JSONObject message = new JSONObject();
        try {

            AtmService.getInstance().update(atmToAdd);
            //加入数据信息
            message.put("message", "修改成功");
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
                responseAtms(response);
            } else {
                int id = Integer.parseInt(id_str);
                responseAtm(id, response);
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            //响应message到前端
            response.getWriter().println(message);
        } catch (Exception e) {
            message.put("message", "网络异常");
            //响应message到前端
            response.getWriter().println(message);
        }
    }

    //响应一个对象
    private void responseAtm(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找
        Atm atm = AtmService.getInstance().find(id);
        String atm_json = JSON.toJSONString(atm);
        //控制台打印结果
        System.out.println(atm_json);
        //浏览器展示结果
        response.getWriter().println(atm_json);
    }

    //响应所有对象
    private void responseAtms(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有
        Collection<Atm> atms = AtmService.getInstance().findAll();
        String atms_json = JSON.toJSONString(atms);
        //控制台打印结果
        System.out.println(atms_json);
        //浏览器展示结果
        response.getWriter().println(atms_json);
    }
}
