package ru.friends.dao;

import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.mapping.Collection;
import org.springframework.stereotype.Repository;
import ru.friends.model.Friend;
import ru.friends.util.HibernateUtil;
import java.util.List;

@Repository("friendsDAO")
class FriendsDAOImpl implements FriendsDAO {

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
            if (tx != null)
                tx.rollback();
            log.error("FriendsDAOImpl: can't get friend by id", ex);
            return null;
        } finally {
            session.close();
        }
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
            if (tx!=null)
                tx.rollback();
            log.error("FriendsDAOImpl: can't save friend", ex);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Friend> getFriends(int user_id) {
        Session session = HibernateUtil.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("");
            Object friend = session.get(Friend.class, user_id);
            System.out.println("!!! " + (friend instanceof Friend) + " " + (friend instanceof Collection));
            tx.commit();
            return null;
        } catch (Throwable ex) {
            if (tx != null)
                tx.rollback();
            log.error("FriendsDAOImpl: can't get friend by id", ex);
            return null;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Friend> getDeletedFriends(int user_id) {
        return null;
    }

}
