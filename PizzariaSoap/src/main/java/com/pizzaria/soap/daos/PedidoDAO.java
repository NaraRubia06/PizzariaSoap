package com.pizzaria.soap.daos;

import com.pizzaria.soap.models.Pedido;
import com.pizzaria.soap.utils.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PedidoDAO {

    public Pedido salvar(Pedido pedido) {
        EntityManager em = ConexaoDAO.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pedido); // Salva o pedido no banco
            em.flush(); // Garante que o ID será gerado imediatamente
            em.getTransaction().commit();
            return pedido; // Retorna o pedido com ID gerado
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Optional<Pedido> buscarPorId(int id) {
        EntityManager em = ConexaoDAO.getEntityManager();
        try {
            Pedido pedido = em.find(Pedido.class, id);
            return Optional.ofNullable(pedido);
        } finally {
            em.close();
        }
    }

    public List<Pedido> buscarPorCpf(String cpf) {
        EntityManager em = JpaUtil.getEntityManager();
        List<Pedido> pedidos = new ArrayList<>();
        try {
            pedidos = em.createQuery("SELECT p FROM Pedido p WHERE p.cliente.cpf = :cpf", Pedido.class)
                    .setParameter("cpf", cpf)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return pedidos;
    }

    public List<Pedido> listarTodos() {
        EntityManager em = ConexaoDAO.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Pedido p", Pedido.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void atualizarStatus(int pedidoId) {
        EntityManager em = ConexaoDAO.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            Pedido pedido = em.find(Pedido.class, pedidoId);
            if (pedido == null) {
                throw new EntityNotFoundException("Pedido não encontrado com ID: " + pedidoId);
            }

            // Esse aqui vai avançar para o próximo status
            pedido.avancarStatus();

            em.merge(pedido);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            System.err.println("Erro ao atualizar status: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}