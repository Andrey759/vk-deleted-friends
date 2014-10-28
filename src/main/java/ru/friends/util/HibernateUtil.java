package ru.friends.util;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateUtil {

    private static Logger log = Logger.getLogger(HibernateUtil.class);

    private static final SessionFactory sessionFactory;
    static {
        try {
            sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Exception ex) {
            log.fatal("HibernateUtil: sessionFactory can't be initialized", ex);

            //Зачем?
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return sessionFactory.openSession();
    }
}
