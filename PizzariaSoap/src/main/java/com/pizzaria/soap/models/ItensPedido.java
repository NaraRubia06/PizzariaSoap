package com.pizzaria.soap.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "itens_pedido")
public class ItensPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String tamanho;
    private int quantidade;

    @Column(name = "valor_unitario", nullable = false)
    private BigDecimal valorUnitario;

    @Column(name = "valor_total", nullable = false)
    private BigDecimal valorTotal;

    @ManyToOne
    @JoinColumn(name = "pizza_id", nullable = false)
    private Pizza pizza;

    @ManyToOne
    @JoinColumn(name = "borda_id", nullable = true) // Para Borda opcional
    private Borda borda;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    private String observacoes;

    public ItensPedido(String tamanho, int quantidade, Pizza pizza, Borda borda) {
        this.tamanho = tamanho;
        this.quantidade = quantidade;
        this.pizza = pizza;
        this.borda = borda;
        calcularValor(); // Calcula o valor automaticamente ao criar o item
    }

    // Getters
    public int getId() { return id; }
    public String getTamanho() { return tamanho; }
    public int getQuantidade() { return quantidade; }
    public BigDecimal getValorUnitario() { return valorUnitario; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public Pizza getPizza() { return pizza; }
    public Borda getBorda() { return borda; }
    public Pedido getPedido() { return pedido; }
    public String getObservacoes() { return observacoes; }

    // Setters com Recalculo Automático
    public void setId(int id) { this.id = id; }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
        calcularValor(); // Recalcula o valor ao alterar o tamanho
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
        calcularValor(); // Recalcula o valor ao alterar a quantidade
    }

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
        calcularValor(); // Recalcula o valor ao alterar a pizza
    }

    public void setBorda(Borda borda) {
        this.borda = borda;
        calcularValor(); // Recalcula o valor ao alterar a borda
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    // calcular o valor total do item do pedido
    public void calcularValor() {
        BigDecimal precoPizza = BigDecimal.ZERO;
        BigDecimal precoBorda = BigDecimal.ZERO;

        if (pizza != null) {
            precoPizza = pizza.getPreco(); // Usa o preço já definido lá na classe Pizza
        }

        if (borda != null && borda.getPreco() != null) {
            precoBorda = borda.getPreco(); // è um BigDecimal, então não precisa de conversão
        }

        this.valorUnitario = precoPizza.add(precoBorda);
        this.valorTotal = this.valorUnitario.multiply(BigDecimal.valueOf(quantidade)).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return "ItensPedido{" +
                "id=" + id +
                ", tamanho='" + tamanho + '\'' +
                ", quantidade=" + quantidade +
                ", valorUnitario=" + valorUnitario +
                ", valorTotal=" + valorTotal +
                ", pizza=" + (pizza != null ? pizza.getSabor() : "N/A") +
                ", borda=" + (borda != null ? borda.getSabor() : "Sem borda") +
                '}';
    }
}