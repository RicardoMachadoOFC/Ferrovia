package net.javaguides.springboot.model;

public class Rodovia {
    private Aresta aresta;

    public Rodovia(Aresta aresta) {
        this.aresta = aresta;
    }

    public Aresta getAresta() {
        return aresta;
    } 
}
