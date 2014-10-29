package ru.friends.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.friends.model.Friend;
import ru.friends.util.HibernateUtil;

@Repository("friendsDAO")
public class FriendsDAOImpl implements FriendsDAO {

    private static Logger log = Logger.getLogger(FriendsDAOImpl.class);

    @Override
    public Friend getById(int id) {
        Session session = HibernateUtil.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Friend friend = (Friend) session.get(Friend.class, id);
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
    public void save(Friend friend) {
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
