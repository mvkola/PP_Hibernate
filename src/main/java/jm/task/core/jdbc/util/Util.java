package jm.task.core.jdbc.util;
import jm.task.core.jdbc.model.User;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

//  ТЗ: в классе Util должна быть добавлена конфигурация для Hibernate (рядом с JDBC), без использования xml

public class Util {
        private final static String driver = "com.mysql.cj.jdbc.Driver";
        static final String url = "jdbc:mysql://localhost:3306/hibernate_db";
        static final String username = "mvkola";
        static final String password = "lXJD@ge1";
        static Connection connection;

        public static Connection getConnection() {                               // JDBC solution
                try {
                        Class.forName(driver);
                        connection = DriverManager.getConnection(url, username, password);
                        System.out.println("INFO: Connected successfully");
                } catch (SQLException | ClassNotFoundException e) {
                        Logger.getLogger("connect").log(Level.WARNING, "Connection failed...");
                }
                return connection;
        }

        static SessionFactory mySessionFactory;                                  // Hibernate solution
        public static SessionFactory getSessionFactory() {
                Environment envrmnt = null;
                Configuration configuration = new Configuration();
                Properties settings = new Properties();
                try {
                        settings.put(envrmnt.DRIVER, driver);
                        settings.put(envrmnt.URL, url);
                        settings.put(envrmnt.USER, username);
                        settings.put(envrmnt.PASS, password);
                        settings.put(envrmnt.AUTOCOMMIT, "false");
                        settings.put(envrmnt.DIALECT, "org.hibernate.dialect.MySQLDialect");
                        settings.put(envrmnt.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                        configuration.setProperties(settings);
                        configuration.addAnnotatedClass(User.class);             // add JPA Entity mapping class

                        ServiceRegistry serviceReg = new StandardServiceRegistryBuilder()
                                .applySettings(configuration.getProperties()).build();
                        mySessionFactory = configuration
                                .buildSessionFactory(serviceReg);
                } catch (Exception e) {
                        Logger.getLogger("connect").log(Level.WARNING, "Connection failed...");
                }
                return mySessionFactory;
        }
}

