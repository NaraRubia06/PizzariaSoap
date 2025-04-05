package com.pizzaria.soap.views;

import com.pizzaria.soap.daos.ClienteDAO;
import com.pizzaria.soap.daos.PedidoDAO;
import com.pizzaria.soap.models.*;
import com.pizzaria.soap.services.AtualizadorPedidos;

import javax.swing.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class CadastroCliente {
    public static void main(String[] args) {
        AtualizadorPedidos.iniciarAtualizacaoAutomatica();

        // MENU DE ESCOLHA PARA CADASTRAR CLIENTE, FAZER PEDIDO, CONSULTAR STATUS E SAIR DO SIST.
        while (true) {
            String[] opcoes = {"Cadastrar Cliente", "Fazer Pedido", "Consultar Status", "Sair"};
            int escolha = JOptionPane.showOptionDialog(
                    null,
                    "Escolha uma opção:",
                    "Menu Principal",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            switch (escolha) {
                case 0 -> {
                    Cliente cliente = cadastrarCliente();
                    if (cliente != null) {
                        JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso!");
                    }
                }
                case 1 -> {
                    Cliente cliente = selecionarCliente();
                    if (cliente != null) {
                        realizarPedido(cliente);
                    }
                }
                case 2 -> consultarPedido();
                case 3 -> {
                    JOptionPane.showMessageDialog(null, "Saindo do sistema...");
                    System.exit(0);
                }
                default -> JOptionPane.showMessageDialog(null, "Opção inválida!");
            }
        }
    }


    // CADASTRAR USUÁRIO
    private static Cliente cadastrarCliente() {
        Cliente cliente = new Cliente();
        try {

            // NOME
            String nome;
            do {
                nome = JOptionPane.showInputDialog("Digite o seu nome:");
                if (nome == null || nome.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "O nome não pode estar vazio!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } while (nome == null || nome.trim().isEmpty());


            // TELEFONE
            String telefone;
            do {
                telefone = JOptionPane.showInputDialog("Digite o seu telefone:");
                if (telefone == null || telefone.trim().isEmpty() || !telefone.matches("\\d{8,11}")) {
                    JOptionPane.showMessageDialog(null, "Digite um telefone válido!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } while (telefone == null || telefone.trim().isEmpty() || !telefone.matches("\\d{8,11}"));


            // CPF
            String cpf;
            do {
                cpf = JOptionPane.showInputDialog("Digite o seu CPF (apenas números):");
                if (cpf == null || !cpf.matches("\\d{11}")) {
                    JOptionPane.showMessageDialog(null, "Digite um CPF válido com 11 números!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } while (cpf == null || !cpf.matches("\\d{11}"));


            // ENDEREÇO
            String endereco;
            do {
                endereco = JOptionPane.showInputDialog("Digite o seu endereço:");
                if (endereco == null || endereco.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "O endereço não pode estar vazio!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } while (endereco == null || endereco.trim().isEmpty());


            // DATA DE NASCIMENTO
            String dataNascimentoStr;
            Date dataNascimento = null;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
            sdf.setLenient(false);

            do {
                dataNascimentoStr = JOptionPane.showInputDialog("Digite a sua data de nascimento (dd/mm/yyyy):");
                if (dataNascimentoStr == null) {
                    return null;
                }
                try {
                    dataNascimento = sdf.parse(dataNascimentoStr);
                    break;
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(null, "Formato inválido! Use dd/mm/yyyy.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } while (true);

            cliente.setNome(nome);
            cliente.setTelefone(telefone);
            cliente.setCpf(cpf);
            cliente.setEndereco(endereco);
            cliente.setDataNascimento(dataNascimento);

            ClienteDAO clienteDAO = new ClienteDAO();
            clienteDAO.salvar(cliente);

            return cliente;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar cliente: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }
    }


    // SELECIONAR O CLIENTE PELO CPF PARA REALIZAR O PEDIDO DA PIZZA.
    private static Cliente selecionarCliente() {
        String cpf = JOptionPane.showInputDialog("Digite o CPF do cliente para buscar o cadastro:");
        if (cpf == null || !cpf.matches("\\d{11}")) {
            JOptionPane.showMessageDialog(null, "CPF inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        ClienteDAO clienteDAO = new ClienteDAO();
        Optional<Cliente> clienteOpt = Optional.ofNullable(clienteDAO.buscarPorCpf(cpf));

        return clienteOpt.orElseGet(() -> {
            JOptionPane.showMessageDialog(null, "Cliente não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        });
    }


    // REALIZAÇÃO DO PEDIDO
    private static void realizarPedido(Cliente cliente) {
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.RECEBIDO);
        BigDecimal valorTotalPedido = BigDecimal.ZERO;

        boolean adicionarMaisItens = true;
        while (adicionarMaisItens) {
            try {
                String saborPizza;
                do {
                    saborPizza = JOptionPane.showInputDialog("Digite o sabor da pizza:" +
                            "\n Mussarela = R$ 1,00" +
                            "\n Calabresa = R$ 2,00" +
                            "\n Portuguesa = R$ 3,00\n");
                    if (saborPizza == null || saborPizza.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "O sabor não pode estar vazio!", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } while (saborPizza == null || saborPizza.trim().isEmpty());

                String tamanho;
                do {
                    tamanho = JOptionPane.showInputDialog("Escolha o tamanho da pizza (P, M, G):" +
                            "\n P = R$ 20,00" +
                            "\n M = R$ 30,00" +
                            "\n G = R$ 40,00\n");
                    if (tamanho == null || !tamanho.matches("[PMG]")) {
                        JOptionPane.showMessageDialog(null, "Tamanho inválido! Escolha P, M ou G.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } while (!tamanho.matches("[PMG]"));

                int quantidade;
                while (true) {
                    try {
                        quantidade = Integer.parseInt(JOptionPane.showInputDialog("Digite a quantidade de pizzas:"));
                        if (quantidade <= 0) {
                            JOptionPane.showMessageDialog(null, "A quantidade deve ser maior que zero!", "Erro", JOptionPane.ERROR_MESSAGE);
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Digite um número válido!", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }

                String saborBorda = JOptionPane.showInputDialog("Digite o sabor da borda:" +
                        "\n Cheddar =  R$ 5,00 " +
                        "\n Catupiry = R$ 6,00 " +
                        "\n Chocolate = R$ 4,50 " +
                        "\n Outro sabor de Borda  = R$ 4,00 \n" );


                // SOLICITA ALGUMA OBS NO PEDIDO.
                String observacoes = JOptionPane.showInputDialog("Digite observações para o pedido (ex.: sem cebola, sem pimenta):");

                Pizza pizza = new Pizza(saborPizza, tamanho);
                Borda borda = new Borda(saborBorda);
                ItensPedido itemPedido = new ItensPedido(tamanho, quantidade, pizza, borda);

                itemPedido.setObservacoes(observacoes);
                itemPedido.calcularValor();
                valorTotalPedido = valorTotalPedido.add(itemPedido.getValorTotal());

                JOptionPane.showMessageDialog(null,
                        "Item adicionado: " + "\n" + quantidade + "x " + saborPizza + " (" + tamanho + ") " +
                                (borda != null ? "\nBorda: " + borda.getSabor() : "\nSem borda") +
                                (itemPedido.getObservacoes() != null && !itemPedido.getObservacoes().isEmpty()
                                        ? "\nObservações: " + itemPedido.getObservacoes()
                                        : "\nSem observações") +
                                "\nValor: R$ " + itemPedido.getValorTotal() +
                                "\nRecebido.");


                pedido.adicionarItem(itemPedido);

                int opcao = JOptionPane.showConfirmDialog(null, "Deseja adicionar mais itens ao pedido?", "Confirmação", JOptionPane.YES_NO_OPTION);
                adicionarMaisItens = (opcao == JOptionPane.YES_OPTION);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro ao adicionar item ao pedido: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }

        JOptionPane.showMessageDialog(null, "Total de itens no pedido: " + pedido.getItens().size());

        if (!pedido.getItens().isEmpty()) {
            PedidoDAO pedidoDAO = new PedidoDAO();
            Pedido novoPedido = pedidoDAO.salvar(pedido);

            if (novoPedido.getId() == null) {
                JOptionPane.showMessageDialog(null, "Erro: Pedido foi salvo, mas ID não foi retornado!", "Erro", JOptionPane.ERROR_MESSAGE);
            }

            JOptionPane.showMessageDialog(null, "Pedido cadastrado com sucesso!\nNúmero do pedido: " + novoPedido.getId() + "\nValor total: R$ " + valorTotalPedido, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Nenhum item foi adicionado. Pedido não será salvo.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }


    // CONSULTAR PEDIDO PELO ID DO PEDIDO
    private static void consultarPedido() {
        String cpf = JOptionPane.showInputDialog("Digite o seu CPF:");

        if (cpf == null || cpf.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "CPF inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PedidoDAO pedidoDAO = new PedidoDAO();
        List<Pedido> pedidos = pedidoDAO.buscarPorCpf(cpf);

        if (pedidos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum pedido encontrado para o CPF informado.", "Erro", JOptionPane.ERROR_MESSAGE);
        } else {
            StringBuilder mensagem = new StringBuilder("Pedidos encontrados:\n");
            for (Pedido pedido : pedidos) {
                mensagem.append("Pedido #").append(pedido.getId())
                        .append(" - Status: ").append(pedido.getStatus())
                        .append("\n");
            }
            JOptionPane.showMessageDialog(null, mensagem.toString());
        }
    }

}