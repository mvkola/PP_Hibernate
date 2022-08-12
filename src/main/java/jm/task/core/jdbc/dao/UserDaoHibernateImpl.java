package jm.task.core.jdbc.dao;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// ТЗ: методы создания и удаления таблицы users в классе UserHibernateDaoImpl должны быть реализованы с помощью SQL
// Обработка всех исключений, связанных с работой с базой данных, должна находиться в dao (ТЗ)

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory mySessionFactory;
    private Transaction transaction = null;

    public UserDaoHibernateImpl() {
        this.mySessionFactory = Util.getSessionFactory();
    }

    @Override
    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users " +
                "(id INT NOT NULL AUTO_INCREMENT, " +
                " name VARCHAR(20) NOT NULL, " +
                " lastName VARCHAR(20) NOT NULL, " +
                " age INT NOT NULL, " +
                " PRIMARY KEY (id))";

        try (Session session = mySessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery(sql).addEntity(User.class).executeUpdate();
            transaction.commit();
            System.out.println("INFO: Table created successfully");
        } catch (Exception e) {
            Logger.getLogger("create").log(Level.WARNING, "Creation failed...");
            transaction.rollback();
        }
    }

    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";

        try (Session session = mySessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery(sql).addEntity(User.class).executeUpdate();
            transaction.commit();
            System.out.println("INFO: Table deleted successfully");
        } catch (Exception e) {
            Logger.getLogger("delete").log(Level.WARNING, "Removal failed...");
            transaction.rollback();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = mySessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();

            System.out.println("INFO: User \"Alex\" added to database successfully");
            System.out.println("INFO: User \"Anton\" added to database successfully");
            System.out.println("INFO: User \"Lev\" added to database successfully");
            System.out.println("INFO: User \"Vladimir\" added to database successfully");

        } catch (Exception e) {
            Logger.getLogger("add").log(Level.WARNING, "Adding failed...");
                transaction.rollback();
            }
        }

    @Override
    public void removeUserById(long id) {
        try (Session session = mySessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.get(User.class, id));
            transaction.commit();
            System.out.println("INFO: Remove successful, user id: " + id);
        } catch (Exception e) {
            Logger.getLogger("remove").log(Level.WARNING, "Remove failed..., user id: " + id);
            transaction.rollback();
        }
    }

    @Override
    public List<User> getAllUsers() {                                      // READ методы не оборачиваем в транзакции
        try (Session session = mySessionFactory.openSession()) {
            session.beginTransaction();
            Query<User> query = session.createQuery("from User");          // загружаем в память сохраняемые объекты POJO класса
            System.out.println("INFO: Getting Users successfully");
            return query.getResultList();
        } catch (Exception e) {
            Logger.getLogger("getAll").log(Level.WARNING, "Getting Users failed...");
        }
        return new ArrayList<>();
    }

    @Override
    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE users";

        try (Session session = mySessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery(sql).addEntity(User.class).executeUpdate();
            transaction.commit();
            System.out.println("INFO: Data Delete successful!");
        } catch (Exception e) {
            Logger.getLogger("delete").log(Level.WARNING, "Data Deletion failed...");
            transaction.rollback();
        }
    }
}
