package service;

import dao.AtmDao;
import domain.Atm;

import java.sql.SQLException;
import java.util.Collection;

public final class AtmService {
    private static AtmDao atmDao
            = AtmDao.getInstance();
    private static AtmService atmService
            = new AtmService();

    private AtmService() {
    }

    public static AtmService getInstance() {
        return atmService;
    }

    //定义相关所需的方法(增删改查)
    public Collection<Atm> findAll() {
        return atmDao.findAll();
    }

    public Atm find(Integer id) throws SQLException {
        return atmDao.find(id);
    }

    public boolean update(Atm atm) throws SQLException, ClassNotFoundException {
        return atmDao.update(atm);
    }

    public boolean add(Atm atm) throws SQLException, ClassNotFoundException {
        return atmDao.add(atm);
    }

    public boolean delete(Integer id) throws SQLException, ClassNotFoundException {
        return atmDao.delete(id);
    }
}

