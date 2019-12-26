package service;


import dao.AccountDao;
import domain.Account;

import java.sql.SQLException;
import java.util.Collection;


public final class AccountService {
    private static AccountDao accountDao = AccountDao.getInstance();
    private static AccountService accountService = new AccountService();

    public static AccountService getInstance() {
        return accountService;
    }

    public Collection<Account> findAll() {
        return accountDao.findAll();
    }

    public Account find(Integer id) throws SQLException, ClassNotFoundException {
        return accountDao.find(id);
    }
    public Account findByAcct(int acct) throws SQLException {
        return accountDao.findByAcct(acct);
    }
    public boolean changePassWord(Account account) throws SQLException, ClassNotFoundException {
        return accountDao.changPassWord(account);
    }

    public boolean saveOrDrawMoney(int acctId, Double balanceAfterSave) throws SQLException, ClassNotFoundException {
        return accountDao.saveOrDrawMoney(acctId, balanceAfterSave);
    }
    public boolean transfer(int outAcct, int inAcct, int amount){
        return accountDao.transfer(outAcct,inAcct,amount);
    }

    public boolean add(Account account) throws SQLException, ClassNotFoundException {
        return accountDao.add(account);
    }

    public boolean update(Account account) throws SQLException, ClassNotFoundException {
        return accountDao.update(account);
    }

    public boolean delete(Integer id) throws SQLException, ClassNotFoundException {
        return accountDao.delete(id);
    }

    public Account login(String username, String password) throws SQLException {
        return accountDao.login(username, password);
    }
    public double findBalanceByAcct(Integer acct) throws SQLException {
        return accountDao.findBalanceByAcct(acct);
    }
}

