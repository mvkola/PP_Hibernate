package jm.task.core.jdbc.service;
import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;                       // JDBC solution
import jm.task.core.jdbc.model.User;

import javax.transaction.SystemException;
import java.util.List;

// слой Service отвечает за бизнес-логику
// Service на этот раз использует реализацию DAO через Hibernate (ТЗ)
// Переключаем переменную userDao

public class UserServiceImpl implements UserService {

    //   private final UserDao userDao = new UserDaoJDBCImpl();     // JDBC solution
    private final UserDao userDao = new UserDaoHibernateImpl();  // Hibernate solution

    public void createUsersTable() throws SystemException {
        userDao.createUsersTable();
    }

    public void dropUsersTable() {
        userDao.dropUsersTable();
    }

    public void saveUser(String name, String lastName, byte age) {
        userDao.saveUser(name, lastName, age);
    }

    public void removeUserById(long id) {
        userDao.removeUserById(id);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public void cleanUsersTable() {
        userDao.cleanUsersTable();
    }
}
