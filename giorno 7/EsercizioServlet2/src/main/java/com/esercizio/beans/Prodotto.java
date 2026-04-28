package com.esercizio.beans;

/**
 * JavaBean che rappresenta un prodotto con nome, prezzo e disponibilità.
 */
public class Prodotto {

    private String nome;
    private double prezzo;
    private boolean disponibile;

    // Costruttore vuoto (obbligatorio per JavaBean)
    public Prodotto() {}

    // Costruttore di convenienza
    public Prodotto(String nome, double prezzo, boolean disponibile) {
        this.nome = nome;
        this.prezzo = prezzo;
        this.disponibile = disponibile;
    }

    // Getter e Setter
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getPrezzo() { return prezzo; }
    public void setPrezzo(double prezzo) { this.prezzo = prezzo; }

    public boolean isDisponibile() { return disponibile; }
    public void setDisponibile(boolean disponibile) { this.disponibile = disponibile; }
}
