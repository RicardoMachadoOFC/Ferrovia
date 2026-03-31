package net.javaguides.springboot.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.javaguides.springboot.model.Aresta;
import net.javaguides.springboot.model.Cidade;
import net.javaguides.springboot.model.Grafo;

public class KruskalController {

    private List<Aresta> ordenarArestas(Grafo grafo) {
        List<Aresta> arestasUnicas = new ArrayList<>();
        Set<String> chaves = new HashSet<>();
        for (Aresta aresta : grafo.getArestas()) {
            String origem = aresta.getOrigem().getNome();
            String destino = aresta.getDestino().getNome();

            String chave;
            if (origem.compareTo(destino) < 0) {
                chave = origem + "-" + destino;
            } else {
                chave = destino + "-" + origem;
            }

            if (!chaves.contains(chave)) {
                chaves.add(chave);
                arestasUnicas.add(aresta);
            }
        }

        arestasUnicas.sort(Comparator.comparingDouble(Aresta::getDistancia));
        return arestasUnicas;
    }

    public List<Aresta> kruskal(Grafo grafo) {
        ArrayList<Aresta> agm = new ArrayList<>();
        ArrayList<Aresta> arestasOrdenadas = new ArrayList<>(ordenarArestas(grafo));

        UnionFindController uf = new UnionFindController(grafo.getCidades());

        for (Aresta aresta : arestasOrdenadas){
            Cidade origem = aresta.getOrigem();
            Cidade destino = aresta.getDestino();
            if(!uf.find(origem).equals(uf.find(destino))){ 
                agm.add(aresta);
                uf.union(origem, destino);
            } 

            if(agm.size() == grafo.getCidades().size() - 1){
                break;
            }
        }
        return agm;
    }
}