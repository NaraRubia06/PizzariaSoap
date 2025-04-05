package com.pizzaria.soap.services;

import com.pizzaria.soap.daos.PedidoDAO;
import com.pizzaria.soap.models.Pedido;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AtualizadorPedidos {
    private static final PedidoDAO pedidoDAO = new PedidoDAO();

    public static void iniciarAtualizacaoAutomatica() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                List<Pedido> pedidos = pedidoDAO.listarTodos();
                for (Pedido pedido : pedidos) {
                    pedidoDAO.atualizarStatus(pedido.getId().intValue()); // Conversão de Long para int
                }
                System.out.println("Status dos pedidos atualizados.");
            }
        }, 0, 60 * 1000); // A cada 60 segundos
    }
}
