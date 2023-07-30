package ir.sleepycat.cipher.util;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


@Slf4j
public class HibernateUtil {
    private HibernateUtil() {
        //
    }

//    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);

    private static final SessionFactory sf;

    static {
        try {
            sf = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (Exception ex) {
            log.info("Initial SessionFactory creation failed, error is:{}", ex.toString());
//            logger.error("Initial SessionFactory creation failed, error is:{}", ex.toString());
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sf;
    }

}
