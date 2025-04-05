package com.pizzaria.soap.daos;

import com.pizzaria.soap.models.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import java.util.Optional;

public class ClienteDAO {

    public void salvar(Cliente cliente) {
        EntityManager em = ConexaoDAO.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.persist(cliente);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public Optional<Cliente> buscarPorId(int id) {
        EntityManager em = ConexaoDAO.getEntityManager();
        try {
            Cliente cliente = em.find(Cliente.class, id);
            return Optional.ofNullable(cliente);
        } finally {
            em.close();
        }
    }

    public Cliente buscarPorCpf(String cpf) {
        EntityManager em = ConexaoDAO.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Cliente c WHERE c.cpf = :cpf", Cliente.class)
                    .setParameter("cpf", cpf)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}