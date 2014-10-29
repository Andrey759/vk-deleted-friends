package ru.friends.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.friends.model.User;
import ru.friends.util.HibernateUtil;

@Repository("usersDAO")
public class UsersDAOImpl implements UsersDAO {

    private static Logger log = Logger.getLogger(UsersDAOImpl.class);

    @Override
    public User getById(int id) {
        Session session = HibernateUtil.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            User user = (User) session.get(User.class, id);
            tx.commit();
            return user;
        } catch (Throwable ex) {
            if (tx != null) tx.rollback();
            log.error("UsersDAOImpl: can't get user by id", ex);
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public void save(User user) {
        Session session = HibernateUtil.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(user);
            tx.commit();
        } catch (Throwable ex) {
            if (tx!=null) tx.rollback();
            log.error("UsersDAOImpl: can't save user", ex);
        } finally {
            session.close();
        }
    }
}
