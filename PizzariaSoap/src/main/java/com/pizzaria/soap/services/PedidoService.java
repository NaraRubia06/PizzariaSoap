package com.pizzaria.soap.services;

import jakarta.jws.WebService;
import jakarta.jws.WebMethod;

@WebService
public class PedidoService {

    @WebMethod
    public String fazerPedido(String nomePizza) {
        return "Pedido recebido para pizza de: " + nomePizza;
    }
}
