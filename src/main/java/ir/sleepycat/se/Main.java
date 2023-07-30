package ir.sleepycat.se;


import ir.sleepycat.se.cli.CommandLine;
import ir.sleepycat.se.gui.GuiApplication;

public class Main {
    public static void main(String[] args) {
//        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
//        try (Session session = sessionFactory.openSession()) {
//            Transaction tx = session.beginTransaction();
//
//            // Perform CRUD operations using Hibernate
//            // Example: session.save(yourEntity);
//
//            tx.commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        if (args.length > 0 && args[0].equals("--gui")) {
            // Launch JavaFX GUI
            GuiApplication.runGui(args);
        } else {
            CommandLine.runCli(args);
            // Start command-line interface
        }
    }
}