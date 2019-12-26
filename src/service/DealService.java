package service;

import dao.DealDao;
import domain.Account;
import domain.Deal;

import java.sql.SQLException;
import java.util.Collection;

public final class DealService {
    private static DealDao dealDao = DealDao.getInstance();
    private static DealService dealService = new DealService();

    private DealService() {
    }

    public static DealService getInstance() {
        return dealService;
    }

    //定义方法根据外键id查询
    public Collection<Deal> findAllByAcct(int acct) throws SQLException, ClassNotFoundException {
        return dealDao.findAllByAcct(acct);
    }

    //定义相关所需的方法(增删改查)
    public Collection<Deal> findAll() {
        return dealDao.findAll();
    }


    public boolean add(Deal deal) throws SQLException, ClassNotFoundException {
        return dealDao.add(deal);
    }


}

