package net.javaguides.springboot.model;

public class Ferrovia {
    private Aresta aresta;

    public Ferrovia(Aresta aresta) {
        this.aresta = aresta;
    }

    public Aresta getAresta() {
        return aresta;
    } 
}
