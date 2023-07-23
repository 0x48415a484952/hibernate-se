package ir.sleepycat.cipher;


import ir.sleepycat.cipher.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class Main {
    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            // Perform CRUD operations using Hibernate
            // Example: session.save(yourEntity);

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
