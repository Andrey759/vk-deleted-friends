package ru.friends.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.friends.model.Transaction;
import ru.friends.util.HibernateUtil;

@Repository("transactionsDAO")
public class TransactionsDAOImpl implements TransactionsDAO {

    private static Logger log = Logger.getLogger(TransactionsDAOImpl.class);

    @Override
    public Transaction getById(int id) {
        Session session = HibernateUtil.getSession();
        org.hibernate.Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Transaction transaction = (Transaction) session.get(Transaction.class, id);
            tx.commit();
            return transaction;
        } catch (Throwable ex) {
            if (tx != null) tx.rollback();
            log.error("TransactionsDAOImpl: can't get transaction by id", ex);
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public void save(Transaction transaction) {
        Session session = HibernateUtil.getSession();
        org.hibernate.Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(transaction);
            tx.commit();
        } catch (Throwable ex) {
            if (tx!=null) tx.rollback();
            log.error("TransactionsDAOImpl: can't save transaction", ex);
        } finally {
            session.close();
        }
    }
}
