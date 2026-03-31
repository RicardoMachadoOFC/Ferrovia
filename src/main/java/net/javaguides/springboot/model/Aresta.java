package net.javaguides.springboot.model;

public class Aresta {

    private Cidade origem;
    private Cidade destino;
    private int distancia;
     

    public Aresta(Cidade origem, Cidade destino, int distancia) {
        this.origem = origem;
        this.destino = destino;
        this.distancia = distancia;
    }

    public Cidade getOrigem() {
        return origem;
    }

    public void setOrigem(Cidade origem) {
        this.origem = origem;
    }

    public Cidade getDestino() {
        return destino;
    }

    public void setDestino(Cidade destino) {
        this.destino = destino;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    @Override
    public String toString() {
        return "Aresta{" +
                "origem=" + origem +
                ", destino=" + destino +
                ", distancia=" + distancia +
                '}';
    }
}