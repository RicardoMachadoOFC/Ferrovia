package net.javaguides.springboot.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.javaguides.springboot.model.Aresta;
import net.javaguides.springboot.model.Grafo;

public class KruskalController {

    public List<Aresta> gerarArvoreMinima(Grafo grafo){

        List<Aresta> resultado = new ArrayList<>();

        if (grafo == null) {
            return resultado;
        }

        List<Aresta> arestas = grafo.getArestas();

        arestas.sort(new Comparator<Aresta>() {
            @Override
            public int compare(Aresta a1, Aresta a2) {
                return Double.compare(a1.getDistancia(), a2.getDistancia());
            }
        });

        return resultado;
    }
}