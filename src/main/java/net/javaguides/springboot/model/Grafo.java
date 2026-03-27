package net.javaguides.springboot.model;

import java.util.ArrayList;
import java.util.List;

public class Grafo {

    private List<Cidade> cidades;
    private List<Aresta> arestas;

    public Grafo() {
        this.cidades = new ArrayList<>();
        this.arestas = new ArrayList<>();
    }

    public void adicionarCidade(Cidade cidade) {
        cidades.add(cidade);
    }

    public void adicionarAresta(Aresta aresta) {
        arestas.add(aresta);
        // Adiciona a aresta inversa para que o grafo seja não direcionado
        arestas.add(new Aresta(aresta.getDestino(), aresta.getOrigem(), aresta.getDistancia()));
    }

    public List<Cidade> getCidades() {
        return cidades;
    }

    public List<Aresta> getArestas() {
        return arestas;
    }
}