package com.example.hibernate;

import com.example.hibernate.entity.ProductAuto;
import com.example.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Arrays;
import java.util.List;

public class HQLExamples {
    public static void main(String[] args) {
        SessionFactory sf = HibernateUtil.getSessionFactory();

        // Insert sample data (6 products)
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            List<ProductAuto> samples = Arrays.asList(
                    new ProductAuto("Pen", "Blue ink pen", 1.5, 100),
                    new ProductAuto("Notebook", "200 pages", 3.75, 50),
                    new ProductAuto("Pencil", "HB pencil", 0.5, 200),
                    new ProductAuto("Eraser", "White eraser", 0.75, 0),
                    new ProductAuto("Marker", "Permanent marker", 2.25, 60),
                    new ProductAuto("Highlighter", "Yellow highlighter", 1.25, 80)
            );
            for (ProductAuto p : samples) session.save(p);
            session.getTransaction().commit();
        }

        try (Session session = sf.openSession()) {
            System.out.println("\n-- Products sorted by price (asc) --");
            List<ProductAuto> asc = session.createQuery("from ProductAuto p order by p.price asc", ProductAuto.class).list();
            asc.forEach(System.out::println);

            System.out.println("\n-- Products sorted by price (desc) --");
            List<ProductAuto> desc = session.createQuery("from ProductAuto p order by p.price desc", ProductAuto.class).list();
            desc.forEach(System.out::println);

            System.out.println("\n-- Products sorted by quantity (highest first) --");
            List<ProductAuto> byQty = session.createQuery("from ProductAuto p order by p.quantity desc", ProductAuto.class).list();
            byQty.forEach(System.out::println);

            System.out.println("\n-- Pagination: first 3 products --");
            Query<ProductAuto> q = session.createQuery("from ProductAuto p order by p.id", ProductAuto.class);
            q.setFirstResult(0);
            q.setMaxResults(3);
            q.list().forEach(System.out::println);

            System.out.println("\n-- Pagination: next 3 products --");
            Query<ProductAuto> q2 = session.createQuery("from ProductAuto p order by p.id", ProductAuto.class);
            q2.setFirstResult(3);
            q2.setMaxResults(3);
            q2.list().forEach(System.out::println);

            System.out.println("\n-- Aggregate: count total products --");
            Long total = session.createQuery("select count(p) from ProductAuto p", Long.class).getSingleResult();
            System.out.println("Total products: " + total);

            System.out.println("\n-- Aggregate: count products where quantity > 0 --");
            Long positiveQty = session.createQuery("select count(p) from ProductAuto p where p.quantity > 0", Long.class).getSingleResult();
            System.out.println("Products with quantity>0: " + positiveQty);

            System.out.println("\n-- Aggregate: count products grouped by description --");
            List<Object[]> grouped = session.createQuery("select p.description, count(p) from ProductAuto p group by p.description", Object[].class).list();
            for (Object[] row : grouped) System.out.println(row[0] + " -> " + row[1]);

            System.out.println("\n-- Aggregate: min and max price --");
            Object[] minmax = session.createQuery("select min(p.price), max(p.price) from ProductAuto p").uniqueResult();
            System.out.println("Min price: " + minmax[0] + ", Max price: " + minmax[1]);

            System.out.println("\n-- GROUP BY description (names grouped by description) --");
            List<Object[]> groupByDesc = session.createQuery("select p.description, count(p) from ProductAuto p group by p.description", Object[].class).list();
            for (Object[] r : groupByDesc) System.out.println(r[0] + " -> " + r[1]);

            System.out.println("\n-- WHERE price range (1.0 to 2.0) --");
            List<ProductAuto> inRange = session.createQuery("from ProductAuto p where p.price between :low and :high", ProductAuto.class)
                    .setParameter("low", 1.0)
                    .setParameter("high", 2.0)
                    .list();
            inRange.forEach(System.out::println);

            System.out.println("\n-- LIKE queries --");
            System.out.println("Names starting with 'P' --");
            session.createQuery("from ProductAuto p where p.name like :start", ProductAuto.class)
                    .setParameter("start", "P%")
                    .list().forEach(System.out::println);

            System.out.println("\nNames ending with 'er' --");
            session.createQuery("from ProductAuto p where p.name like :end", ProductAuto.class)
                    .setParameter("end", "%er")
                    .list().forEach(System.out::println);

            System.out.println("\nNames containing 'note' (case-sensitive) --");
            session.createQuery("from ProductAuto p where p.name like :any", ProductAuto.class)
                    .setParameter("any", "%note%")
                    .list().forEach(System.out::println);

            System.out.println("\nNames with exact length 3 --");
            session.createQuery("from ProductAuto p where length(p.name) = :len", ProductAuto.class)
                    .setParameter("len", 3)
                    .list().forEach(System.out::println);
        }

        HibernateUtil.shutdown();
    }
}
