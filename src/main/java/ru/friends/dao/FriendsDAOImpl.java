package ru.friends.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.friends.model.Friends;
import ru.friends.util.HibernateUtil;

@Repository("friendsDAO")
public class FriendsDAOImpl implements FriendsDAO {

    private static Logger log = Logger.getLogger(FriendsDAOImpl.class);

    @Override
    public Friends getById(int id) {
        Session session = HibernateUtil.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Friends friend = (Friends) session.get(Friends.class, id);
            tx.commit();
            return friend;
        } catch (Throwable ex) {
            if (tx != null) tx.rollback();
            log.error("FriendsDAOImpl: can't get friend by id", ex);
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public void save(Friends friend) {
        Session session = HibernateUtil.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(friend);
            tx.commit();
        } catch (Throwable ex) {
            if (tx!=null) tx.rollback();
            log.error("FriendsDAOImpl: can't save friend", ex);
        } finally {
            session.close();
        }
    }

}
