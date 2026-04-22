package net.javaguides.springboot.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.javaguides.springboot.model.Aresta;
import net.javaguides.springboot.model.Cidade;
import net.javaguides.springboot.model.Grafo;

public class KruskalController {

    public List<Aresta> ordenarArestas(Grafo grafo) {
        List<Aresta> arestas = grafo.getArestas();
        arestas.sort(Comparator.comparingDouble(Aresta::getDistancia));
        for (Aresta aresta : arestas) {
            System.out.println("Origem: " + aresta.getOrigem().getNome() + ", Destino: " + aresta.getDestino().getNome()
                    + ", Distância: " + aresta.getDistancia());
        }
        return arestas;
    }

    public List<Aresta> kruskal(Grafo grafo) {
        Grafo agm = new Grafo();

        for (Cidade cidade : grafo.getCidades()) {
            agm.adicionarCidade(cidade);
        }

        UnionFindController uf = new UnionFindController(grafo.getCidades());

        List<Aresta> arestasOrdenadas = ordenarArestas(grafo);

        for (Aresta aresta : arestasOrdenadas) {
            Cidade origem = aresta.getOrigem();
            Cidade destino = aresta.getDestino();

            if (!uf.find(origem).equals(uf.find(destino))) {
                agm.adicionarAresta(aresta);
                uf.union(origem, destino);
            }

            if (agm.getArestas().size() == grafo.getCidades().size() - 1) {
                break;
            }
        }

        return agm.getArestas();
    }
}