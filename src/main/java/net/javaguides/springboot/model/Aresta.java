package net.javaguides.springboot.model;

public class Aresta {

    private Cidade origem;
    private Cidade destino;
    private int distancia;
    private boolean ferrovia;
     

    public Aresta(Cidade origem, Cidade destino, int distancia) {
        this.origem = origem;
        this.destino = destino;
        this.distancia = distancia;
        this.ferrovia = false;
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

    public boolean isFerrovia() {
        return ferrovia;
    }

    public void setFerrovia(boolean ferrovia) {
        this.ferrovia = ferrovia;
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