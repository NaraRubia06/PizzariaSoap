package com.pizzaria.soap.models;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Borda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String sabor;

    @Column(name = "preco", nullable = false)
    private BigDecimal preco;

    public Borda(String sabor) {
        this.sabor = sabor;
        this.preco = definirPrecoPorSabor(sabor);
    }

    public int getId() { return id; }
    public String getSabor() { return sabor; }

    // getPreco() retorna o preço da borda
    public BigDecimal getPreco() {
        return preco;
    }

    public void setId(int id) { this.id = id; }
    public void setSabor(String sabor) {
        this.sabor = sabor;
        this.preco = definirPrecoPorSabor(sabor);
    }

    public void setPreco(BigDecimal preco) { this.preco = preco; }

    // preço da borda.
    private BigDecimal definirPrecoPorSabor(String sabor) {
        return switch (sabor.toLowerCase()) {
            case "cheddar" -> BigDecimal.valueOf(5.0);
            case "catupiry" -> BigDecimal.valueOf(6.0);
            case "chocolate" -> BigDecimal.valueOf(4.5);
            default -> BigDecimal.valueOf(4.0); // Preço para outras bordas
        };
    }
}