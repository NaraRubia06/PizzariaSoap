package com.pizzaria.soap.services;

import com.pizzaria.soap.models.Cliente;
import com.pizzaria.soap.models.Pedido;
import com.pizzaria.soap.models.StatusPedido;
import com.pizzaria.soap.daos.ClienteDAO;
import com.pizzaria.soap.daos.PedidoDAO;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@WebService
public class PizzariaService {

    private ClienteDAO clienteDAO = new ClienteDAO();
    private PedidoDAO pedidoDAO = new PedidoDAO();

    @WebMethod
    public String criarCliente(String nome, String telefone, String cpf, String endereco, String dataNascimento) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dataNasc;

        try {
            dataNasc = sdf.parse(dataNascimento);
        } catch (ParseException e) {
            return "Erro: Formato de data inválido. Use dd/MM/yyyy";
        }

        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setTelefone(telefone);
        cliente.setCpf(cpf);
        cliente.setEndereco(endereco);
        cliente.setDataNascimento(dataNasc);

        clienteDAO.salvar(cliente);
        return "Cliente cadastrado com sucesso! ID: " + cliente.getId();
    }

    @WebMethod
    public String criarPedido(int clienteId) {
        Optional<Cliente> clienteOpt = clienteDAO.buscarPorId(clienteId);
        if (clienteOpt.isEmpty()) {
            return "Erro: Cliente não encontrado";
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(clienteOpt.get());
        pedido.setStatus(StatusPedido.RECEBIDO);
        pedidoDAO.salvar(pedido);

        return "Pedido criado com sucesso! ID: " + pedido.getId();
    }

    @WebMethod
    public String consultarStatusPedido(int pedidoId) {
        Optional<Pedido> pedidoOpt = pedidoDAO.buscarPorId(pedidoId);
        if (pedidoOpt.isEmpty()) {
            return "Erro: Pedido não encontrado";
        }
        return "Status do Pedido: " + pedidoOpt.get().getStatus();
    }
}