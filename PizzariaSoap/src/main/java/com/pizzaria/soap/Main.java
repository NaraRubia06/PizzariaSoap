package com.pizzaria.soap;

import com.pizzaria.soap.services.PedidoService;
import jakarta.xml.ws.Endpoint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/pizzaria"; // meu host, porta e banco
        String user = "postgres"; // usuário
        String password = "nara"; // senha

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if (conn != null) {
                System.out.println("Conexão bem-sucedida com o banco de dados!");
            }
        } catch (SQLException e) {
            System.out.println("Erro de conexão: " + e.getMessage());
            e.printStackTrace();
        }

        // Conexão com o SOAP
        Endpoint.publish("http://localhost:8080/pedido", new PedidoService());
        System.out.println("SOAP publicado em http://localhost:8080/pedido?wsdl");
    }

}