package net.javaguides.springboot.model;

public class RotaCarga {

    private Cidade origem;
    private Cidade destino;
    private int quantidade;

    public RotaCarga(Cidade origem, Cidade destino, int quantidade){
        this.origem = origem;
        this.destino = destino;
        this.quantidade = quantidade;

    }

    public Cidade getOrigem() {
        return origem;
    }

    public Cidade getDestino() {
        return destino;
    }

    public int getQuantidade() {
        return quantidade;
    }

}
