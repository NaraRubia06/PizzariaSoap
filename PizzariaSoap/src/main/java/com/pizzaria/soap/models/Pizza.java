package com.pizzaria.soap.models;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "pizzas") // Define o nome da tabela no banco de dados
public class Pizza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String sabor;
    private String tamanho;

    @Column(name = "preco", nullable = false)
    private BigDecimal preco;

    public Pizza() {}

    public Pizza(String sabor, String tamanho) {
        this.sabor = sabor;
        this.tamanho = tamanho;
        this.preco = definirPrecoPorSaborETamanho(sabor, tamanho);
    }

    // Getters
    public int getId() { return id; }
    public String getSabor() { return sabor; }
    public String getTamanho() { return tamanho; }
    public BigDecimal getPreco() { return preco; }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setSabor(String sabor) {
        this.sabor = sabor;
        this.preco = definirPrecoPorSaborETamanho(sabor, this.tamanho); // Atualiza o preço ao mudar o sabor
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
        this.preco = definirPrecoPorSaborETamanho(this.sabor, tamanho); // Atualiza o preço ao mudar o tamanho
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    // Define o preço da pizza com base no sabor e no tamanho
    private BigDecimal definirPrecoPorSaborETamanho(String sabor, String tamanho) {
        BigDecimal precoBase;

        // soma o preço pelo tamanho
        switch (tamanho.toUpperCase()) {
            case "P" -> precoBase = BigDecimal.valueOf(20.0);
            case "M" -> precoBase = BigDecimal.valueOf(30.0);
            case "G" -> precoBase = BigDecimal.valueOf(40.0);
            default -> precoBase = BigDecimal.valueOf(20.0); // Tamanho padrão
        }

        // soma o acréscimo de preço pelo sabor
        BigDecimal acrescimo = switch (sabor.toLowerCase()) {
            case "mussarela" -> BigDecimal.valueOf(1.0);
            case "calabresa" -> BigDecimal.valueOf(2.0);
            case "portuguesa" -> BigDecimal.valueOf(3.0);
            default -> BigDecimal.valueOf(1.0); // soma para outros sabores
        };

        return precoBase.add(acrescimo);
    }

    @Override
    public String toString() {
        return "Pizza{" +
                "id=" + id +
                ", sabor='" + sabor + '\'' +
                ", tamanho='" + tamanho + '\'' +
                ", preco=" + preco +
                '}';
    }
}