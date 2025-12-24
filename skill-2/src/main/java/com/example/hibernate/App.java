package com.example.hibernate;

import com.example.hibernate.dao.ProductDAO;
import com.example.hibernate.entity.ProductAuto;
import com.example.hibernate.entity.ProductIdentity;
import com.example.hibernate.entity.ProductSequence;
import com.example.hibernate.util.HibernateUtil;
import org.hibernate.SessionFactory;

import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        ProductDAO<ProductAuto> daoAuto = new ProductDAO<>(sf);
        ProductDAO<ProductIdentity> daoIdentity = new ProductDAO<>(sf);
        ProductDAO<ProductSequence> daoSequence = new ProductDAO<>(sf);

        System.out.println("--- AUTO strategy demo ---");
        List<ProductAuto> autos = Arrays.asList(
                new ProductAuto("Pen", "Blue ink pen", 1.5, 100),
                new ProductAuto("Notebook", "200 pages", 3.75, 50)
        );
        daoAuto.saveAll(autos);
        ProductAuto fetchedAuto = daoAuto.findById(ProductAuto.class, autos.get(0).getId());
        System.out.println("Fetched: " + fetchedAuto);
        // update
        fetchedAuto.setPrice(2.0);
        fetchedAuto.setQuantity(80);
        daoAuto.update(fetchedAuto);
        System.out.println("After update: " + daoAuto.findById(ProductAuto.class, fetchedAuto.getId()));
        // delete
        daoAuto.deleteById(ProductAuto.class, autos.get(1).getId());
        System.out.println("After delete (second): " + daoAuto.findById(ProductAuto.class, autos.get(1).getId()));

        System.out.println("--- IDENTITY strategy demo ---");
        List<ProductIdentity> ids = Arrays.asList(
                new ProductIdentity("Pencil", "HB pencil", 0.5, 200),
                new ProductIdentity("Eraser", "White eraser", 0.75, 150)
        );
        daoIdentity.saveAll(ids);
        ProductIdentity fetchedId = daoIdentity.findById(ProductIdentity.class, ids.get(0).getId());
        System.out.println("Fetched: " + fetchedId);
        fetchedId.setPrice(0.6);
        daoIdentity.update(fetchedId);
        System.out.println("After update: " + daoIdentity.findById(ProductIdentity.class, fetchedId.getId()));
        daoIdentity.deleteById(ProductIdentity.class, ids.get(1).getId());
        System.out.println("After delete (second): " + daoIdentity.findById(ProductIdentity.class, ids.get(1).getId()));

        System.out.println("--- SEQUENCE strategy demo ---");
        List<ProductSequence> seqs = Arrays.asList(
                new ProductSequence("Marker", "Permanent marker", 2.25, 60),
                new ProductSequence("Highlighter", "Yellow highlighter", 1.25, 80)
        );
        daoSequence.saveAll(seqs);
        ProductSequence fetchedSeq = daoSequence.findById(ProductSequence.class, seqs.get(0).getId());
        System.out.println("Fetched: " + fetchedSeq);
        fetchedSeq.setQuantity(55);
        daoSequence.update(fetchedSeq);
        System.out.println("After update: " + daoSequence.findById(ProductSequence.class, fetchedSeq.getId()));
        daoSequence.deleteById(ProductSequence.class, seqs.get(1).getId());
        System.out.println("After delete (second): " + daoSequence.findById(ProductSequence.class, seqs.get(1).getId()));

        HibernateUtil.shutdown();
    }
}
