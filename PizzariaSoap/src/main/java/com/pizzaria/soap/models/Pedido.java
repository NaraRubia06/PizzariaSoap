package com.pizzaria.soap.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private StatusPedido status = StatusPedido.RECEBIDO; // Status inicial

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    private String observacoes;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItensPedido> itens = new ArrayList<>();

    // Getters
    public Long getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public String getObservacoes() { return observacoes; }
    public StatusPedido getStatus() { return status; }
    public List<ItensPedido> getItens() { return itens; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public void setStatus(StatusPedido status) { this.status = status; }
    public void setItens(List<ItensPedido> itens) {
        this.itens = itens;
        calcularValorTotal();
    }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }


    public void adicionarItem(ItensPedido itemPedido) {
        this.itens.add(itemPedido);
        itemPedido.setPedido(this);
        calcularValorTotal();
    }

    public void removerItem(ItensPedido itemPedido) {
        this.itens.remove(itemPedido);
        calcularValorTotal();
    }

    private void calcularValorTotal() {
        this.valorTotal = itens.stream()
                .map(ItensPedido::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void avancarStatus() {
        if (this.status == StatusPedido.RECEBIDO) {
            this.status = StatusPedido.EM_PREPARACAO;
        } else if (this.status == StatusPedido.EM_PREPARACAO) {
            this.status = StatusPedido.SAIU_ENTREGA;
        } else if (this.status == StatusPedido.SAIU_ENTREGA) {
            this.status = StatusPedido.ENTREGUE;
        }
    }
}