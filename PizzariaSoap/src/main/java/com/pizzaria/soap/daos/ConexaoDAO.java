package com.pizzaria.soap.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class ConexaoDAO {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("PizzariaSoap");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}